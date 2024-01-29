package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.compra.Compra;

import java.util.ArrayList;
import java.util.List;

public class CompraToRestCompraDtoConversor {
    public static List<RestCompraDto> toRestCompraDtos(List<Compra> compras){
        List<RestCompraDto> compraDtos  = new ArrayList<>(compras.size());
        for(int i = 0; i < compras.size(); i++){
            Compra compra = compras.get(i);
            compraDtos.add(toRestCompraDto(compra));
        }
        return compraDtos;
    }

    public static RestCompraDto toRestCompraDto(Compra compra){
        return new RestCompraDto(compra.getCompraId(),compra.getPartidoId(), compra.getEmail(), compra.getNumTarjeta(),
                compra.getNumEntradas(), compra.isRecogidas());
    }

    public static Compra toCompra(RestCompraDto compra){
        return new Compra(compra.getCompraId(), compra.getPartidoId(),compra.getNumTarjeta(), compra.getNumEntradas(), compra.isRecogidas());
    }
}
