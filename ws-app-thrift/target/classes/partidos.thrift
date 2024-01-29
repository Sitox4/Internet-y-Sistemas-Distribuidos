namespace java es.udc.ws.app.thrift

struct ThriftPartidoDto {
    1: i64 partidoId
    2: string nombreVisitante
    3: string celebracion
    4: i16 precio
    5: i16 maxEntradas
    6: i16 vendidas
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

service ThriftPartidoService {

    ThriftPartidoDto addPartido(1: ThriftPartidoDto partidoDto) throws (1: ThriftInputValidationException e)
    list<ThriftPartidoDto> findPartidos(1: string fechaincio, 2: string fechafin)
}
