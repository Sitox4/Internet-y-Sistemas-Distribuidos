package es.udc.ws.app.model.PartidosService.Exceptions;

public class AlreadyCollectException extends Exception{
    private Long compraId;

    public AlreadyCollectException(Long compraId) {
        super("Sale with id=\"" + compraId + "\n has already been collected");
        this.compraId = compraId;
    }

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }
}
