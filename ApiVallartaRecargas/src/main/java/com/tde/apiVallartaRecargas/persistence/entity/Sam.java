package com.tde.apiVallartaRecargas.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "cat_sam",
        uniqueConstraints = {
                @UniqueConstraint(name = "UX_sam_uid", columnNames = {"uid"})
        },
        indexes = {
                @Index(name = "IX_sam_hotel_status", columnList = "id_hotel, status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * UID de la SAM enviado por la app cliente (ej: 0AFD4B3E44F610F1).
     */
    @Column(name = "uid", length = 16, nullable = false)
    private String uid;

    /**
     * Opcionales (operación/soporte).
     */
    @Column(name = "serie", length = 40)
    private String serie;

    @Column(name = "alias", length = 60)
    private String alias;

    /**
     * Hotel al que está asignada la SAM.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_hotel",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_sam_hotel")
    )
    private Hotel hotel;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_alta", nullable = false, updatable = false)
    private Date fechaAlta;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_baja")
    private Date fechaBaja;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Opcional: usuario admin que dio de alta la SAM.
     * Lo dejamos como ID para no depender de la entidad ope_user todavía.
     */
    @Column(name = "created_by")
    private Long createdBy;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        if (this.fechaAlta == null) {
            this.fechaAlta = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
