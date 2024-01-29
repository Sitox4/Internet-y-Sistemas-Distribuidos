package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientCompraDtoConversor {
    public static ObjectNode toObjectNode(ClientCompraDto compra){
        ObjectNode compraNode = JsonNodeFactory.instance.objectNode();
        if(compra.getCompraId() != null){
            compraNode.put("compraId", compra.getCompraId());
        }
        compraNode.put("partidoId",compra.getPartidoId()).
                put("email", compra.getEmail()).
                put("tarjBanc", compra.getNumTarjeta()).
                put("numEntradas", compra.getNumEntradas()).
                put("recogidas", compra.isRecogidas());

        return compraNode;
    }

    public static ClientCompraDto toClientCompraDto(InputStream jsonCompra) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonCompra);
            if(rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode compraObject = (ObjectNode) rootNode;
                JsonNode compraIdNode = compraObject.get("compraId");
                Long compraId = (compraIdNode != null) ? compraIdNode.longValue() : null;
                Long partidoId = compraObject.get("partidoId").longValue();
                String email = compraObject.get("email").textValue().trim();
                String numTarjeta = compraObject.get("tarjBanc").textValue().trim();
                int numEntradas = compraObject.get("numEntradas").asInt();
                boolean recogidas = compraObject.get("recogidas").booleanValue();
                return new ClientCompraDto(compraId, partidoId, email, numTarjeta, numEntradas, recogidas);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<ClientCompraDto> toClientComprasDtos(InputStream jsonResponses) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonResponses);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode comprasArray = (ArrayNode) rootNode;
                List<ClientCompraDto> compraDtos = new ArrayList<>(comprasArray.size());
                for (JsonNode compraNode : comprasArray) {
                    compraDtos.add(toClientCompraDto(compraNode));
                }

                return compraDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientCompraDto toClientCompraDto(JsonNode compraNode) throws ParsingException {
        if(compraNode.getNodeType() != JsonNodeType.OBJECT){
            throw new ParsingException("Unrecognized JSON(object expected)");
        }else{
            ObjectNode compraObject = (ObjectNode) compraNode;
            JsonNode compraIdNode = compraObject.get("compraId");
            Long compraId = (compraIdNode != null) ? compraIdNode.longValue() : null;
            Long partidoId = compraObject.get("partidoId").longValue();
            String email = compraObject.get("email").textValue().trim();
            String numTarjeta = compraObject.get("tarjBanc").textValue().trim();
            int numEntradas = compraObject.get("numEntradas").intValue();
            boolean recogidas =compraObject.get("recogidas").booleanValue();

            return new ClientCompraDto(compraId,partidoId,email,numTarjeta,numEntradas,recogidas);
        }
    }
}
