package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.PartidosService.PartidoServiceFactory;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.restservice.dto.PartidoToRestPartidoDtoConversor;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.app.restservice.json.JsonToRestPartidoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class PartidoServlet extends RestHttpServletTemplate {
    //Funcionalidad 1: Añadir partido
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {
        ServletUtils.checkEmptyPath(req);
        RestPartidoDto partidoDto = JsonToRestPartidoDtoConversor.toRestPartidoDto(req.getInputStream());
        Partido partido = PartidoToRestPartidoDtoConversor.toPartido(partidoDto);
        partido = PartidoServiceFactory.getService().addPartido(partido);
        partidoDto = PartidoToRestPartidoDtoConversor.toRestPartidoDto(partido);
        String partidoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + partido.getPartidoId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", partidoURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), headers);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InstanceNotFoundException, InputValidationException {
        //ServletUtils.checkEmptyPath(req);
        String aux = ServletUtils.normalizePath(req.getPathInfo());
        if(aux == null || aux.equals("/")){
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //String inicio = req.getParameter("fechainicio");
            //String fin = req.getParameter("fechafin");
            LocalDateTime fechainicio = LocalDateTime.parse(req.getParameter("fechainicio"));
            LocalDateTime fechafin = LocalDateTime.parse(req.getParameter("fechafin"));
            List<Partido> partidos = PartidoServiceFactory.getService().findPartidos(fechainicio,fechafin);
            List<RestPartidoDto> partidoDtos = PartidoToRestPartidoDtoConversor.toRestPartidoDtos(partidos);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestPartidoDtoConversor.toArrayNode(partidoDtos), null);
        }else{
            Long partidoId = ServletUtils.getIdFromPath(req, "partidoId");
            Partido partido;
            partido = PartidoServiceFactory.getService().findById(partidoId);
            RestPartidoDto partidoDto = PartidoToRestPartidoDtoConversor.toRestPartidoDto(partido);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), null);
        }
    }
}



