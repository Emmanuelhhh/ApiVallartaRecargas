package com.tde.apiVallartaRecargas.persistence.projection;

import java.math.BigDecimal;

public interface HotelResumenProjection {
    Long getId();
    String getCodigo();
    String getNombre();
    String getEstado();
    String getMunicipio();
    BigDecimal getSaldoActual();
}
