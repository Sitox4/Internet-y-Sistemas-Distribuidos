package es.udc.ws.app.model.partido;

import java.sql.*;

public class Jdbc3CcSqlPartidoDao extends AbstractSqlPartidoDao {
    @Override
    public Partido create(Connection connection, Partido partido) {
        String queryString = "INSERT INTO Partido"
                + " (nombreVisitante, celebracion, precio, maxEntradas, vendidas, alta)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){
            int i = 1;

            preparedStatement.setString(i++, partido.getNombreVisitante());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getCelebracion()));
            preparedStatement.setFloat(i++,partido.getPrecio());
            preparedStatement.setInt(i++, partido.getMaxEntradas());
            preparedStatement.setInt(i++, partido.getVendidas());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getAlta()));

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()){
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long partidoId = resultSet.getLong(1);

            return new Partido(partidoId, partido.getNombreVisitante(), partido.getCelebracion(), partido.getPrecio(), partido.getMaxEntradas(),partido.getVendidas(),
                    partido.getAlta());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}