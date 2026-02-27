package com.tde.apiVallartaRecargas.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tde.apiVallartaRecargas.persistence.entity.Wallet;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Obtiene el wallet ACTIVO de un hotel (sin bloqueo).
     * Útil para consultas de lectura (mostrar saldo, vigencia, etc.).
     */
    Optional<Wallet> findFirstByHotel_IdAndStatus(Long idHotel, String status);

    /**
     * Obtiene el wallet ACTIVO y vigente por hotel (sin bloqueo).
     * Si quieres estrictamente validar vigencia al consultar.
     */
    @Query("SELECT w " +
           "FROM Wallet w " +
           "WHERE w.hotel.id = :idHotel " +
           "  AND w.status = :status " +
           "  AND :fechaActual BETWEEN w.vigenciaInicio AND w.vigenciaFin")
    Optional<Wallet> findActivoVigenteByHotel(@Param("idHotel") Long idHotel,
                                              @Param("status") String status,
                                              @Param("fechaActual") Date fechaActual);

    /**
     * Obtiene el wallet ACTIVO del hotel aplicando bloqueo pesimista de escritura.
     * Recomendado para el flujo de recarga: evita que dos recargas descuenten el mismo saldo en paralelo.
     *
     * Debe ejecutarse dentro de una transacción (@Transactional).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w " +
           "FROM Wallet w " +
           "WHERE w.hotel.id = :idHotel " +
           "  AND w.status = :status")
    Optional<Wallet> findActivoByHotelForUpdate(@Param("idHotel") Long idHotel,
                                                @Param("status") Integer status);

    /**
     * Variante con bloqueo + vigencia.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w " +
           "FROM Wallet w " +
           "WHERE w.hotel.id = :idHotel " +
           "  AND w.status = :status " +
           "  AND :fechaActual BETWEEN w.vigenciaInicio AND w.vigenciaFin")
    Optional<Wallet> findActivoVigenteByHotelForUpdate(@Param("idHotel") Long idHotel,
                                                       @Param("status") Integer status,
                                                       @Param("fechaActual") Date fechaActual);
}
