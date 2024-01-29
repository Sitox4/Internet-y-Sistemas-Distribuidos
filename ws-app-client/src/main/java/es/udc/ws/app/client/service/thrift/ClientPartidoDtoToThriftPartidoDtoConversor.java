package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientPartidoDtoToThriftPartidoDtoConversor {
    public static ThriftPartidoDto toThriftPartidoDto(ClientPartidoDto clientPartidoDto){
        Long partidoId = clientPartidoDto.getPartidoId();
        return new ThriftPartidoDto(
                partidoId == null ? -1 : partidoId,
                clientPartidoDto.getNombreVisitante(),
                clientPartidoDto.getCelebracion(),
                clientPartidoDto.getPrecio(),
                clientPartidoDto.getMaxEntradas(),
                clientPartidoDto.getVendidas());
    }

    public static List<ClientPartidoDto> toClientPartidoDtos(List<ThriftPartidoDto> partidos){
        List<ClientPartidoDto> clientPartidoDtos = new ArrayList<>(partidos.size());

        for(ThriftPartidoDto partido : partidos){
            clientPartidoDtos.add(toClientPartidoDto(partido));
        }
        return clientPartidoDtos;
    }


    private static ClientPartidoDto toClientPartidoDto(ThriftPartidoDto partido){
        return new ClientPartidoDto(
                partido.getPartidoId(),
                partido.getNombreVisitante(),
                LocalDateTime.parse(partido.getCelebracion()),
                partido.getPrecio(),
                partido.getMaxEntradas(),
                partido.getVendidas());
    }
}
