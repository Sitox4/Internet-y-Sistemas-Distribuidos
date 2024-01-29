package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidNumTarjetaException;
import es.udc.ws.app.client.service.exceptions.ClientNoQuedanEntradasException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoYaComenzadoException;
import es.udc.ws.app.client.service.rest.json.JsonToClientCompraDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientPartidoDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.http.HttpStatus;
import org.apache.hc.client5.http.fluent.Form;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RestClientPartidoService implements ClientPartidoService{
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientService.endpointAddress";
    private String endpointAddress;


    @Override
    public Long addPartido(ClientPartidoDto partido) throws InputValidationException {
        try{

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "partidos").
                    bodyStream(toInputStream(partido), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientPartidoDtoConversor.toClientPartidoDto(response.getEntity().getContent()).getPartidoId();

        } catch (InputValidationException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPartidoDto findById(Long partidoId) throws InstanceNotFoundException {

        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "partidos/" + partidoId).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientPartidoDtoConversor.toClientPartidoDto(response.getEntity().getContent());

        } catch (InstanceNotFoundException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPartidoDto> findPartidos(LocalDateTime fechainicio, LocalDateTime fechafin) throws InputValidationException {

        if(fechainicio == null || fechainicio.equals("") || fechafin == null || fechafin.equals("")){
            throw new InputValidationException("Las fechas no pueden ser nulas o estar vacias");
        }
        try{
                ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "partidos?fechainicio="
                        + URLEncoder.encode(fechainicio.toString(), UTF_8) +
                        "&fechafin=" + URLEncoder.encode(fechafin.toString(), UTF_8)).execute().returnResponse();
                validateStatusCode(HttpStatus.SC_OK, response);
                return JsonToClientPartidoDtoConversor.toClientPartidoDtos(response.getEntity().getContent());
            } catch (Exception e) {
                throw new RuntimeException(e);
        }
    }

    @Override
    public Long comprarEntradas(Long partidoId, String email, String tarjBanc, int numEntradas) throws InputValidationException, ClientNoQuedanEntradasException,
            ClientInvalidNumTarjetaException, ClientPartidoYaComenzadoException{

        try{
            ClassicHttpResponse entrada = (ClassicHttpResponse) Request.post(getEndpointAddress() + "compras").
                    bodyForm(
                            Form.form().
                                    add("partidoId", Long.toString(partidoId)).
                                    add("email", email).
                                    add("tarjBanc", tarjBanc).
                                    add("numEntradas", String.valueOf(numEntradas)).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, entrada);

            return JsonToClientCompraDtoConversor.toClientCompraDto(entrada.getEntity().getContent()).getCompraId();

        }catch (InputValidationException | ClientPartidoYaComenzadoException | ClientInvalidNumTarjetaException | ClientNoQuedanEntradasException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientCompraDto> findEntradas(String email) throws InputValidationException{
        try{
            if(email==null){
                throw new InputValidationException("El email no puede ser nulo");
            }

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "compras?email="
                    + URLEncoder.encode(email, "UTF-8")).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientCompraDtoConversor.toClientComprasDtos(response.getEntity().getContent());

        }catch (InputValidationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }



    @Override
    public void recogerEntradas(Long compraId, String tarjBanc) throws InputValidationException, ClientAlreadyCollectException {

        try{
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "compras/" + compraId
             + "/").
                    bodyForm(
                            Form.form().add("tarjBanc", tarjBanc).
                                    build()).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        }catch (InputValidationException | ClientAlreadyCollectException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientPartidoDto partido) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientPartidoDtoConversor.toObjectNode(partido));
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {

            int statusCode = response.getCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
