package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidNumTarjetaException;
import es.udc.ws.app.client.service.exceptions.ClientNoQuedanEntradasException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoYaComenzadoException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientPartidoService {
    Long addPartido(ClientPartidoDto partido) throws InputValidationException;
    ClientPartidoDto findById(Long partidoId) throws InstanceNotFoundException;
    List<ClientPartidoDto> findPartidos(LocalDateTime fechainicio, LocalDateTime fechafin) throws InputValidationException;

    Long comprarEntradas(Long partidoId, String email, String tarjBanc, int numEntradas) throws InputValidationException, ClientNoQuedanEntradasException, ClientInvalidNumTarjetaException, ClientPartidoYaComenzadoException;

    List<ClientCompraDto> findEntradas(String Email) throws InputValidationException;

    //ClientCompraDto findByIdCompra(Long compraId) throws InstanceNotFoundException;

    void recogerEntradas(Long compraId, String tarjBanc) throws InputValidationException, ClientAlreadyCollectException;
}
