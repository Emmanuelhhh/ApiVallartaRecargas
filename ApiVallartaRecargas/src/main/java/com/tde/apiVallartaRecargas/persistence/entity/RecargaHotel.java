package com.tde.apiVallartaRecargas.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(
        name = "recarga_hotel",
        uniqueConstraints = {
                @UniqueConstraint(name = "UX_recarga_hotel_folio", columnNames = {"folio"})
        },
        indexes = {
                @Index(name = "IX_recarga_hotel_hotel_fecha", columnList = "id_hotel, fecha_recarga"),
                @Index(name = "IX_recarga_hotel_wallet_fecha", columnList = "id_wallet, fecha_recarga"),
                @Index(name = "IX_recarga_hotel_user_fecha", columnList = "id_user, fecha_recarga"),
                @Index(name = "IX_recarga_hotel_estatus_fecha", columnList = "estatus, fecha_recarga"),
                @Index(name = "IX_recarga_hotel_sam_uid_fecha", columnList = "uid_sam_recarga, fecha_recarga")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecargaHotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /* =========================================================
       Contexto operativo
       ========================================================= */

    /**
     * Usuario que ejecuta la recarga.
     * Nota: Lo dejamos como ID por compatibilidad con ope_user,
     * a menos que ya tengas la entidad OpeUser mapeada.
     */
    @Column(name = "id_user", nullable = false)
    private Long idUser;

    /**
     * Hotel al que pertenece la recarga.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_hotel",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_recarga_hotel_hotel")
    )
    private Hotel hotel;

    /**
     * Monedero utilizado para financiar la recarga.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_wallet",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_recarga_hotel_wallet")
    )
    private Wallet wallet;

    /* =========================================================
       Datos de la operación (enviados por app / calculados)
       ========================================================= */

    @Column(name = "uid_sam_activa", length = 16, nullable = false)
    private String uidSamActiva;

    @Column(name = "uid_sam_recarga", length = 16, nullable = false)
    private String uidSamRecarga;

    @Column(name = "tipo_tarjeta", nullable = false)
    private Short tipoTarjeta;

    @Column(name = "subtipo_tarjeta", nullable = false)
    private Short subtipoTarjeta;

    @Column(name = "credencial", length = 100, nullable = false)
    private String credencial;

    @Column(name = "saldo_inicial", precision = 10, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "monto_recarga", precision = 10, scale = 2, nullable = false)
    private BigDecimal montoRecarga;

    @Column(name = "saldo_final", precision = 10, scale = 2)
    private BigDecimal saldoFinal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_recarga", nullable = false)
    private Date fechaRecarga;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_expiracion", nullable = false)
    private Date fechaExpiracion;

    /* =========================================================
       Trazabilidad / estado transaccional
       ========================================================= */

    @Column(name = "folio", length = 40, nullable = false)
    private String folio;
    
    @Column(name = "folio_ticket", length = 40, nullable = false)
    private String folioTicket;

    @Column(name = "referencia", length = 60)
    private String referencia;

    /**
     * PENDIENTE / APLICADA / RECHAZADA / REVERSADA
     */
    @Column(name = "estatus", length = 20, nullable = false)
    private String estatus;

    @Column(name = "motivo_rechazo", length = 200)
    private String motivoRechazo;

    /* =========================================================
       Auditoría
       ========================================================= */

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;

        // Si tu backend no setea fecha_recarga explícitamente, puedes usar esto:
        if (this.fechaRecarga == null) {
            this.fechaRecarga = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
