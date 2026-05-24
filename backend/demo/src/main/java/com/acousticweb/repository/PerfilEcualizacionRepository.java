package com.acousticweb.repository;

import com.acousticweb.entity.PerfilEcualizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilEcualizacionRepository extends JpaRepository<PerfilEcualizacion, Long> {
    Optional<PerfilEcualizacion> findByMedicionId(Long medicionId);
}