package com.acousticweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MedicionRequest(

        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "El parlante es obligatorio")
        Long parlanteId,

        @NotNull(message = "El micrófono es obligatorio")
        Long microfonoId,

        @NotNull(message = "La sala es obligatoria")
        Long salaId,

        @NotBlank(message = "El nombre de la medición es obligatorio")
        String nombre,

        BigDecimal nivelSpl,

        @NotNull(message = "La frecuencia inicial es obligatoria")
        @Positive(message = "La frecuencia inicial debe ser positiva")
        BigDecimal frecuenciaInicio,

        @NotNull(message = "La frecuencia final es obligatoria")
        @Positive(message = "La frecuencia final debe ser positiva")
        BigDecimal frecuenciaFin,

        BigDecimal ruidoAmbienteDb,

        String observaciones
) {
}