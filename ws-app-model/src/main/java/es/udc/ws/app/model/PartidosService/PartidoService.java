package es.udc.ws.app.model.PartidosService;

import es.udc.ws.app.model.PartidosService.Exceptions.*;
import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.app.model.PartidosService.Exceptions.AlreadyCollectException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface PartidoService {

    Partido addPartido(Partido partido) throws InputValidationException;

    List<Partido> findPartidos(LocalDateTime fechainicio, LocalDateTime fechafin) throws InputValidationException;
    List<Partido> findAll() throws InstanceNotFoundException;


    Partido findById(Long partidoId) throws InstanceNotFoundException;

    Compra buyEntradas(Long partidoId, String Email, String tarjBanc, int numEntradas) throws InputValidationException, PartidoYaComenzadoException, NoQuedanEntradasException;

    List<Compra> findCompra(String email) throws InstanceNotFoundException;

    void removeCompra(Long compraId) throws InstanceNotFoundException;

    void recogidaCompra(Long compraId, String tarjBanc) throws InstanceNotFoundException, AlreadyCollectException, InputValidationException,InvalidNumTarjetaException;

    Compra findByIdCompra(Long compraId) throws InstanceNotFoundException;
}
