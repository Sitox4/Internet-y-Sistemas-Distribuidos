package es.udc.ws.app.model.partido;

import java.time.LocalDateTime;

public class Partido {
    private Long partidoId;
    private String nombreVisitante;
    private LocalDateTime celebracion;
    private float precio;
    private int maxEntradas;
    private int vendidas;
    private LocalDateTime alta;

    public Partido(Long partidoId, String nombreVisitante, LocalDateTime celebracion, float precio, int maxEntradas, int vendidas, LocalDateTime alta) {
        this.partidoId = partidoId;
        this.nombreVisitante = nombreVisitante;
        this.celebracion = (celebracion != null) ? celebracion.withNano(0) : null;
        this.precio = precio;
        this.maxEntradas = maxEntradas;
        this.vendidas = vendidas;
        this.alta = (alta != null) ? alta.withNano(0) : null;
    }

    public Partido(String nombreVisitante, LocalDateTime celebracion, float precio, int maxEntradas, int vendidas, LocalDateTime alta) {
        this.nombreVisitante = nombreVisitante;
        this.celebracion = (celebracion != null) ? celebracion.withNano(0) : null;
        this.precio = precio;
        this.maxEntradas = maxEntradas;
        this.vendidas = vendidas;
        this.alta = (alta != null) ? alta.withNano(0) : null;
    }

    public Partido(Long partidoId, String nombreVisitante, LocalDateTime celebracion, float precio, int maxEntradas, int vendidas) {
        this.partidoId = partidoId;
        this.nombreVisitante = nombreVisitante;
        this.celebracion = (celebracion != null) ? celebracion.withNano(0) : null;
        this.precio = precio;
        this.maxEntradas = maxEntradas;
        this.vendidas = vendidas;
    }

    public Partido(Long partidoId, String nombreVisitante, LocalDateTime celebracion, float precio, int maxEntradas) {
        this.partidoId = partidoId;
        this.nombreVisitante = nombreVisitante;
        this.celebracion = (celebracion != null) ? celebracion.withNano(0) : null;
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

    public LocalDateTime getAlta() {
        return alta;
    }

    public void setAlta(LocalDateTime alta) {
        this.alta = alta;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Partido other = (Partido) obj;
        if (alta == null) {
            if (other.alta != null)
                return false;
        } else if (!alta.equals(other.alta))
            return false;
        if (celebracion == null) {
            if (other.celebracion != null)
                return false;
        } else if (!celebracion.equals(other.celebracion))
            return false;
        if (nombreVisitante == null) {
            if (other.nombreVisitante != null)
                return false;
        } else if (!nombreVisitante.equals(other.nombreVisitante))
            return false;
        if (partidoId == null) {
            if (other.partidoId != null)
                return false;
        } else if (!partidoId.equals(other.partidoId))
            return false;
        if (Float.floatToIntBits(precio) != Float.floatToIntBits(other.precio))
            return false;
        if (maxEntradas != other.maxEntradas)
            return false;
        if (vendidas != other.vendidas)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alta == null) ? 0 : alta.hashCode());
        result = prime * result + ((celebracion == null) ? 0 : celebracion.hashCode());
        result = prime * result + ((nombreVisitante == null) ? 0 : nombreVisitante.hashCode());
        result = prime * result + ((partidoId == null) ? 0 : partidoId.hashCode());
        result = prime * result + Float.floatToIntBits(precio);
        result = prime * result + maxEntradas;
        result = prime * result + vendidas;
        return result;
    }
}
