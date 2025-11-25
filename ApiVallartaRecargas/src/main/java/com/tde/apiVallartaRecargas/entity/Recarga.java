package com.tde.apiVallartaRecargas.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "tblrecarga")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recarga {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "strprogramador", nullable = false, length = 32)
        private String strProgramador;

        @Column(name = "inttipotarjeta")
        private Short intTipoTarjeta;

        @Column(name = "strcredencial", nullable = false, length = 100)
        private String strCredencial;

        @Column(name = "intsaldoinicial", nullable = false)
        private Integer intSaldoInicial;

        @Column(name = "intsaldofinal", nullable = false)
        private Integer intSaldoFinal;

        @Column(name = "intrecarga", nullable = false)
        private Integer intRecarga;

        @Column(name = "fecharecarga", nullable = false)
        private LocalDateTime fechaRecarga;

        @Column(name = "fechaexpiracion", nullable = false)
        private LocalDateTime fechaExpiracion;

}