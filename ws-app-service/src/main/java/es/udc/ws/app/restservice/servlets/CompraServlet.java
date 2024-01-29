package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.PartidosService.Exceptions.AlreadyCollectException;
import es.udc.ws.app.model.PartidosService.Exceptions.InvalidNumTarjetaException;
import es.udc.ws.app.model.PartidosService.Exceptions.NoQuedanEntradasException;
import es.udc.ws.app.model.PartidosService.Exceptions.PartidoYaComenzadoException;
import es.udc.ws.app.model.PartidosService.PartidoServiceFactory;
import es.udc.ws.app.model.compra.Compra;
import es.udc.ws.app.restservice.dto.CompraToRestCompraDtoConversor;
import es.udc.ws.app.restservice.dto.RestCompraDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestCompraDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("serial")
public class CompraServlet extends RestHttpServletTemplate {
    @Override //Comprar entradas y recoger entradas
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException, InstanceNotFoundException {
        String aux = req.getPathInfo();
        //Buy -> /partido
        if (aux == null || aux.equals("/")) {
            Long compraId = ServletUtils.getMandatoryParameterAsLong(req, "partidoId");
            String email = ServletUtils.getMandatoryParameter(req, "email");
            String tarjBanc = ServletUtils.getMandatoryParameter(req, "tarjBanc");
            int numEntradas =Integer.parseInt (ServletUtils.getMandatoryParameter(req, "numEntradas"));
            Compra compra= null;

            try {
                compra = PartidoServiceFactory.getService().buyEntradas(compraId, email, tarjBanc, numEntradas);
                assert compra != null;
                RestCompraDto compraDto = CompraToRestCompraDtoConversor.toRestCompraDto(compra);
                String compraURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + compra.getCompraId().toString();

                Map<String, String> headers = new HashMap<>(1);
                headers.put("Location", compraURL);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                        JsonToRestCompraDtoConversor.toObjectNode(compraDto), headers);
            } catch (PartidoYaComenzadoException pyc) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toPartidoYaComenzadoException(pyc), null);
            } catch (NoQuedanEntradasException nqe) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toNoQuedanEntradasException(nqe), null);
            }
        }
        else{
            Long compraId = ServletUtils.getIdFromPath(req,"compra");
            String tarjBanc = ServletUtils.getMandatoryParameter(req, "tarjBanc");
            try{
                PartidoServiceFactory.getService().recogidaCompra(compraId,tarjBanc);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
            } catch (InvalidNumTarjetaException | AlreadyCollectException e) {
                throw new RuntimeException(e);
            }


        }

    }


    @Override //Encontrar entradas de usuario
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        String email = ServletUtils.getMandatoryParameter(req, "email");

        List<Compra> compras = PartidoServiceFactory.getService().findCompra(email);

        List<RestCompraDto> comprasDto = CompraToRestCompraDtoConversor.toRestCompraDtos(compras);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestCompraDtoConversor.toArrayNode(comprasDto), null);
    }

}
