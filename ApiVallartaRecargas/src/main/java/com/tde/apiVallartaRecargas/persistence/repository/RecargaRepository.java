package com.tde.apiVallartaRecargas.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tde.apiVallartaRecargas.persistence.*;
import com.tde.apiVallartaRecargas.persistence.entity.Recarga;

public interface RecargaRepository extends JpaRepository<Recarga, Long> {
	

}
