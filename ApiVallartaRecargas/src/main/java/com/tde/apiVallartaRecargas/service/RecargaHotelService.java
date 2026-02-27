package com.tde.apiVallartaRecargas.service;


import com.tde.apiVallartaRecargas.dto.RecargaRequestHotelDTO;
import com.tde.apiVallartaRecargas.dto.RecargaResponseDTO;
import com.tde.apiVallartaRecargas.persistence.entity.*;
import com.tde.apiVallartaRecargas.persistence.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
	
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class RecargaHotelService {

    private static final BigDecimal MONTO_MAXIMO = new BigDecimal("500");

    private final WalletRepository walletRepository;
    private final SamRepository samRepository;
    private final RecargaHotelRepository recargaHotelRepository;
    private final WalletMovementRepository walletMovementRepository;
    private final HotelFolioTicketSeqRepository hotelFolioTicketSeqRepository;
    private final CardAssignmentRepository cardAssignmentRepository;

    public RecargaHotelService(WalletRepository walletRepository,
                          SamRepository samRepository,
                          RecargaHotelRepository recargaHotelRepository,
                          WalletMovementRepository walletMovementRepository,
                          HotelFolioTicketSeqRepository hotelFolioTicketSeqRepository,
                          CardAssignmentRepository cardAssignmentRepository) {
                          
        this.walletRepository = walletRepository;
        this.samRepository = samRepository;
        this.recargaHotelRepository = recargaHotelRepository;
        this.walletMovementRepository = walletMovementRepository;
        this.hotelFolioTicketSeqRepository = hotelFolioTicketSeqRepository;
        this.cardAssignmentRepository = cardAssignmentRepository;
    }

    /**
     * Flujo principal de recarga.
     * Debe ejecutarse SIEMPRE dentro de una transacción.
     */
    @Transactional
    public RecargaResponseDTO realizarRecarga(RecargaRequestHotelDTO request,
                                              Long idUser,
                                              //Long idHotel,
                                              String ipOrigen) {

        // ======================================================
        // 1. Validaciones básicas de entrada
        // ======================================================
    	//obtenemos el id del reques
    	//para validar si este hotel y la tarjeta estan asignadas a dicho hotel sino 
    	//se sale del fljo y se crea la respuesta de con el mensaje.
    	Long idHotel= request.getIdHotel();
    	
        long tarjetaAsignada = cardAssignmentRepository.countTarjetaAsignadaHotel(
                request.getStrCredencial().trim(),
                request.getIdHotel()
        );
        if (tarjetaAsignada <= 0) {
            return rechazo("La tarjeta no pertenece al hotel indicado");
        }
    	
        if (request.getDecRecarga() == null ||
            request.getDecRecarga().compareTo(BigDecimal.ZERO) <= 0) {

            return rechazo("Monto inválido");
        }

        if (request.getDecRecarga().compareTo(MONTO_MAXIMO) > 0) {
            return rechazo("El monto máximo permitido es 500");
        }

        // ======================================================
        // 2. Validar SAM activa y perteneciente al hotel
        // ======================================================

        Sam sam = samRepository
                .findActivaByUidAndHotel(
                        request.getStrUIDsamRecarga(),
                        idHotel,
                        1
                )
                .orElse(null);

        if (sam == null) {
            return rechazo("SAM inválida, inactiva o no pertenece al hotel");
        }

        // ======================================================
        // 3. Obtener wallet ACTIVO y vigente con bloqueo
        // ======================================================

        Date ahora = new Date();

        Wallet wallet = walletRepository
                .findActivoVigenteByHotelForUpdate(idHotel, 1, ahora)
                .orElse(null);

        if (wallet == null) {
            return rechazo("No existe un monedero activo y vigente para el hotel");
        }

        // ======================================================
        // 4. Validar saldo suficiente
        // ======================================================

        if (wallet.getSaldoActual().compareTo(request.getDecRecarga()) < 0) {
            return rechazo("Saldo insuficiente en el monedero");
        }

        // ======================================================
        // 5. Crear recarga (estatus PENDIENTE)
        // ======================================================

        String folioTicket = generarFolioTicket(
                idHotel,
                wallet.getHotel().getCodigo()   // ej. "H12"
        );
       
        String folio = generarFolio();

        RecargaHotel recarga = RecargaHotel.builder()
                .idUser(idUser)
                .hotel(wallet.getHotel())
                .wallet(wallet)
                .uidSamActiva(request.getStrUIDsamActiva())
                .uidSamRecarga(request.getStrUIDsamRecarga())
                .tipoTarjeta(request.getIntTipoTarjeta())
                .subtipoTarjeta(request.getIntSubTipoTarjeta())
                .credencial(request.getStrCredencial())
                .saldoInicial(request.getDecSaldoInicial())
                .montoRecarga(request.getDecRecarga())
                .saldoFinal(request.getDecSaldoFinal())
                .fechaRecarga(ahora)
                .fechaExpiracion(parseFechaExpiracion(request.getFechaExpiracion()))
                .folio(folio)
                .folioTicket(folioTicket)
                .estatus("PENDIENTE")
                .build();

        recargaHotelRepository.save(recarga);

        // ======================================================
        // 6. Aplicar débito al wallet
        // ======================================================

        BigDecimal saldoAntes = wallet.getSaldoActual();
        BigDecimal saldoDespues = saldoAntes.subtract(request.getDecRecarga());

        WalletMovement movement = WalletMovement.builder()
                .wallet(wallet)
                .tipo("DEBITO")
                .monto(request.getDecRecarga())
                .saldoAntes(saldoAntes)
                .saldoDespues(saldoDespues)
                .refTipo("RECARGA")
                .refId(recarga.getId())
                .descripcion("Recarga aplicada folio " + folio)
                .createdBy(idUser)
                .ipOrigen(ipOrigen)
                .build();

        walletMovementRepository.save(movement);

        // ======================================================
        // 7. Actualizar saldo del wallet
        // ======================================================

        wallet.setSaldoActual(saldoDespues);
        walletRepository.save(wallet);

        // ======================================================
        // 8. Finalizar recarga
        // ======================================================

        recarga.setSaldoFinal(request.getDecSaldoInicial()); // saldo de tarjeta si aplica
        recarga.setEstatus("APLICADA");
        recargaHotelRepository.save(recarga);

        // ======================================================
        // 9. Respuesta
        // ======================================================

        return RecargaResponseDTO.builder()
                .folio(folioTicket)
                .estatus("APLICADA")
                .mensaje("Recarga aplicada correctamente")
                .montoRecarga(request.getDecRecarga())
                .saldoWalletRestante(saldoDespues)
                .fechaRecarga(ahora.toString())
                .build();
    }

    // ==========================================================
    // Métodos auxiliares
    // ==========================================================

    private RecargaResponseDTO rechazo(String mensaje) {
        return RecargaResponseDTO.builder()
                .estatus("RECHAZADA")
                .mensaje(mensaje)
                .build();
    }
    private String generarFolio() {
        return "RCH-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
    
    @Transactional
    protected String generarFolioTicket(Long idHotel, String codigoHotel) {

        java.util.Date now = new java.util.Date();
        java.sql.Date fechaHoy = new java.sql.Date(now.getTime());

        // 1. Bloquear (o crear) el registro de secuencia del día
        HotelFolioTicketSeq seq = hotelFolioTicketSeqRepository
                .findForUpdate(idHotel, fechaHoy)
                .orElseGet(() -> {
                    HotelFolioTicketSeq nuevo = new HotelFolioTicketSeq();
                    nuevo.setIdHotel(idHotel);
                    nuevo.setFecha(fechaHoy);
                    nuevo.setLastSeq(0);
                    nuevo.setUpdatedAt(now);
                    return nuevo;
                });

        // 2. Incrementar consecutivo
        int siguiente = seq.getLastSeq() + 1;
        seq.setLastSeq(siguiente);
        seq.setUpdatedAt(now);

        hotelFolioTicketSeqRepository.save(seq);

        // 3. Formatear componentes
        String fechaYYMMDD = new java.text.SimpleDateFormat("yyMMdd").format(now);
        String consecutivo = String.format("%05d", siguiente);

        // 4. Construir folio
        return codigoHotel + "-" + fechaYYMMDD + "-" + consecutivo;
    }


    private Date parseFechaExpiracion(String fecha) {
        // Implementación simple por ahora.
        // Luego puedes parsear formato ISO o calcular expiración automática.
        return new Date();
    }
}
