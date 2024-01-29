package es.udc.ws.app.model.PartidosService.Exceptions;

public class InvalidNumTarjetaException extends Exception{
    private Long compraId;

    public InvalidNumTarjetaException(Long compraId) {
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
