package com.tde.apiVallartaRecargas.persistence.repository;


import com.tde.apiVallartaRecargas.persistence.entity.RecargaHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RecargaHotelRepository extends JpaRepository<RecargaHotel, Long> {

    /**
     * Buscar por folio (único).
     * Útil para soporte, conciliación o idempotencia.
     */
    Optional<RecargaHotel> findByFolio(String folio);

    /**
     * Listar recargas por hotel en un rango de fechas (para reportes).
     */
    @Query("SELECT r " +
           "FROM RecargaHotel r " +
           "WHERE r.hotel.id = :idHotel " +
           "  AND r.fechaRecarga >= :fechaInicio " +
           "  AND r.fechaRecarga < :fechaFin " +
           "ORDER BY r.fechaRecarga DESC")
    List<RecargaHotel> findByHotelAndRangoFechas(@Param("idHotel") Long idHotel,
                                                 @Param("fechaInicio") Date fechaInicio,
                                                 @Param("fechaFin") Date fechaFin);

    /**
     * Listar recargas por hotel y estatus en un rango de fechas.
     */
    @Query("SELECT r " +
           "FROM RecargaHotel r " +
           "WHERE r.hotel.id = :idHotel " +
           "  AND r.estatus = :estatus " +
           "  AND r.fechaRecarga >= :fechaInicio " +
           "  AND r.fechaRecarga < :fechaFin " +
           "ORDER BY r.fechaRecarga DESC")
    List<RecargaHotel> findByHotelEstatusAndRangoFechas(@Param("idHotel") Long idHotel,
                                                        @Param("estatus") String estatus,
                                                        @Param("fechaInicio") Date fechaInicio,
                                                        @Param("fechaFin") Date fechaFin);

    /**
     * Consultas rápidas por SAM (útil cuando hay incidencias con un lector/SAM).
     */
    @Query("SELECT r " +
           "FROM RecargaHotel r " +
           "WHERE r.hotel.id = :idHotel " +
           "  AND r.uidSamRecarga = :uidSam " +
           "  AND r.fechaRecarga >= :fechaInicio " +
           "  AND r.fechaRecarga < :fechaFin " +
           "ORDER BY r.fechaRecarga DESC")
    List<RecargaHotel> findByHotelAndSamUidAndRangoFechas(@Param("idHotel") Long idHotel,
                                                          @Param("uidSam") String uidSam,
                                                          @Param("fechaInicio") Date fechaInicio,
                                                          @Param("fechaFin") Date fechaFin);

    /**
     * Historial por monedero (útil para auditoría de saldo).
     */
    @Query("SELECT r " +
           "FROM RecargaHotel r " +
           "WHERE r.wallet.id = :idWallet " +
           "  AND r.fechaRecarga >= :fechaInicio " +
           "  AND r.fechaRecarga < :fechaFin " +
           "ORDER BY r.fechaRecarga DESC")
    List<RecargaHotel> findByWalletAndRangoFechas(@Param("idWallet") Long idWallet,
                                                  @Param("fechaInicio") Date fechaInicio,
                                                  @Param("fechaFin") Date fechaFin);

    /**
     * Si necesitas obtener la última recarga por hotel (por ejemplo, para vista rápida).
     */
    @Query("SELECT r " +
           "FROM RecargaHotel r " +
           "WHERE r.hotel.id = :idHotel " +
           "ORDER BY r.fechaRecarga DESC")
    List<RecargaHotel> findUltimasPorHotel(@Param("idHotel") Long idHotel);
}
