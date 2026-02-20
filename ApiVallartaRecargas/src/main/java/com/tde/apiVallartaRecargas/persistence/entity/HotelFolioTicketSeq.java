package com.tde.apiVallartaRecargas.persistence.entity;



import java.sql.Date;

import javax.persistence.*;

import lombok.*;


@Entity
@Table(name = "hotel_folio_ticket_seq")
@IdClass(HotelFolioTicketSeq.PK.class)
@Getter 
@Setter
public class HotelFolioTicketSeq {

    @Id
    @Column(name = "id_hotel")
    private Long idHotel;

    @Id
    @Column(name = "fecha")
    private Date fecha; // java.sql.Date (solo día)

    @Column(name = "last_seq", nullable = false)
    private Integer lastSeq;

    @Column(name = "updated_at", nullable = false)
    private java.util.Date updatedAt;

    @Getter @Setter
    public static class PK implements java.io.Serializable {
        private Long idHotel;
        private Date fecha;
    }
}
