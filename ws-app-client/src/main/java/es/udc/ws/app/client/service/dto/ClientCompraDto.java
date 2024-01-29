package es.udc.ws.app.client.service.dto;

public class ClientCompraDto {
    private Long compraId;
    private Long partidoId;
    private String email;
    private String numTarjeta;
    private int numEntradas;
    private boolean recogidas;

    public ClientCompraDto(Long compraId, Long partidoId, String email, String numTarjeta, int numEntradas, boolean recogidas) {
        this.compraId = compraId;
        this.partidoId = partidoId;
        this.email = email;
        this.numTarjeta = numTarjeta;
        this.numEntradas = numEntradas;
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

    public boolean isRecogidas() {
        return recogidas;
    }

    public void setRecogidas(boolean recogidas) {
        this.recogidas = recogidas;
    }

    @Override
    public String toString() {
        return "ClientCompraDto{" +
                "compraId=" + compraId +
                ", partidoId=" + partidoId +
                ", email='" + email + '\'' +
                ", numTarjeta='" + numTarjeta + '\'' +
                ", numEntradas=" + numEntradas +
                ", recogidas=" + recogidas +
                '}';
    }
}
