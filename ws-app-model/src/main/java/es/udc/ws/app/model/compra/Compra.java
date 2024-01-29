package es.udc.ws.app.model.compra;

import java.time.LocalDateTime;
import java.util.Objects;

public class Compra {
    private Long compraId;
    private Long partidoId;
    private String email;
    private String numTarjeta;
    private int numEntradas;
    private LocalDateTime fechahoraCompra;

    private boolean recogidas;

    public Compra(Long compraId, Long partidoId, String email, String numTarjeta, int numEntradas, LocalDateTime fechahoraCompra) {
        this.compraId = compraId;
        this.partidoId = partidoId;
        this.email = email;
        this.numTarjeta = numTarjeta;
        this.numEntradas = numEntradas;
        this.fechahoraCompra = fechahoraCompra;
    }
    public Compra( Long partidoId, String email, String numTarjeta, int numEntradas, LocalDateTime fechahoraCompra) {
        this.partidoId = partidoId;
        this.email = email;
        this.numTarjeta = numTarjeta;
        this.numEntradas = numEntradas;
        this.fechahoraCompra = fechahoraCompra;
    }

    public Compra(Long compraId, Long partidoId, String email, String numTarjeta, int numEntradas, LocalDateTime fechahoraCompra, boolean recogidas) {
        this.compraId = compraId;
        this.partidoId = partidoId;
        this.email = email;
        this.numTarjeta = numTarjeta;
        this.numEntradas = numEntradas;
        this.fechahoraCompra = fechahoraCompra;
        this.recogidas = recogidas;
    }

    public Compra(Long compraId, Long partidoId, String numTarjeta, int numEntradas, boolean recogidas) {
        this.compraId = compraId;
        this.partidoId = partidoId;
        this.numTarjeta = numTarjeta;
        this.numEntradas = numEntradas;
        this.recogidas = recogidas;
    }

    public boolean isRecogidas() {
        return recogidas;
    }

    public void setRecogidas(boolean recogidas) {
        this.recogidas = recogidas;
    }

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public int getNumEntradas() {
        return numEntradas;
    }

    public void setNumEntradas(int numEntradas) {
        this.numEntradas = numEntradas;
    }

    public LocalDateTime getFechahoraCompra() {
        return fechahoraCompra;
    }

    public void setFechahoraCompra(LocalDateTime fechahoraCompra) {
        this.fechahoraCompra = fechahoraCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return numEntradas == compra.numEntradas && recogidas == compra.recogidas && Objects.equals(compraId, compra.compraId) && Objects.equals(partidoId, compra.partidoId) && Objects.equals(email, compra.email) && Objects.equals(numTarjeta, compra.numTarjeta) && Objects.equals(fechahoraCompra, compra.fechahoraCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compraId, partidoId, email, numTarjeta, numEntradas, fechahoraCompra, recogidas);
    }
}
