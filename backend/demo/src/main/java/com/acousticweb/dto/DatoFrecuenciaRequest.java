package com.acousticweb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DatoFrecuenciaRequest(
        @NotNull @Positive BigDecimal frecuenciaHz,
        @NotNull BigDecimal nivelDb,
        BigDecimal faseGrados
) {}