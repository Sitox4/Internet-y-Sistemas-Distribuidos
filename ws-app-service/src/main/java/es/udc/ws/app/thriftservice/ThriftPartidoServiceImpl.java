package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.PartidosService.PartidoServiceFactory;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.thrift.ThriftInputValidationException;
import es.udc.ws.app.thrift.ThriftPartidoDto;
import es.udc.ws.app.thrift.ThriftPartidoService;
import es.udc.ws.util.exceptions.InputValidationException;
import org.apache.thrift.TException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftPartidoServiceImpl implements ThriftPartidoService.Iface {
    @Override
    public ThriftPartidoDto addPartido(ThriftPartidoDto partidoDto) throws ThriftInputValidationException, TException {
        Partido partido = PartidoToThriftPartidoDtoConversor.toPartido(partidoDto);
        try{
            Partido addedPartido = PartidoServiceFactory.getService().addPartido(partido);
            return PartidoToThriftPartidoDtoConversor.toThriftPartidoDto(addedPartido);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }


    @Override
    public List<ThriftPartidoDto> findPartidos(String fechaincio, String fechafin) throws TException {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaincio);
            LocalDateTime fin = LocalDateTime.parse(fechafin);
            List<Partido> partidos = PartidoServiceFactory.getService().findPartidos(inicio, fin);
            return PartidoToThriftPartidoDtoConversor.toThriftPartidoDtos(partidos);

        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }
}
