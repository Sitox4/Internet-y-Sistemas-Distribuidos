package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.PartidosService.Exceptions.*;
import es.udc.ws.app.model.PartidosService.PartidoService;
import es.udc.ws.app.model.PartidosService.PartidoServiceFactory;
import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.app.model.compra.SqlCompraDao;
import es.udc.ws.app.model.compra.SqlCompraDaoFactory;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partido.SqlPartidoDao;
import es.udc.ws.app.model.partido.SqlPartidoDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

import jakarta.servlet.http.Part;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static  es.udc.ws.app.model.util.ModelConstants.MAX_PRICE;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {


    private final long NON_EXISTENT_PARTIDO_ID = -1;

    private static PartidoService partidoService = null;
    private static SqlPartidoDao partidoDao = null;

    private static SqlCompraDao compraDao = null;
    @BeforeAll
    public static void init(){
        DataSource dataSource = new SimpleDataSource();

        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);
        partidoService = PartidoServiceFactory.getService();

        partidoDao = SqlPartidoDaoFactory.getDao();
        compraDao = SqlCompraDaoFactory.getDao();
    }

    private Partido getValidPartido(String nombreVisitante){
        LocalDateTime celebracion = LocalDateTime.now().plusDays(5).withNano(0);
        return new Partido(nombreVisitante, celebracion,33,3,0,LocalDateTime.now());
    }

    private Partido getValidPartido2Vendidas(String nombreVisitante){
        LocalDateTime celebracion = LocalDateTime.now().plusDays(5).withNano(0);
        return new Partido(nombreVisitante, celebracion,33,3,0,LocalDateTime.now());
    }

    private Partido getValidPartido(){
        return getValidPartido("Osasuna");
    }

    private Partido createPartido(Partido partido){
        Partido addedPartido = null;
        try{
            addedPartido = partidoService.addPartido(partido);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedPartido;
    }

    public void removePartido(Long partidoId){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                partidoDao.remove(connection, partidoId);

                connection.commit();

            } catch (SQLException | InstanceNotFoundException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeCompra(Long compraId){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

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


    @Test
    public void testAddPartidoAndFinById() throws InstanceNotFoundException{
        Partido partido = getValidPartido();
        Partido addedPartido = null;

        try {
            addedPartido = createPartido(partido);

            Partido foundPartido = partidoService.findById(addedPartido.getPartidoId());

            assertEquals(addedPartido, foundPartido);
            assertEquals(foundPartido.getNombreVisitante(), partido.getNombreVisitante());
            assertEquals(foundPartido.getCelebracion(), partido.getCelebracion());
            assertEquals(foundPartido.getPrecio(), partido.getPrecio());
            assertEquals(foundPartido.getMaxEntradas(), partido.getMaxEntradas());
            assertEquals(foundPartido.getVendidas(), partido.getVendidas());
            assertEquals(foundPartido.getAlta(), partido.getAlta());


        } finally {
            if (addedPartido != null){
                removePartido(addedPartido.getPartidoId());
            }
        }
    }

    @Test
    public void testfinByIdCompras() throws InputValidationException, InstanceNotFoundException, PartidoYaComenzadoException, NoQuedanEntradasException {
        Partido partido = getValidPartido();
        Partido addedPartido = null;
        List<Compra> compras = new LinkedList<>();


        try {
            addedPartido = createPartido(partido);

            Compra compra1 = partidoService.buyEntradas(addedPartido.getPartidoId(), "email1", "1111222233334444", 2);

            Compra foundCompra = partidoService.findByIdCompra(compra1.getCompraId());

            compras.add(compra1);

            assertEquals(compra1, foundCompra);
            assertEquals(compra1.getEmail(), foundCompra.getEmail());
            assertEquals(compra1.getFechahoraCompra(), foundCompra.getFechahoraCompra());
            assertEquals(compra1.getNumEntradas(), foundCompra.getNumEntradas());
            assertEquals(compra1.getNumTarjeta(), foundCompra.getNumTarjeta());
            assertEquals(compra1.isRecogidas(), foundCompra.isRecogidas());
            assertEquals(compra1.getPartidoId(), foundCompra.getPartidoId());

            Partido foundPartido = partidoService.findById(addedPartido.getPartidoId());

            assertEquals(foundPartido.getVendidas(), 2);


        } finally {
            for( Compra compra : compras) {
                removeCompra(compra.getCompraId());
            }
            if (addedPartido != null) {
                removePartido(addedPartido.getPartidoId());
            }
        }
    }

    @Test
    public void testAddInvalidPartido(){
        //Nombre de equipo visitante nulo
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setNombreVisitante(null);
            Partido addedPartido = partidoService.addPartido(partido);
            removePartido(addedPartido.getPartidoId());
        });

        //Supera el precio máximo establecido para una entrada que es 300
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setPrecio(MAX_PRICE + 5);
            Partido addedPartido = partidoService.addPartido(partido);
            removePartido(addedPartido.getPartidoId());
        });


    }

    @Test
    public void testFindNonExistentPartido(){
        assertThrows(InstanceNotFoundException.class, () -> partidoService.findById(NON_EXISTENT_PARTIDO_ID));
    }


    @Test
    public  void testfindPartidos() throws InputValidationException {

        List<Partido> partidos = new LinkedList<>();



        try{

            LocalDateTime fechainicio = LocalDateTime.now().withNano(0).minusDays(5);
            LocalDateTime fechafin = LocalDateTime.now().withNano(0).plusDays(10);


            LocalDateTime fecha1 = LocalDateTime.now().plusDays(6).withNano(0);
            LocalDateTime fecha2 = LocalDateTime.now().plusDays(15).withNano(0);
            //La fecha de fin sera hora + 10 dias
            Partido partido3aux = new Partido("Equipo 3",fecha1,33, 3, 0, LocalDateTime.now());
            //Partido fuera del rango de dias introducido
            Partido partido4aux = new Partido("Equipo 4", fecha2, 32, 3,0,LocalDateTime.now());



            Partido partido1 = createPartido(getValidPartido("Equipo 1"));
            Partido partido2 = createPartido(getValidPartido("Equipo 2"));
            Partido partido3 = partidoService.addPartido(partido3aux);
            Partido partido4 = partidoService.addPartido(partido4aux);

            partidos.add(partido1);
            partidos.add(partido2);
            partidos.add(partido3);
            partidos.add(partido4);

            List<Partido> foundPartidos = partidoService.findPartidos(fechainicio, fechafin);
            assertEquals(3, foundPartidos.size());
            assertEquals(partido1.getPartidoId(), foundPartidos.get(0).getPartidoId());
            assertEquals(partido2.getPartidoId(), foundPartidos.get(1).getPartidoId());
            assertEquals(partido3.getPartidoId(), foundPartidos.get(2).getPartidoId());


        } finally {
            for (Partido partido : partidos){
                removePartido(partido.getPartidoId());
            }
        }
    }
    @Test
    public  void testfindAll() throws InputValidationException, InstanceNotFoundException {

        List<Partido> partidos = new LinkedList<>();



        try{

            LocalDateTime fechainicio = LocalDateTime.now().withNano(0).minusDays(5);
            LocalDateTime fechafin = LocalDateTime.now().withNano(0).plusDays(10);


            LocalDateTime fecha1 = LocalDateTime.now().plusDays(6).withNano(0);
            LocalDateTime fecha2 = LocalDateTime.now().plusDays(15).withNano(0);
            //La fecha de fin sera hora + 10 dias
            Partido partido3aux = new Partido("Equipo 3",fecha1,33, 3, 0, LocalDateTime.now());
            //Partido fuera del rango de dias introducido
            Partido partido4aux = new Partido("Equipo 4", fecha2, 32, 3,0,LocalDateTime.now());



            Partido partido1 = createPartido(getValidPartido("Equipo 1"));
            Partido partido2 = createPartido(getValidPartido("Equipo 2"));
            Partido partido3 = partidoService.addPartido(partido3aux);
            Partido partido4 = partidoService.addPartido(partido4aux);

            partidos.add(partido1);
            partidos.add(partido2);
            partidos.add(partido3);
            partidos.add(partido4);

            List<Partido> foundPartidos = partidoService.findAll();
            assertEquals(4, foundPartidos.size());
            assertEquals(partido1.getPartidoId(), foundPartidos.get(0).getPartidoId());
            assertEquals(partido2.getPartidoId(), foundPartidos.get(1).getPartidoId());
            assertEquals(partido3.getPartidoId(), foundPartidos.get(2).getPartidoId());


        } finally {
            for (Partido partido : partidos){
                removePartido(partido.getPartidoId());
            }
        }
    }
    @Test
    public  void testCompra() throws InputValidationException, PartidoYaComenzadoException, NoQuedanEntradasException {

        List<Partido> partidos = new LinkedList<>();
        List<Compra> compras = new LinkedList<>();


        try {


            LocalDateTime fecha1 = LocalDateTime.now().plusDays(16).withNano(0);
            LocalDateTime fechaAyer = LocalDateTime.now().plusDays(1).withNano(0);

            Partido partido1 = getValidPartido("Equipo 1");
            Partido partidoAyer = new Partido("Equipo",fechaAyer,33, 3, 0, LocalDateTime.now());
            Partido vendidas = getValidPartido2Vendidas("Vendidas");

            Partido partidoaux = partidoService.addPartido(partido1);
            Partido partidoauxAyer = partidoService.addPartido(partidoAyer);
            Partido partidoVendidas= partidoService.addPartido(vendidas);


            partidos.add(partidoaux);
            partidos.add(partidoauxAyer);
            partidos.add(partidoVendidas);

            Compra compra = partidoService.buyEntradas(partidoaux.getPartidoId(), "email", "1111222233334444",2);
            compras.add(compra);

            assertEquals(compras.size(), 1);
            //assertThrows(PartidoYaComenzadoException.class, () -> partidoService.buyEntradas(partidoauxAyer.getPartidoId(), "email", "1111222233334444", 2));
            assertThrows(NoQuedanEntradasException.class, () -> partidoService.buyEntradas(partidoVendidas.getPartidoId(), "email2", "1112222333344441",4) );




        }finally {
                for (Partido partido : partidos){
                    removePartido(partido.getPartidoId());
                }

            }

    }

    @Test
    public  void testfindCompras() throws InputValidationException, InstanceNotFoundException, PartidoYaComenzadoException, NoQuedanEntradasException {

        List<Partido> partidos = new LinkedList<Partido>();
        List<Compra> compras = new LinkedList<Compra>();



        try{

            Partido partido1 = createPartido(getValidPartido("Equipo 1"));
            Partido partido2 = createPartido(getValidPartido("Equipo 2"));
            partidos.add(partido1);
            partidos.add(partido2);

            Compra compra1 = partidoService.buyEntradas(partido1.getPartidoId(), "email", "1111222233334444",2);
            Compra compra2 = partidoService.buyEntradas(partido2.getPartidoId(), "email", "1111222233334445",2);
            Compra compra3 = partidoService.buyEntradas(partido2.getPartidoId(), "email2", "1111222233334444",1);


            compras.add(compra1);
            compras.add(compra2);
            compras.add(compra3);

            List<Compra> foundComprasEmail1 = partidoService.findCompra("email");
            List<Compra> foundComprasEmail2 = partidoService.findCompra("email2");

            assertEquals(foundComprasEmail1.size(), 2);

            assertEquals(partido1.getPartidoId(), foundComprasEmail1.get(0).getPartidoId());
            assertEquals(partido2.getPartidoId(), foundComprasEmail1.get(1).getPartidoId());

            assertEquals("email", foundComprasEmail1.get(1).getEmail());
            assertEquals(("1111222233334444"),foundComprasEmail2.get(0).getNumTarjeta());


        } finally {
            for( Compra compra : compras) {
                removeCompra(compra.getCompraId());
            }
            for (Partido partido : partidos){
                removePartido(partido.getPartidoId());
            }
        }
    }

    @Test
    public void testRecogidaEntradas() throws InputValidationException, InvalidNumTarjetaException, InstanceNotFoundException, AlreadyCollectException, PartidoYaComenzadoException, NoQuedanEntradasException {
        Partido partido = getValidPartido();
        Partido addedPartido = null;
        try {
            addedPartido = createPartido(partido);

            Compra compra1 = partidoService.buyEntradas(addedPartido.getPartidoId(), "email1", "1111222233334444", 2);
            partidoService.recogidaCompra(compra1.getCompraId(), "1111222233334444");
            Compra comprafinal = partidoService.findByIdCompra(compra1.getCompraId());
            assertTrue(comprafinal.isRecogidas());


        } finally {
            if(addedPartido != null){
                removePartido(addedPartido.getPartidoId());
            }
        }


    }

    @Test
    public void testRecogidaEntradasTarjetaErronea() throws InputValidationException, PartidoYaComenzadoException, NoQuedanEntradasException {
        Partido partido = getValidPartido();
        Partido addedPartido = null;

        try {
            addedPartido = createPartido(partido);

            Compra compra2 = partidoService.buyEntradas(addedPartido.getPartidoId(), "email2", "1111222233334444", 1);

            assertThrows(InvalidNumTarjetaException.class, () ->
                    partidoService.recogidaCompra(compra2.getCompraId(), "1111"));

        } finally {
            if(addedPartido != null){
                removePartido(addedPartido.getPartidoId());
            }
        }
    }
}