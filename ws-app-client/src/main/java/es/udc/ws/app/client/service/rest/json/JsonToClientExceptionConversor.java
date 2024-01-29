package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidNumTarjetaException;
import es.udc.ws.app.client.service.exceptions.ClientNoQuedanEntradasException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoYaComenzadoException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;

public class JsonToClientExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode){
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException{
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if(rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            }else{
                String errortype = rootNode.get("errorType").textValue();
                switch (errortype) {
                    case "Already Collect" -> {
                        return toAlreadyCollectException(rootNode);
                    }
                    case "Invalid Num Tarjeta" -> {
                        return toInvalidNumTarjetaException(rootNode);
                    }
                    case "No quedan entradas" -> {
                        return toNoQuedanEntradasException(rootNode);
                    }
                }
                throw new ParsingException("Unrecognized error type: " + errortype);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Exception fromGoneErrorCode(InputStream ex)throws ParsingException{
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("PartidoYaComenzado")) {
                    return toPartidoYaComenzadoException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientAlreadyCollectException toAlreadyCollectException(JsonNode rootNode) {
        Long compraId = rootNode.get("compraId").longValue();
        return new ClientAlreadyCollectException(compraId);
    }

    private static ClientInvalidNumTarjetaException toInvalidNumTarjetaException(JsonNode rootNode) {
        Long compraId = rootNode.get("compraId").longValue();
        return new ClientInvalidNumTarjetaException(compraId);
    }

    private static ClientNoQuedanEntradasException toNoQuedanEntradasException(JsonNode rootNode) {
        Long partidoId = rootNode.get("partidoId").longValue();
        return new ClientNoQuedanEntradasException(partidoId);
    }
    private static ClientPartidoYaComenzadoException toPartidoYaComenzadoException(JsonNode rootNode) {
        Long partidoId = rootNode.get("partidoId").longValue();
        return new ClientPartidoYaComenzadoException(partidoId);
    }
}
