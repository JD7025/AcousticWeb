package com.acousticweb.repository;

import com.acousticweb.entity.DatoFrecuencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatoFrecuenciaRepository extends JpaRepository<DatoFrecuencia, Long> {
    List<DatoFrecuencia> findByMedicionIdOrderByFrecuenciaHzAsc(Long medicionId);
}