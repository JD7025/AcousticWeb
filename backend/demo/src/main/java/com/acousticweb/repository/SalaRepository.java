package com.acousticweb.repository;

import com.acousticweb.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByUsuarioId(Long usuarioId);
}