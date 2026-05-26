package com.acousticweb.service;

import com.acousticweb.entity.FiltroEQ;
import com.acousticweb.entity.PerfilEcualizacion;
import com.acousticweb.enums.TipoFiltroEQ;
import com.acousticweb.repository.FiltroEQRepository;
import com.acousticweb.repository.PerfilEcualizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExportacionService {

    private final PerfilEcualizacionRepository perfilRepository;
    private final FiltroEQRepository filtroRepository;

    public String exportarEqualizerApo(Long perfilId) {
        PerfilEcualizacion perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de ecualización no encontrado."));

        List<FiltroEQ> filtros = filtroRepository.findByPerfilIdOrderByOrdenAsc(perfilId);

        StringBuilder builder = new StringBuilder();

        builder.append("# AcousticWeb - Perfil de ecualización\n");
        builder.append("# Perfil: ").append(perfil.getNombre()).append("\n");
        builder.append("# Curva objetivo: ").append(perfil.getCurvaObjetivo()).append("\n");
        builder.append("# Generado para Equalizer APO / Peace\n\n");

        builder.append("Preamp: ")
                .append(formatearDecimal(perfil.getPreampDb().doubleValue()))
                .append(" dB\n");

        for (FiltroEQ filtro : filtros) {
            builder.append("Filter ")
                    .append(filtro.getOrden())
                    .append(": ON ")
                    .append(mapearTipoEqualizerApo(filtro.getTipo()))
                    .append(" Fc ")
                    .append(formatearDecimal(filtro.getFrecuenciaHz().doubleValue()))
                    .append(" Hz Gain ")
                    .append(formatearDecimal(filtro.getGananciaDb().doubleValue()))
                    .append(" dB Q ")
                    .append(formatearDecimal(filtro.getQ().doubleValue()))
                    .append("\n");
        }

        return builder.toString();
    }

    public String exportarResumenTexto(Long perfilId) {
        PerfilEcualizacion perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de ecualización no encontrado."));

        List<FiltroEQ> filtros = filtroRepository.findByPerfilIdOrderByOrdenAsc(perfilId);

        StringBuilder builder = new StringBuilder();

        builder.append("Resumen del perfil de ecualización\n");
        builder.append("Nombre: ").append(perfil.getNombre()).append("\n");
        builder.append("Preamp recomendado: ").append(perfil.getPreampDb()).append(" dB\n");
        builder.append("Curva objetivo: ").append(perfil.getCurvaObjetivo()).append("\n");
        builder.append("Descripción: ").append(perfil.getDescripcion()).append("\n\n");
        builder.append("Filtros:\n");

        for (FiltroEQ filtro : filtros) {
            builder.append("- ")
                    .append(filtro.getTipo())
                    .append(" | ")
                    .append(filtro.getFrecuenciaHz())
                    .append(" Hz | ")
                    .append(filtro.getGananciaDb())
                    .append(" dB | Q ")
                    .append(filtro.getQ())
                    .append("\n");
        }

        return builder.toString();
    }

    private String mapearTipoEqualizerApo(TipoFiltroEQ tipo) {
        return switch (tipo) {
            case PEAK -> "PK";
            case LOW_SHELF -> "LS";
            case HIGH_SHELF -> "HS";
            case LOW_PASS -> "LP";
            case HIGH_PASS -> "HP";
        };
    }

    private String formatearDecimal(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }
}