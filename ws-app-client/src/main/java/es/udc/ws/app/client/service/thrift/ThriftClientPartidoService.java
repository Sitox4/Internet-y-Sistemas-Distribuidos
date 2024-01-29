package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidNumTarjetaException;
import es.udc.ws.app.client.service.exceptions.ClientNoQuedanEntradasException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoYaComenzadoException;
import es.udc.ws.app.thrift.ThriftInputValidationException;
import es.udc.ws.app.thrift.ThriftPartidoService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftClientPartidoService implements ClientPartidoService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "ThriftClientPartidoService.endpointAddress";

    private final static String endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public Long addPartido(ClientPartidoDto partido) throws InputValidationException {
        ThriftPartidoService.Client client = getClient();
        try(TTransport transport = client.getInputProtocol().getTransport()){
            transport.open();
            return client.addPartido(ClientPartidoDtoToThriftPartidoDtoConversor.toThriftPartidoDto(partido)).getPartidoId();
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPartidoDto findById(Long partidoId) throws InstanceNotFoundException {
        return null;
    }

    @Override
    public List<ClientPartidoDto> findPartidos(LocalDateTime fechainicio, LocalDateTime fechafin) throws InputValidationException {
        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return ClientPartidoDtoToThriftPartidoDtoConversor.toClientPartidoDtos(client.findPartidos(String.valueOf(fechainicio), String.valueOf(fechafin)));
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long comprarEntradas(Long partidoId, String email, String tarjBanc, int numEntradas) throws InputValidationException, ClientNoQuedanEntradasException, ClientInvalidNumTarjetaException, ClientPartidoYaComenzadoException {
        return null;
    }

    @Override
    public List<ClientCompraDto> findEntradas(String Email) throws InputValidationException {
        return null;
    }

    @Override
    public void recogerEntradas(Long compraId, String tarjBanc) throws InputValidationException, ClientAlreadyCollectException {

    }

    private ThriftPartidoService.Client getClient(){
        try{
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);
            return new ThriftPartidoService.Client(protocol);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }
}
