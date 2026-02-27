package com.tde.apiVallartaRecargas.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.tde.apiVallartaRecargas.persistence.entity.Hotel;

public interface CardAssignmentRepository extends Repository<Hotel, Long> {

    @Query(value =
            "SELECT COUNT(1) " +
            "FROM ope_inventory_card ic " +
            "INNER JOIN ope_card_assignment ca ON ic.pcn = ca.pcn " +
            "WHERE ic.str_UIDTarjeta = :uidTarjeta " +
            "  AND ca.id_hotel = :idHotel",
            nativeQuery = true)
    long countTarjetaAsignadaHotel(@Param("uidTarjeta") String uidTarjeta,
                                   @Param("idHotel") Long idHotel);
}
