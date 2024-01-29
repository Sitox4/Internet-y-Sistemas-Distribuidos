package es.udc.ws.app.model.compra;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlCompraDao implements SqlCompraDao{
    protected AbstractSqlCompraDao(){}


    @Override
    public List<Compra> find(Connection connection, String email){

        String queryString = "SELECT compraId, partidoId, tarjetaBancaria, " +
                " numEntradasCompradas, fechaHoraCompra, recogidas FROM Compra WHERE LOWER(email) = LOWER(?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Compra> compras = new ArrayList<>();

            while (resultSet.next()) {
                i = 1;
                Long compraId = resultSet.getLong(i++);
                Long partidoId = resultSet.getLong(i++);
                String tarjetaBancaria = resultSet.getString(i++);
                int numEntradasCompradas = resultSet.getInt(i++);
                Timestamp celebracionTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime celebracion = celebracionTimestamp.toLocalDateTime();
                boolean recogidas = resultSet.getBoolean(i++);

                compras.add(new Compra(compraId, partidoId, email, tarjetaBancaria, numEntradasCompradas, celebracion, recogidas));
            }
            return compras;
        }
            catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long compraId) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Compra WHERE " + " compraId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i++, compraId);
            int removedRows = preparedStatement.executeUpdate();

            if(removedRows == 0){
                throw new InstanceNotFoundException(compraId,
                        Compra.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Compra findByIdCompra(Connection connection, Long compraId) throws InstanceNotFoundException{
        String queryString = "SELECT partidoId, email, tarjetaBancaria, numEntradasCompradas, fechaHoraCompra, recogidas FROM Compra WHERE compraId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int  i = 1;
            preparedStatement.setLong(i++, compraId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new InstanceNotFoundException(compraId,
                        Compra.class.getName());
            }
            i=1;
            Long partidoId = resultSet.getLong(i++);
            String email = resultSet.getString(i++);
            String tarjetaBancaria = resultSet.getString(i++);
            int numEntradas = resultSet.getInt(i++);
            Timestamp fecha = resultSet.getTimestamp(i++);
            LocalDateTime fechaHoraCompra = fecha.toLocalDateTime();
            boolean recogida = resultSet.getBoolean(i++);

            return new Compra(compraId,partidoId,email,tarjetaBancaria,numEntradas,fechaHoraCompra,recogida);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Connection connection, Compra compra){
        String queryString = "UPDATE Compra SET recogidas = ? WHERE compraId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setBoolean(i++, compra.isRecogidas());
            preparedStatement.setLong(i, compra.getCompraId());


            int updatedRows = preparedStatement.executeUpdate();

            if(updatedRows == 0){
                throw new InstanceNotFoundException(compra.getCompraId(), Compra.class.getName());
            }



        } catch (SQLException | InstanceNotFoundException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

    }

}
