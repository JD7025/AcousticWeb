package com.acousticweb.repository;

import com.acousticweb.entity.Medicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicionRepository extends JpaRepository<Medicion, Long> {
    List<Medicion> findByUsuarioId(Long usuarioId);
}