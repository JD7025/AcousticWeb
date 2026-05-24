package com.acousticweb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CrearMedicionRequest(
        @NotNull Long usuarioId,
        @NotNull Long parlanteId,
        @NotNull Long microfonoId,
        @NotNull Long salaId,

        @NotBlank String nombre,

        BigDecimal nivelSpl,

        @NotNull @Positive BigDecimal frecuenciaInicio,
        @NotNull @Positive BigDecimal frecuenciaFin,

        BigDecimal ruidoAmbienteDb,
        String observaciones
) {}