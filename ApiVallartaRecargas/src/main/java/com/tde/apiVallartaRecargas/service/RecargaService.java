package com.tde.apiVallartaRecargas.service;

import com.tde.apiVallartaRecargas.dto.RecargaRequest;
import com.tde.apiVallartaRecargas.entity.Recarga;
import com.tde.apiVallartaRecargas.repository.RecargaRepository;
import com.tde.apiVallartaRecargas.util.SecurityUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                List<Recarga> recargas = requests.stream()
                        .map(this::mapToEntity)
                        .collect(Collectors.toList());

                return recargaRepository.saveAll(recargas);
        }

        private Recarga mapToEntity(RecargaRequest request) {
        	Long idUsuario = SecurityUtils.getCurrentUserId();
            if (idUsuario == null) {
                throw new IllegalStateException("No se pudo obtener el usuario autenticado.");
            }
                Recarga recarga = new Recarga();
               // recarga.setStrProgramador(request.getStrProgramador());
                
                recarga.setStrUIDsamRecarga(request.getStrUIDsamRecarga());
                recarga.setStrUIDsamActiva(request.getStrUIDsamActiva());
                recarga.setIntTipoTarjeta(request.getIntTipoTarjeta());
                recarga.setIntSubTipoTarjeta(request.getIntSubTipoTarjeta());
                recarga.setStrCredencial(request.getStrCredencial());
                recarga.setIntSaldoInicial(request.getDecSaldoInicial());
                recarga.setIntSaldoFinal(request.getDecSaldoFinal());
                recarga.setIntRecarga(request.getDecRecarga());
                recarga.setFechaRecarga(defaultDate(request.fechaRecargaAsDate()));
                recarga.setFechaExpiracion(defaultDate(request.fechaExpiracionAsDate()));
                recarga.setIdUsuario(idUsuario);
                return recarga;
        }

        private LocalDateTime defaultDate(LocalDateTime value) {
                return value != null ? value : LocalDateTime.now();
        }
}