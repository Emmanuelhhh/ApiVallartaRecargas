package com.tde.apiVallartaRecargas.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record RecargaRequest(
                String strProgramador,
                Short intTipoTarjeta,
                String strCredencial,
                Integer intSaldoInicial,
                Integer intSaldoFinal,
                Integer intRecarga,
                String fechaRecarga,
                String fechaExpiracion) {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
                        throw new IllegalArgumentException("Formato de fecha inválido, use yyyy-MM-dd HH:mm:ss", ex);
                }
        }
}