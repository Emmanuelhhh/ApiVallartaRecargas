package com.tde.apiVallartaRecargas.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResumenDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String estado;
    private String municipio;

    // puede venir null si el hotel aún no tiene wallet activo
    private BigDecimal saldoActual;
}
