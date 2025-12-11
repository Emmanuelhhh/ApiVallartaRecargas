package com.tde.apiVallartaRecargas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecargaRequest {

  //  private String strProgramador;
	private String strUIDsamActiva;
	private String strUIDsamRecarga;
    private Short intTipoTarjeta;
    private Short intSubTipoTarjeta;
    private String strCredencial;
    private BigDecimal decSaldoInicial;
    private BigDecimal decSaldoFinal;
    private BigDecimal decRecarga;
    private String fechaRecarga;
    private String fechaExpiracion;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime fechaRecargaAsDate() {
        return parseDate(fechaRecarga);
    }

    public LocalDateTime fechaExpiracionAsDate() {
        return parseDate(fechaExpiracion);
    }

    private LocalDateTime parseDate(String value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "Formato de fecha inválido, use yyyy-MM-dd HH:mm:ss", ex);
        }
    }
}
