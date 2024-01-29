package es.udc.ws.app.model.partido;

import java.net.ConnectException;
import java.sql.Connection;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.util.List;
import java.time.LocalDateTime;

public interface SqlPartidoDao {
    Partido create(Connection connection, Partido partido);
    List<Partido> find(Connection connection, Long partidoId) throws InstanceNotFoundException;
    void remove(Connection connection, Long partidoId) throws InstanceNotFoundException;
    List<Partido> findByDate(Connection connection, LocalDateTime fechainicio, LocalDateTime fechafin);
    void update(Connection connection, Partido partido) throws InstanceNotFoundException;

}

