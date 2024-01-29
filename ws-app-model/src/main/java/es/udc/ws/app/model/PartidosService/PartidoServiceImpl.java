package es.udc.ws.app.model.PartidosService;

import es.udc.ws.app.model.PartidosService.Exceptions.*;
import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.app.model.compra.SqlCompraDao;
import es.udc.ws.app.model.compra.SqlCompraDaoFactory;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partido.SqlPartidoDao;
import es.udc.ws.app.model.partido.SqlPartidoDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static es.udc.ws.app.model.util.ModelConstants.MAX_PRICE;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PartidoServiceImpl implements PartidoService {
    private final DataSource dataSource;
    private SqlPartidoDao partidoDao = null;
    private SqlCompraDao compraDao = null;

    public PartidoServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        partidoDao = SqlPartidoDaoFactory.getDao();
        compraDao = SqlCompraDaoFactory.getDao();
    }

    private void validatePartido(Partido partido) throws InputValidationException {
        PropertyValidator.validateMandatoryString("nombreVisitante", partido.getNombreVisitante());
        PropertyValidator.validateDouble("precio", partido.getPrecio(), 0, MAX_PRICE);
    }

    //Funcionalidad 1:Dar de alta un partido
    @Override
    public Partido addPartido(Partido partido) throws InputValidationException {

        validatePartido(partido);
        partido.setAlta(LocalDateTime.now().withNano(0));
        partido.setVendidas(0);

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);


                Partido partidoCreado = partidoDao.create(connection, partido);

                if(partidoCreado.getMaxEntradas() == 0){
                    throw new InputValidationException("No se pueden añadir partidos sin entradas disponibles");
                }else if (partidoCreado.getCelebracion().isBefore(LocalDateTime.now())){
                    throw new InputValidationException("No se pueden crear partidos con fecha anterior a hoy");
                }
                connection.commit();

                return partidoCreado;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Funcionalidad 2:Buscar un partido entre dos fechas
    @Override
    public List<Partido> findPartidos(LocalDateTime fechainicio, LocalDateTime fechafin) throws InputValidationException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            if (fechainicio.isAfter(fechafin)) {
                connection.rollback();
                throw new InputValidationException("La fecha de inicio esta después de la fecha final");
            }
            return partidoDao.findByDate(connection, fechainicio, fechafin);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InputValidationException e) {
            throw new InputValidationException("No se encuentran partidos en el pasado");
        }
    }


    @Override
    public Partido findById(Long partidoId) throws InstanceNotFoundException {


        try (Connection connection = dataSource.getConnection()) {
            if(partidoId<0){
            throw new InstanceNotFoundException(partidoId, null);
            }
            Partido partido = partidoDao.find(connection, partidoId).get(0);
            return partido;

        }catch (InstanceNotFoundException e){
            throw e;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Partido> findAll() throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return partidoDao.find(connection, 0L);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Compra findByIdCompra(Long compraId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()){
            return compraDao.findByIdCompra(connection,compraId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Compra buyEntradas(Long partidoId, String Email, String tarjBanc, int numEntradas) throws PartidoYaComenzadoException, NoQuedanEntradasException, InputValidationException {
        PropertyValidator.validateCreditCard(tarjBanc);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Partido partido = partidoDao.find(connection,partidoId).get(0);
            LocalDateTime horaActual = LocalDateTime.now();

            if (horaActual.isAfter(partido.getCelebracion())) {
                connection.rollback();
                throw new PartidoYaComenzadoException(partido.getPartidoId());
            }
            if (partido.getVendidas() + numEntradas > partido.getMaxEntradas()) {
                connection.rollback();
                throw new NoQuedanEntradasException(partido.getPartidoId());
            }
            if(numEntradas == 0) throw new InputValidationException("No puedes comprar 0 entradas");

            partido.setVendidas(partido.getVendidas()+numEntradas);
            partidoDao.update(connection, partido);


            Compra compra = compraDao.create(connection, new Compra(partido.getPartidoId(), Email, tarjBanc, numEntradas, LocalDateTime.now()));
            connection.commit();

            return compra;

        } catch (SQLException | InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Compra> findCompra(String email){
        try (Connection connection = dataSource.getConnection()) {
            return compraDao.find(connection, email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeCompra(Long compraId){

        try(Connection connection = dataSource.getConnection()){

            try{
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                compraDao.remove(connection,compraId);
                connection.commit();
            } catch (SQLException | InstanceNotFoundException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void recogidaCompra(Long compraId, String tarjBanc) throws AlreadyCollectException,InvalidNumTarjetaException, InputValidationException {

        try(Connection connection = dataSource.getConnection()){
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            Compra compra = compraDao.findByIdCompra(connection,compraId);


            if(compra.isRecogidas()){
                connection.rollback();
                throw new AlreadyCollectException(compraId);
            }
            if(!compra.getNumTarjeta().equals(tarjBanc)){
                connection.rollback();
                throw new InvalidNumTarjetaException(compraId);
            }

            PropertyValidator.validateCreditCard(tarjBanc);

            compra.setRecogidas(true);
            compraDao.update(connection,compra);
            connection.commit();


    } catch (SQLException | InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}