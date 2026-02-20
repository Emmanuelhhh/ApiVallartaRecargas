package com.tde.apiVallartaRecargas.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cat_hotel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Código operativo único del hotel.
     */
    @Column(name = "codigo", length = 20, nullable = false)
    private String codigo;
    /* =========================
       Datos generales
       ========================= */

    @Column(name = "nombre", length = 120, nullable = false)
    private String nombre;

    @Column(name = "contacto_nombre", length = 100)
    private String contactoNombre;

    @Column(name = "contacto_telefono", length = 20)
    private String contactoTelefono;

    @Column(name = "contacto_email", length = 120)
    private String contactoEmail;

    @Column(name = "estado", length = 100, nullable = false)
    private String estado;

    @Column(name = "municipio", length = 100, nullable = false)
    private String municipio;

    @Column(name = "direccion", length = 100, nullable = false)
    private String direccion;

    @Column(name = "cp", length = 15, nullable = false)
    private String cp;

    /* =========================
       Datos fiscales
       ========================= */

    @Column(name = "razon_social", length = 180)
    private String razonSocial;

    @Column(name = "rfc", length = 20)
    private String rfc;

    /* =========================
       Configuración de negocio
       ========================= */

    /**
     * Porcentaje de beneficio/comisión aplicable al hotel.
     * Rango esperado: 0 – 100
     */
    @Column(name = "porcentaje", nullable = false)
    private Integer porcentaje;

    /**
     * Estado operativo del hotel
     * 1 = ACTIVO
     * 0 = INACTIVO
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /* =========================
       Auditoría
       ========================= */

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
