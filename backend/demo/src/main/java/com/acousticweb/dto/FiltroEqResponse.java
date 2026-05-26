package com.acousticweb.dto;

import java.math.BigDecimal;

public record FiltroEQResponse(
        String tipo,
        BigDecimal frecuenciaHz,
        BigDecimal gananciaDb,
        BigDecimal q,
        Integer orden
) {}