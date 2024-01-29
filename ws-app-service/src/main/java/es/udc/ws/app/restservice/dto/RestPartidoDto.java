package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestPartidoDto {
    private Long partidoId;
    private String nombreVisitante;
    private LocalDateTime celebracion;
    private float precio;
    private int maxEntradas;
    private int vendidas;

    public RestPartidoDto(){}

    public RestPartidoDto(Long partidoId, String nombreVisitante, LocalDateTime celebracion, float precio, int maxEntradas, int vendidas) {
        this.partidoId = partidoId;
        this.nombreVisitante = nombreVisitante;
        this.celebracion = celebracion;
        this.precio = precio;
        this.maxEntradas = maxEntradas;
        this.vendidas = vendidas;
    }

    public RestPartidoDto(Long partidoId, String nombreVisitante, LocalDateTime celebracion, float precio, int maxEntradas) {
        this.partidoId = partidoId;
        this.nombreVisitante = nombreVisitante;
        this.celebracion = celebracion;
        this.precio = precio;
        this.maxEntradas = maxEntradas;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }

    public String getNombreVisitante() {
        return nombreVisitante;
    }

    public void setNombreVisitante(String nombreVisitante) {
        this.nombreVisitante = nombreVisitante;
    }

    public LocalDateTime getCelebracion() {
        return celebracion;
    }

    public void setCelebracion(LocalDateTime celebracion) {
        this.celebracion = celebracion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getMaxEntradas() {
        return maxEntradas;
    }

    public void setMaxEntradas(int maxEntradas) {
        this.maxEntradas = maxEntradas;
    }

    public int getVendidas() {
        return vendidas;
    }

    public void setVendidas(int vendidas) {
        this.vendidas = vendidas;
    }

    @Override
    public String toString() {
        return "PartidoDto{" +
                "partidoId=" + partidoId +
                ", nombreVisitante='" + nombreVisitante + '\'' +
                ", celebracion=" + celebracion +
                ", precio=" + precio +
                ", maxEntradas=" + maxEntradas +
                ", vendidas=" + vendidas +
                '}';
    }
}
