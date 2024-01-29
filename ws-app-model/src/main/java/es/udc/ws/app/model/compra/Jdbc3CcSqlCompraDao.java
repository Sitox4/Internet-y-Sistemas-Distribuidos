package es.udc.ws.app.model.compra;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class Jdbc3CcSqlCompraDao extends AbstractSqlCompraDao{

    @Override
    public Compra create(Connection connection, Compra compra) {
        String queryString = "INSERT INTO Compra"
                + " (partidoId, email, tarjetaBancaria, numEntradasCompradas, fechaHoraCompra, recogidas)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){
            int i = 1;
            preparedStatement.setLong(i++, compra.getPartidoId());
            preparedStatement.setString(i++, compra.getEmail());
            preparedStatement.setString(i++, compra.getNumTarjeta());
            preparedStatement.setInt(i++, compra.getNumEntradas());

            LocalDate fecha = LocalDate.now();
            LocalTime hora = LocalTime.now();
            LocalDateTime fechahoraCompra = LocalDateTime.of(fecha,hora).withNano(0);

            compra.setFechahoraCompra(fechahoraCompra);
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(compra.getFechahoraCompra()));

            preparedStatement.setBoolean(i++, false);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()){
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }

            Long compraId = resultSet.getLong(1);

            return new Compra(compraId, compra.getPartidoId(), compra.getEmail(), compra.getNumTarjeta(), compra.getNumEntradas(), compra.getFechahoraCompra(), compra.isRecogidas());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
