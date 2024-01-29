package es.udc.ws.app.client.service.exceptions;

public class ClientNoQuedanEntradasException extends Exception{
    private Long partidoId;

    public ClientNoQuedanEntradasException(Long partidoId) {
        super("Match with id=\"" + partidoId + "\n has no more tickets.");
        this.partidoId = partidoId;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }
}
