package es.udc.ws.app.client.service.exceptions;

public class ClientAlreadyCollectException extends Exception {
    private Long compraId;

    public ClientAlreadyCollectException(Long compraId) {
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
