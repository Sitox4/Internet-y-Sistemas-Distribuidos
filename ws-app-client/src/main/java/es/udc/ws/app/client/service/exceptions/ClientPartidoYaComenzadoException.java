package es.udc.ws.app.client.service.exceptions;

public class ClientPartidoYaComenzadoException extends Exception{

    private Long partidoId;

    public ClientPartidoYaComenzadoException(Long partidoId) {
        super("Match with id=\"" + partidoId + "\n has already started.");
        this.partidoId = partidoId;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }
}
