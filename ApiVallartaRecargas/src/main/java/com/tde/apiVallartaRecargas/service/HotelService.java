package com.tde.apiVallartaRecargas.service;

import com.tde.apiVallartaRecargas.dto.HotelResumenDTO;
import com.tde.apiVallartaRecargas.persistence.projection.HotelResumenProjection;
import com.tde.apiVallartaRecargas.persistence.repository.HotelQueryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelService {

    private final HotelQueryRepository hotelQueryRepository;

    public HotelService(HotelQueryRepository hotelQueryRepository) {
        this.hotelQueryRepository = hotelQueryRepository;
    }

    public List<HotelResumenDTO> listarHotelesPermitidos(Long userId) {
        List<HotelResumenProjection> rows = hotelQueryRepository.listarHotelesPermitidos(userId);

        List<HotelResumenDTO> result = new ArrayList<>();
        for (HotelResumenProjection r : rows) {
            result.add(HotelResumenDTO.builder()
                    .id(r.getId())
                    .codigo(r.getCodigo())
                    .nombre(r.getNombre())
                    .estado(r.getEstado())
                    .municipio(r.getMunicipio())
                    .saldoActual(r.getSaldoActual())
                    .build());
        }
        return result;
    }
}
