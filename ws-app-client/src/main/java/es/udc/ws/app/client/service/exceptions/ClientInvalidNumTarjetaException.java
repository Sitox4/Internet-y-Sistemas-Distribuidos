package es.udc.ws.app.client.service.exceptions;

public class ClientInvalidNumTarjetaException extends Exception{
    private Long compraId;

    public ClientInvalidNumTarjetaException(Long compraId) {
        super("Sale with id=\"" + compraId + "\n cannot be collected because the number card is invalid");
        this.compraId = compraId;
    }

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }
}
