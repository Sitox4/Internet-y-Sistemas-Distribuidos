package es.udc.ws.app.restservice.dto;
import es.udc.ws.app.model.partido.Partido;

import java.util.ArrayList;
import java.util.List;

public class PartidoToRestPartidoDtoConversor {

    public static List<RestPartidoDto> toRestPartidoDtos(List<Partido> partidos){
        List<RestPartidoDto> partidoDtos = new ArrayList<>(partidos.size());
        for (int i = 0; i < partidos.size(); i++){
            Partido partido = partidos.get(i);
            partidoDtos.add(toRestPartidoDto(partido));
        }
        return partidoDtos;
    }

    public static RestPartidoDto toRestPartidoDto(Partido partido){
        return new RestPartidoDto(partido.getPartidoId(), partido.getNombreVisitante(), partido.getCelebracion(),
                partido.getPrecio(),partido.getMaxEntradas(), partido.getVendidas());
    }

    public  static Partido toPartido(RestPartidoDto partido) {
        return new Partido(partido.getPartidoId(), partido.getNombreVisitante(),partido.getCelebracion(),partido.getPrecio(),
                partido.getMaxEntradas(), partido.getVendidas());
    }
}
