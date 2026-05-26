package com.acousticweb.service;

import com.acousticweb.dto.AnalisisResponse;
import com.acousticweb.dto.FiltroEQResponse;
import com.acousticweb.entity.FiltroEQ;
import com.acousticweb.entity.Medicion;
import com.acousticweb.entity.PerfilEcualizacion;
import com.acousticweb.enums.TipoFiltroEQ;
import com.acousticweb.repository.FiltroEQRepository;
import com.acousticweb.repository.MedicionRepository;
import com.acousticweb.repository.PerfilEcualizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilEqService {

    private final MedicionRepository medicionRepository;
    private final PerfilEcualizacionRepository perfilRepository;
    private final FiltroEQRepository filtroRepository;
    private final AnalisisAcusticoService analisisAcusticoService;

    public PerfilEcualizacion generarPerfilDesdeMedicion(Long medicionId) {
        Medicion medicion = medicionRepository.findById(medicionId)
                .orElseThrow(() -> new IllegalArgumentException("Medición no encontrada."));

        perfilRepository.findByMedicionId(medicionId).ifPresent(perfilExistente -> {
            throw new IllegalArgumentException("Esta medición ya tiene un perfil de ecualización generado.");
        });

        AnalisisResponse analisis = analisisAcusticoService.analizarMedicion(medicionId);

        PerfilEcualizacion perfil = PerfilEcualizacion.builder()
                .medicion(medicion)
                .nombre("Perfil EQ - " + medicion.getNombre())
                .preampDb(analisis.preampRecomendado() != null ? analisis.preampRecomendado() : BigDecimal.ZERO)
                .curvaObjetivo(analisis.curvaObjetivo())
                .descripcion(analisis.resumen())
                .build();

        PerfilEcualizacion perfilGuardado = perfilRepository.save(perfil);

        List<FiltroEQ> filtros = analisis.filtros()
                .stream()
                .map(filtro -> convertirFiltro(perfilGuardado, filtro))
                .toList();

        filtroRepository.saveAll(filtros);

        return perfilGuardado;
    }

    public List<PerfilEcualizacion> listar() {
        return perfilRepository.findAll();
    }

    public PerfilEcualizacion obtenerPorId(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Perfil de ecualización no encontrado."));
    }

    public PerfilEcualizacion obtenerPorMedicion(Long medicionId) {
        return perfilRepository.findByMedicionId(medicionId)
                .orElseThrow(() -> new IllegalArgumentException("La medición no tiene perfil de ecualización."));
    }

    public List<FiltroEQ> obtenerFiltros(Long perfilId) {
        obtenerPorId(perfilId);
        return filtroRepository.findByPerfilIdOrderByOrdenAsc(perfilId);
    }

    public void eliminar(Long id) {
        PerfilEcualizacion perfil = obtenerPorId(id);
        perfilRepository.delete(perfil);
    }

    private FiltroEQ convertirFiltro(PerfilEcualizacion perfil, FiltroEQResponse filtro) {
        return FiltroEQ.builder()
                .perfil(perfil)
                .tipo(TipoFiltroEQ.valueOf(filtro.tipo()))
                .frecuenciaHz(filtro.frecuenciaHz())
                .gananciaDb(filtro.gananciaDb())
                .q(filtro.q())
                .orden(filtro.orden())
                .build();
    }
}