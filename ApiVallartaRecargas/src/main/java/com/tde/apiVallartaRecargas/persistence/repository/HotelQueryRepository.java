package com.tde.apiVallartaRecargas.persistence.repository;

import com.tde.apiVallartaRecargas.persistence.entity.Hotel;
import com.tde.apiVallartaRecargas.persistence.projection.HotelResumenProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelQueryRepository extends JpaRepository<Hotel, Long> {

    @Query(value =
            "SELECT " +
            "  h.id            AS id, " +
            "  h.codigo        AS codigo, " +
            "  h.nombre        AS nombre, " +
            "  h.estado        AS estado, " +
            "  h.municipio     AS municipio, " +
            "  w.saldo_actual  AS saldoActual " +
            "FROM dbo.ope_user_hotel uh " +
            "INNER JOIN dbo.cat_hotel h ON h.id = uh.id_hotel " +
            "LEFT JOIN dbo.ope_wallet w ON w.id_hotel = h.id AND w.status = 1 " +
            "WHERE uh.id_user = :idUser " +
            "  AND uh.status = 1 " +
            "  AND h.status = 1 " +
            "ORDER BY h.codigo",
            nativeQuery = true)
    List<HotelResumenProjection> listarHotelesPermitidos(@Param("idUser") Long idUser);
}
