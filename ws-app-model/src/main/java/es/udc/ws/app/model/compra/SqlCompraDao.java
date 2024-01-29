package es.udc.ws.app.model.compra;

import es.udc.ws.app.model.PartidosService.Exceptions.AlreadyCollectException;
import es.udc.ws.app.model.PartidosService.Exceptions.InvalidNumTarjetaException;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SqlCompraDao {
    Compra create (Connection connection, Compra compra);
    List<Compra> find(Connection connection, String email);
    void remove(Connection connection, Long compraId) throws InstanceNotFoundException;

    Compra findByIdCompra(Connection connection, Long compraId) throws InstanceNotFoundException;

    void update(Connection connection, Compra compra) throws InstanceNotFoundException;
}
