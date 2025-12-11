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

import java.math.BigDecimal;
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

        @Column(name = "struidsamactiva", nullable = false, length = 100)
        private String strUIDsamActiva;
        
        @Column(name = "struidsamrecarga",  nullable = false, length = 100)
        private String strUIDsamRecarga;
        
        @Column(name = "intsubtipotarjeta", nullable = false )
        private Short intSubTipoTarjeta;
        
        @Column(name = "inttipotarjeta", nullable = false)
        private Short intTipoTarjeta;

        @Column(name = "strcredencial", nullable = false, length = 100)
        private String strCredencial;

        @Column(name = "decsaldoinicial", nullable = false)
        private BigDecimal intSaldoInicial;

        @Column(name = "decsaldofinal", nullable = false)
        private BigDecimal intSaldoFinal;

        @Column(name = "decrecarga", nullable = false)
        private BigDecimal intRecarga;

        @Column(name = "fecharecarga", nullable = false)
        private LocalDateTime fechaRecarga;

        @Column(name = "fechaexpiracion", nullable = false)
        private LocalDateTime fechaExpiracion;
        
        @Column(name = "id_user", nullable = false)
        private Long idUsuario;
        

}