package es.udc.ws.app.model.PartidosService.Exceptions;

public class NoQuedanEntradasException extends Exception{
    private Long partidoId;

    public NoQuedanEntradasException(Long partidoId) {
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