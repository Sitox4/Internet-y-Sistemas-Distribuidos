package es.udc.ws.app.model.PartidosService.Exceptions;

public class PartidoYaComenzadoException extends Exception{
    private Long partidoId;

    public PartidoYaComenzadoException(Long partidoId) {
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