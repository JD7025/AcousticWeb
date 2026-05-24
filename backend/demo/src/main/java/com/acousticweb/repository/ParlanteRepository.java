package com.acousticweb.repository;

import com.acousticweb.entity.Parlante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;  

public interface ParlanteRepository extends JpaRepository<Parlante, Long> {
    List<Parlante> findByUsuarioId(Long usuarioId);
}