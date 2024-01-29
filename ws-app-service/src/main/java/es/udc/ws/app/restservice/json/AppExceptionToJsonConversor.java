package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.PartidosService.Exceptions.AlreadyCollectException;
import es.udc.ws.app.model.PartidosService.Exceptions.InvalidNumTarjetaException;
import es.udc.ws.app.model.PartidosService.Exceptions.NoQuedanEntradasException;
import es.udc.ws.app.model.PartidosService.Exceptions.PartidoYaComenzadoException;

public class AppExceptionToJsonConversor {
    public static ObjectNode toAlreadyCollectException(AlreadyCollectException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "AlreadyCollect");
        exceptionObject.put("compraId", (ex.getCompraId() != null) ? ex.getCompraId() : null);
        return exceptionObject;
    }

    public static ObjectNode toInvalidNumTarjetaException(InvalidNumTarjetaException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "InvalidNumTarjeta");
        exceptionObject.put("compraId", (ex.getCompraId() != null) ? ex.getCompraId() : null);
        return exceptionObject;
    }

    public static ObjectNode toNoQuedanEntradasException(NoQuedanEntradasException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "NoQuedanEntradas");
        exceptionObject.put("partidoId", (ex.getPartidoId() != null) ? ex.getPartidoId() : null);
        return exceptionObject;
    }

    public static ObjectNode toPartidoYaComenzadoException(PartidoYaComenzadoException ex){
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "PartidoYaComenzado");
        exceptionObject.put("partidoId", (ex.getPartidoId() != null) ? ex.getPartidoId() : null);
        return exceptionObject;
    }
}
