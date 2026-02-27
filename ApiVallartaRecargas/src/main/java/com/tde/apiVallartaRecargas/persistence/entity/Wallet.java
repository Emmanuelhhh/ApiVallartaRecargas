package com.tde.apiVallartaRecargas.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(
        name = "ope_wallet",
        uniqueConstraints = {
                @UniqueConstraint(name = "UX_wallet_codigo", columnNames = {"codigo"})
        },
        indexes = {
                @Index(name = "IX_wallet_hotel_estatus_vigencia", columnList = "id_hotel, status, vigencia_inicio, vigencia_fin")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Hotel dueño/asignación del monedero.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_hotel",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_wallet_hotel")
    )
    private Hotel hotel;

    /**
     * Identificador operativo único del monedero.
     */
    @Column(name = "codigo", length = 30, nullable = false)
    private String codigo;

    /**
     * ACTIVO / INACTIVO
     * (Luego lo convertimos a Enum si lo decides)
     */
    @Column(name = "status", length = 20, nullable = false)
    private Integer status;

    /**
     * Saldo vigente. Recomendación: actualizarlo siempre en la misma transacción
     * en la que insertas wallet_movement.
     */
    @Column(name = "saldo_actual", precision = 14, scale = 2, nullable = false)
    private BigDecimal saldoActual;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "vigencia_inicio", nullable = false)
    private Date vigenciaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "vigencia_fin", nullable = false)
    private Date vigenciaFin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Opcional: usuario admin que creó el monedero.
     * Nota: está mapeado como ID para no amarrarnos a la entidad de usuarios existente.
     * Si quieres mapearlo a OpeUser como @ManyToOne, lo ajustamos.
     */
    @Column(name = "created_by")
    private Long createdBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
