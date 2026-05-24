package com.acousticweb.repository;

import com.acousticweb.entity.FiltroEQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FiltroEQRepository extends JpaRepository<FiltroEQ, Long> {
    List<FiltroEQ> findByPerfilIdOrderByOrdenAsc(Long perfilId);
}