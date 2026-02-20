package com.tde.apiVallartaRecargas.persistence.repository;

import com.tde.apiVallartaRecargas.persistence.entity.WalletMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface WalletMovementRepository extends JpaRepository<WalletMovement, Long> {

    /**
     * Historial de movimientos de un monedero (kardex) en un rango de fechas.
     */
    @Query("SELECT m " +
           "FROM WalletMovement m " +
           "WHERE m.wallet.id = :idWallet " +
           "  AND m.createdAt >= :fechaInicio " +
           "  AND m.createdAt < :fechaFin " +
           "ORDER BY m.createdAt DESC")
    List<WalletMovement> findByWalletAndRangoFechas(@Param("idWallet") Long idWallet,
                                                    @Param("fechaInicio") Date fechaInicio,
                                                    @Param("fechaFin") Date fechaFin);

    /**
     * Movimientos por referencia (ej: para encontrar el movimiento generado por una recarga específica).
     */
    @Query("SELECT m " +
           "FROM WalletMovement m " +
           "WHERE m.refTipo = :refTipo " +
           "  AND m.refId = :refId " +
           "ORDER BY m.createdAt DESC")
    List<WalletMovement> findByReferencia(@Param("refTipo") String refTipo,
                                         @Param("refId") Long refId);

    /**
     * Últimos movimientos de un wallet (para pantalla o auditoría rápida).
     * Nota: para limitar cantidad, ideal usar Pageable. Aquí se deja como List.
     */
    @Query("SELECT m " +
           "FROM WalletMovement m " +
           "WHERE m.wallet.id = :idWallet " +
           "ORDER BY m.createdAt DESC")
    List<WalletMovement> findUltimosByWallet(@Param("idWallet") Long idWallet);

    /**
     * Suma de débitos y créditos por rango (útil para reportes/conciliación).
     * Retorna: [0]=totalCreditos, [1]=totalDebitos
     */
    @Query("SELECT " +
           "  COALESCE(SUM(CASE WHEN m.tipo = 'CREDITO' THEN m.monto ELSE 0 END), 0), " +
           "  COALESCE(SUM(CASE WHEN m.tipo = 'DEBITO'  THEN m.monto ELSE 0 END), 0) " +
           "FROM WalletMovement m " +
           "WHERE m.wallet.id = :idWallet " +
           "  AND m.createdAt >= :fechaInicio " +
           "  AND m.createdAt < :fechaFin")
    Object[] sumarCreditosDebitos(@Param("idWallet") Long idWallet,
                                  @Param("fechaInicio") Date fechaInicio,
                                  @Param("fechaFin") Date fechaFin);
}
