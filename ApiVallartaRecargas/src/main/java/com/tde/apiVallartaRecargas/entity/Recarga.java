package com.tde.apiVallartaRecargas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "TblRecarga")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recarga {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "Id")
        private Long id;

        @Column(name = "StrProgramador", nullable = false, length = 32)
        private String strProgramador;

        @Column(name = "IntTipoTarjeta")
        private Short intTipoTarjeta;

        @Column(name = "StrCredencial", nullable = false, length = 100)
        private String strCredencial;

        @Column(name = "IntSaldoInicial", nullable = false)
        private Integer intSaldoInicial;

        @Column(name = "IntSaldoFinal", nullable = false)
        private Integer intSaldoFinal;

        @Column(name = "IntRecarga", nullable = false)
        private Integer intRecarga;

        @Column(name = "FechaRecarga", nullable = false)
        private LocalDateTime fechaRecarga;

        @Column(name = "FechaExpiracion", nullable = false)
        private LocalDateTime fechaExpiracion;

        @Column(name = "BErrorRecarga", nullable = false)
        private boolean bErrorRecarga;
}