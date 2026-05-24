package com.acousticweb.repository;

import com.acousticweb.entity.Microfono;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MicrofonoRepository extends JpaRepository<Microfono, Long> {
    List<Microfono> findByUsuarioId(Long usuarioId);
}   