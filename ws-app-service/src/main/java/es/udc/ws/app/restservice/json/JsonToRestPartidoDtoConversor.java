package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class JsonToRestPartidoDtoConversor {
    public static ObjectNode toObjectNode(RestPartidoDto partido){

        ObjectNode partidoObject = JsonNodeFactory.instance.objectNode();

        if(partido.getPartidoId() != null){
            partidoObject.put("partidoId", partido.getPartidoId());
        }
        partidoObject.put("nombreVisitante", partido.getNombreVisitante()).
                put("celebracion", partido.getCelebracion().toString()).
                put("precio", partido.getPrecio()).
                put("maxEntradas", partido.getMaxEntradas()).
                put("vendidas", partido.getVendidas());

        return partidoObject;
    }

    public static ArrayNode toArrayNode(List<RestPartidoDto> partidos){
        ArrayNode partidosNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < partidos.size(); i++){
            RestPartidoDto partidoDto = partidos.get(i);
            ObjectNode partidoObject = toObjectNode(partidoDto);
            partidosNode.add(partidoObject);
        }
        return partidosNode;
    }

    public static RestPartidoDto toRestPartidoDto(InputStream jsonPartido) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartido);
            if(rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            }else {
                ObjectNode partidoObject = (ObjectNode) rootNode;
                JsonNode partidoIdNode = partidoObject.get("partidoId");
                Long partidoId = (partidoIdNode != null) ? partidoIdNode.longValue() : null;

                String nombreVisitante =partidoObject.get("nombreVisitante").textValue().trim();
                LocalDateTime celebracion = LocalDateTime.parse((partidoObject.get("celebracion").textValue()));
                float precio = partidoObject.get("precio").floatValue();
                int maxEntradas = partidoObject.get("maxEntradas").intValue();
                //int vendidas = partidoObject.get("vendidas").intValue();

                return new RestPartidoDto(partidoId,nombreVisitante,celebracion,precio,maxEntradas);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
