package com.tde.apiVallartaRecargas.persistence.repository;


import com.tde.apiVallartaRecargas.persistence.entity.Sam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SamRepository extends JpaRepository<Sam, Long> {

    /**
     * Busca una SAM por UID.
     * Útil cuando la app cliente envía el UID directamente.
     */
    Optional<Sam> findByUid(String uid);

    /**
     * Busca una SAM por UID y hotel.
     * Regla clave: una SAM solo puede operar dentro de su hotel asignado.
     */
    @Query("SELECT s " +
           "FROM Sam s " +
           "WHERE s.uid = :uid " +
           "  AND s.hotel.id = :idHotel")
    Optional<Sam> findByUidAndHotel(@Param("uid") String uid,
                                    @Param("idHotel") Long idHotel);

    /**
     * Busca una SAM ACTIVA por UID y hotel.
     * Este es el método recomendado para el flujo de recarga.
     */
    @Query("SELECT s " +
           "FROM Sam s " +
           "WHERE s.uid = :uid " +
           "  AND s.hotel.id = :idHotel " +
           "  AND s.status = :status")
    Optional<Sam> findActivaByUidAndHotel(@Param("uid") String uid,
                                          @Param("idHotel") Long idHotel,
                                          @Param("status") Integer status);
}
