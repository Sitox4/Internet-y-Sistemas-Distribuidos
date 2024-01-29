package es.udc.ws.app.model.partido;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public abstract class AbstractSqlPartidoDao implements SqlPartidoDao {
    protected AbstractSqlPartidoDao(){}

    @Override
    public List<Partido> find(Connection connection, Long partidoId) throws InstanceNotFoundException {

        List<Partido> partidos = new ArrayList<>();
        String queryString = "SELECT partidoId, nombreVisitante, celebracion, " +
                " precio, maxEntradas, vendidas, alta FROM Partido ";

        if(partidoId!=0){
            queryString += "WHERE partidoId = ?";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            if(partidoId!=0) {
                int i = 1;
                preparedStatement.setLong(i++, partidoId);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

          /*  if(!resultSet.next()){
                throw new InstanceNotFoundException(partidoId,
                        Partido.class.getName());
            }*/
            while (resultSet.next()) {
            int i= 1;
            Long id = resultSet.getLong(i++);
            String nombreVisitante = resultSet.getString(i++);
            Timestamp celebracionTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime celebracion = celebracionTimestamp.toLocalDateTime();
            float precio = resultSet.getFloat(i++);
            int maxEntradas = resultSet.getInt(i++);
            int vendidas = resultSet.getInt(i++);
            Timestamp altaTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime alta = altaTimestamp.toLocalDateTime();

            partidos.add(new Partido(id, nombreVisitante, celebracion, precio, maxEntradas, vendidas, alta));
            }
            return partidos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long partidoId) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Partido WHERE" + " partidoId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            int i = 1;
            preparedStatement.setLong(i++, partidoId);

            int removedRows = preparedStatement.executeUpdate();

            if(removedRows == 0){
                throw new InstanceNotFoundException(partidoId,
                        Partido.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Partido> findByDate(Connection connection, LocalDateTime fechainicio, LocalDateTime fechafin) {
        String queryString = "SELECT partidoId, nombreVisitante, celebracion, " +
                " precio, maxEntradas, vendidas, alta FROM Partido WHERE celebracion > ?"
                + " AND celebracion < ? AND celebracion > ?";
        List<Partido> partidos = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;

            preparedStatement.setTimestamp(i++, Timestamp.valueOf(fechainicio));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(fechafin));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(LocalDateTime.now().withNano(0)));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                i = 1;
                Long partidoId = resultSet.getLong(i);
                i++;
                String nombreVisitante = resultSet.getString(i);
                i++;
                LocalDateTime celebracion = resultSet.getTimestamp(i).toLocalDateTime().withNano(0);
                i++;
                float precio = resultSet.getFloat(i);
                i++;
                int maxEntradas = resultSet.getInt(i);
                i++;
                int vendidas = resultSet.getInt(i);
                i++;
                LocalDateTime alta = resultSet.getTimestamp(i).toLocalDateTime().withNano(0);

                partidos.add(new Partido(partidoId, nombreVisitante, celebracion, precio, maxEntradas, vendidas, alta));

            }
            return partidos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void update(Connection connection, Partido partido) throws  InstanceNotFoundException{

        //partido.setVendidas(partido.getVendidas()+entradas);

        String queryString = "UPDATE Partido " +
                "SET nombreVisitante = ?, " +
                " celebracion = ?, " +
                " precio = ?, " +
                " maxEntradas = ?, " +
                " vendidas = ? WHERE partidoId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++,partido.getNombreVisitante() );
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getCelebracion()));
            preparedStatement.setFloat(i++, partido.getPrecio());
            preparedStatement.setInt(i++, partido.getMaxEntradas());
            preparedStatement.setInt(i++, partido.getVendidas());
            preparedStatement.setLong(i++, partido.getPartidoId());

            /* Execute query. */
            int updateRows= preparedStatement.executeUpdate();


            if(updateRows == 0){
                throw new InstanceNotFoundException(partido.getPartidoId(),
                        Partido.class.getName());
            }

        } catch(SQLException e){
            throw new RuntimeException(e);
        }

    }

}


