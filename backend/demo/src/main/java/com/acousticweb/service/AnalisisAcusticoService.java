package com.acousticweb.service;

import com.acousticweb.dto.AnalisisResponse;
import com.acousticweb.dto.FiltroEqResponse;
import com.acousticweb.entity.DatoFrecuencia;
import com.acousticweb.enums.TipoFiltroEQ;
import com.acousticweb.repository.DatoFrecuenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalisisAcusticoService {

    private final DatoFrecuenciaRepository datoFrecuenciaRepository;

    private static final BigDecimal MAX_BOOST_DB = BigDecimal.valueOf(3.0);
    private static final BigDecimal MAX_CUT_DB = BigDecimal.valueOf(-9.0);
    private static final BigDecimal UMBRAL_CORRECCION_DB = BigDecimal.valueOf(3.0);

    public AnalisisResponse analizarMedicion(Long medicionId) {
        List<DatoFrecuencia> datos = datoFrecuenciaRepository.findByMedicionIdOrderByFrecuenciaHzAsc(medicionId);

        if (datos.isEmpty()) {
            throw new IllegalArgumentException("La medición no tiene datos de frecuencia.");
        }

        List<FiltroEqResponse> filtros = new ArrayList<>();
        int orden = 1;
        BigDecimal mayorBoost = BigDecimal.ZERO;

        for (DatoFrecuencia dato : datos) {
            BigDecimal frecuencia = dato.getFrecuenciaHz();
            BigDecimal nivel = dato.getNivelDb();
            BigDecimal objetivo = calcularTargetHouseCurve(frecuencia);
            BigDecimal diferencia = nivel.subtract(objetivo);

            if (diferencia.compareTo(UMBRAL_CORRECCION_DB) > 0) {
                BigDecimal ganancia = diferencia.negate();

                if (ganancia.compareTo(MAX_CUT_DB) < 0) {
                    ganancia = MAX_CUT_DB;
                }

                filtros.add(new FiltroEqResponse(
                        TipoFiltroEQ.PEAK.name(),
                        frecuencia.setScale(2, RoundingMode.HALF_UP),
                        ganancia.setScale(2, RoundingMode.HALF_UP),
                        BigDecimal.valueOf(1.40),
                        orden++
                ));
            }

            if (diferencia.compareTo(UMBRAL_CORRECCION_DB.negate()) < 0) {
                BigDecimal boostNecesario = diferencia.abs();

                if (boostNecesario.compareTo(BigDecimal.valueOf(10)) <= 0) {
                    BigDecimal ganancia = boostNecesario.min(MAX_BOOST_DB);

                    filtros.add(new FiltroEqResponse(
                            TipoFiltroEQ.PEAK.name(),
                            frecuencia.setScale(2, RoundingMode.HALF_UP),
                            ganancia.setScale(2, RoundingMode.HALF_UP),
                            BigDecimal.valueOf(1.20),
                            orden++
                    ));

                    if (ganancia.compareTo(mayorBoost) > 0) {
                        mayorBoost = ganancia;
                    }
                }
            }

            if (filtros.size() >= 8) {
                break;
            }
        }

        BigDecimal preamp = mayorBoost.compareTo(BigDecimal.ZERO) > 0
                ? mayorBoost.negate().subtract(BigDecimal.ONE)
                : BigDecimal.ZERO;

        String resumen = "Análisis conservador generado. Se prioriza reducir picos y limitar aumentos para evitar clipping o deterioro del sonido.";

        return new AnalisisResponse(
                medicionId,
                preamp.setScale(2, RoundingMode.HALF_UP),
                "HOUSE_CURVE_SUAVE",
                resumen,
                filtros
        );
    }

    private BigDecimal calcularTargetHouseCurve(BigDecimal frecuenciaHz) {
        double f = frecuenciaHz.doubleValue();

        if (f < 100) {
            return BigDecimal.valueOf(78.0);
        }

        if (f <= 1000) {
            return BigDecimal.valueOf(75.0);
        }

        if (f <= 10000) {
            return BigDecimal.valueOf(73.0);
        }

        return BigDecimal.valueOf(71.0);
    }
}