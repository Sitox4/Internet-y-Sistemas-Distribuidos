package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.ClientPartidoServiceFactory;
import es.udc.ws.app.client.service.dto.ClientCompraDto;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidNumTarjetaException;
import es.udc.ws.app.client.service.exceptions.ClientNoQuedanEntradasException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoYaComenzadoException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {
        // TODO
        if(args.length == 0){
            printUsageAndExit();
        }
        ClientPartidoService clientPartidoService =
                ClientPartidoServiceFactory.getService();
        //Funcionalidad 1
        if("-a".equalsIgnoreCase(args[0])){
           validateArgs(args, 5, new int[]{});

            // [add]

            try{

                ClientPartidoDto partido = new ClientPartidoDto(null, args[1], LocalDateTime.parse(args[2]), Float.parseFloat(args[3]),
                        Integer.parseInt(args[4]));
                Long partidoId = clientPartidoService.addPartido(partido);

                System.out.println("Partido " + partidoId + " añadido correctamente");

            } catch (InputValidationException e) {
                throw new RuntimeException(e);
            }
            //Funcionalidad 2
        }else if("-fp".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[]{});

            // [findPartidos]

            try {
                List<ClientPartidoDto> partidos = clientPartidoService.findPartidos( LocalDateTime.now() ,  LocalDateTime.parse(args[1]));

                if (partidos.isEmpty()) {
                    System.out.println("No se han encontrado resultados.");
                }else {
                    for (ClientPartidoDto partido : partidos) {

                        System.out.println("Id: " + partido.getPartidoId() + "\nRival: " + partido.getNombreVisitante() +
                                "\nFecha de celebración: " + partido.getCelebracion() + "\nPrecio: " + partido.getPrecio() +
                                "\nEntradas: " + partido.getMaxEntradas() +
                                "\nVendidas: " + partido.getVendidas() + "\n");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            //Funcionalidad 3 find by id
        }else if("-fi".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[]{});
            // [findById]
            try {
                ClientPartidoDto partido = ClientPartidoServiceFactory.getService().findById(Long.parseLong(args[1]));
                System.out.println("Encontrado el partido con id: " + args[1]);
                System.out.println("Id: " + partido.getPartidoId() +
                        ", nombreVisitante: " + partido.getNombreVisitante() +
                        ", celebracion: " + partido.getCelebracion() +
                        ", precio: " + partido.getPrecio() +
                        ", maxEntradas: " + partido.getMaxEntradas() +
                        ", vendidas: " + partido.getVendidas());
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException(e);
            }
            //Funcionalidad 4 comprar entradas
        }else if("-b".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[]{});

            // [buy]

            Long compraId;
            try {
                int entradas = Integer.parseInt(args[4]);
                compraId = clientPartidoService.comprarEntradas(Long.parseLong(args[1]),
                        args[2], args[3], entradas);

                System.out.println("Compra realizada con éxito, identificador de la compra: " + compraId);

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            //Funcionalidad 5 find entradas
        }else if ("-fc".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[]{});
            // [findCompra]
            try{
                List<ClientCompraDto> compras = ClientPartidoServiceFactory.getService().findEntradas(args[1]);

                if (compras.isEmpty()) {
                    System.out.println("No se han encontrado resultados.");
                }else {
                    for (ClientCompraDto compra : compras) {

                        System.out.println("Id compra: " + compra.getCompraId() + "\nId partido: " + compra.getPartidoId() +
                                "\nEmail: " + compra.getEmail() + "\nTarjeta: " + compra.getNumTarjeta() +
                                "\nEntradas: " + compra.getNumEntradas() +
                                "\nRecogidas: " + compra.isRecogidas() + "\n");
                    }
                }
            }catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            //Funcionalidad 6 Recoger entradas
        }else if("-g".equalsIgnoreCase(args[0])){
            validateArgs(args,3,new int[]{1});
            // [get]
            try{

                ClientPartidoServiceFactory.getService().recogerEntradas(Long.parseLong(args[1]), args[2]);
                System.out.println("Las entradas de la compra " + args[1] + " han sido recogidas correctamente");
            } catch (ClientAlreadyCollectException | InputValidationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add]          PartidoServiceClient -a  <nombreVisitante> <celebracion> <precio> <maxEntradas> <vendidas>\n" +
                "    [findPartidos] PartidoServiceClient -fp <celebracion>\n" +
                "    [findById]     PartidoServiceClient -fi <partidoId> <>\n" +
                "    [buy]          PartidoServiceClient -b  <partidoId> <email> <numTarjeta> <numEntradas>\n" +
                "    [findCompra]   PartidoServiceClient -fc <> \n" +
                "    [get]          PartidoServiceClient -g  <compraId>\n");
    }
}