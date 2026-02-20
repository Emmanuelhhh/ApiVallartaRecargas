package com.tde.apiVallartaRecargas.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(
        name = "ope_wallet_movement",
        indexes = {
                @Index(name = "IX_wallet_movement_wallet_fecha", columnList = "id_wallet, created_at"),
                @Index(name = "IX_wallet_movement_ref", columnList = "ref_tipo, ref_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Monedero al que pertenece el movimiento.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_wallet",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_wallet_movement_wallet")
    )
    private Wallet wallet;

    /**
     * CREDITO / DEBITO / AJUSTE / REVERSO
     */
    @Column(name = "tipo", length = 20, nullable = false)
    private String tipo;

    /**
     * Monto positivo siempre (la dirección la define "tipo").
     */
    @Column(name = "monto", precision = 14, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "saldo_antes", precision = 14, scale = 2, nullable = false)
    private BigDecimal saldoAntes;

    @Column(name = "saldo_despues", precision = 14, scale = 2, nullable = false)
    private BigDecimal saldoDespues;

    /**
     * RECARGA / PAGO / AJUSTE_MANUAL / REVERSO
     */
    @Column(name = "ref_tipo", length = 30, nullable = false)
    private String refTipo;

    /**
     * ID del objeto origen (ej: recarga_hotel.id).
     * Se mantiene genérico para permitir varios tipos de referencia.
     */
    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    /**
     * Usuario que originó el movimiento (opcional).
     * Se deja como ID por compatibilidad con ope_user.
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * IP de origen (opcional). VARCHAR(45) para soportar IPv4/IPv6.
     */
    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
