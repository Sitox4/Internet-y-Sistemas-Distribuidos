package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestCompraDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonToRestCompraDtoConversor {
    public static ObjectNode toObjectNode(RestCompraDto compra){
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

    public static ArrayNode toArrayNode(List<RestCompraDto> compras){
        ArrayNode compraNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < compras.size(); i++){
            RestCompraDto compraDto = compras.get(i);
            ObjectNode compraObject = toObjectNode(compraDto);
            compraNode.add(compraObject);
        }
        return  compraNode;
    }

    public static RestCompraDto toRestCompraDto(InputStream jsonCompra) throws ParsingException {
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
                boolean recogidas = compraObject.get("recogidas").booleanValue();//Boolean.parseBoolean(compraObject.get("recogidas").textValue().trim());

                return new RestCompraDto(compraId, partidoId, email, numTarjeta, numEntradas, recogidas);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
