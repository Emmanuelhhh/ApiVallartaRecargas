package com.tde.apiVallartaRecargas.service;

import com.tde.apiVallartaRecargas.dto.RecargaRequest;
import com.tde.apiVallartaRecargas.entity.Recarga;
import com.tde.apiVallartaRecargas.repository.RecargaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecargaService {

        private final RecargaRepository recargaRepository;

        public RecargaService(RecargaRepository recargaRepository) {
                this.recargaRepository = recargaRepository;
        }

        @Transactional
        public Recarga crearRecarga(RecargaRequest request) {
                Recarga recarga = mapToEntity(request);
                return recargaRepository.save(recarga);
        }

        @Transactional
        public List<Recarga> crearRecargas(List<RecargaRequest> requests) {
                if (CollectionUtils.isEmpty(requests)) {
                        throw new IllegalArgumentException("La lista de recargas está vacía");
                }
                List<Recarga> recargas = requests.stream().map(this::mapToEntity).toList();
                return recargaRepository.saveAll(recargas);
        }

        private Recarga mapToEntity(RecargaRequest request) {
                Recarga recarga = new Recarga();
                recarga.setStrProgramador(request.strProgramador());
                recarga.setIntTipoTarjeta(request.intTipoTarjeta());
                recarga.setStrCredencial(request.strCredencial());
                recarga.setIntSaldoInicial(request.intSaldoInicial());
                recarga.setIntSaldoFinal(request.intSaldoFinal());
                recarga.setIntRecarga(request.intRecarga());
                recarga.setFechaRecarga(defaultDate(request.fechaRecargaAsDate()));
                recarga.setFechaExpiracion(defaultDate(request.fechaExpiracionAsDate()));
                recarga.setBErrorRecarga(false);
                return recarga;
        }

        private LocalDateTime defaultDate(LocalDateTime value) {
                return value != null ? value : LocalDateTime.now();
        }
}