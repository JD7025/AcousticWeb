package com.acousticweb.dto;

public record AuthResponse(
        Long id,
        String nombre,
        String correo,
        String rol
) {}