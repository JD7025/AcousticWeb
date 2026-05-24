package com.acousticweb.repository;

import com.acousticweb.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}