package com.tde.apiVallartaRecargas.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecargaRequestHotelDTO {

    /**
     * UID de la SAM activa en el lector.
     * Ejemplo: 0AFD4B3E44F610F1
     */
    private String strUIDsamActiva;

    /**
     * UID de la SAM utilizada para la recarga
     * (en muchos casos será la misma que la activa).
     */
    private String strUIDsamRecarga;

    /**
     * Tipo de tarjeta (definido por tu negocio).
     */
    private Short intTipoTarjeta;

    /**
     * Subtipo de tarjeta.
     */
    private Short intSubTipoTarjeta;

    /**
     * Identificador/credencial de la tarjeta sin contacto.
     */
    private String strCredencial;

  
    private BigDecimal decSaldoInicial;
    
    private BigDecimal decSaldoFinal;

    /**
     * Monto a recargar.
     * Reglas:
     *  - > 0
     *  - <= 500
     *  - <= saldo disponible del wallet
     */
    private BigDecimal decRecarga;
    
    private String fechaRecarga;

    /**
     * Fecha de expiración de la recarga en la tarjeta.
     * La app cliente la envía o el backend la calcula (según tu lógica).
     */
    private String fechaExpiracion;
}
