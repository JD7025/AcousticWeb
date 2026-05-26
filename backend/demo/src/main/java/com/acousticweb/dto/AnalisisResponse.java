package com.acousticweb.dto;

import java.math.BigDecimal;
import java.util.List;

public record AnalisisResponse(
        Long medicionId,
        BigDecimal preampRecomendado,
        String curvaObjetivo,
        String resumen,
        List<FiltroEQResponse> filtros
) {}