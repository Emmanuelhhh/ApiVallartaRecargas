package com.tde.apiVallartaRecargas.persistence.repository;


import com.tde.apiVallartaRecargas.persistence.entity.HotelFolioTicketSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.sql.Date;
import java.util.Optional;

public interface HotelFolioTicketSeqRepository
        extends JpaRepository<HotelFolioTicketSeq, HotelFolioTicketSeq.PK> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s " +
           "FROM HotelFolioTicketSeq s " +
           "WHERE s.idHotel = :idHotel " +
           "  AND s.fecha = :fecha")
    Optional<HotelFolioTicketSeq> findForUpdate(@Param("idHotel") Long idHotel,
                                                @Param("fecha") Date fecha);
}
