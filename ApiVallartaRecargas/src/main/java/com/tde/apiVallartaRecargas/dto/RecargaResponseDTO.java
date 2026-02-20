package com.tde.apiVallartaRecargas.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecargaResponseDTO {

    /**
     * Folio interno de la recarga.
     * Sirve para soporte, auditoría e idempotencia.
     */
    private String folio;

    /**
     * Estatus final de la recarga.
     * PENDIENTE / APLICADA / RECHAZADA
     */
    private String estatus;

    /**
     * Mensaje legible para la app cliente.
     * Ejemplos:
     *  - "Recarga aplicada correctamente"
     *  - "Saldo insuficiente en monedero"
     *  - "SAM inválida o inactiva"
     */
    private String mensaje;

    /**
     * Monto recargado.
     */
    private BigDecimal montoRecarga;

    /**
     * Saldo restante del monedero después de la operación.
     * Útil para mostrar al usuario del hotel.
     */
    private BigDecimal saldoWalletRestante;

    /**
     * Fecha/hora en que se procesó la recarga (backend).
     */
    private String fechaRecarga;
}
