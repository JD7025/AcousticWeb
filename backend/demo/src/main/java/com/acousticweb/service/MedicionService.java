package com.acousticweb.service;

import com.acousticweb.dto.MedicionRequest;
import com.acousticweb.dto.DatoFrecuenciaRequest;
import com.acousticweb.entity.*;
import com.acousticweb.enums.EstadoMedicion;
import com.acousticweb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicionService {

    private final AppUserRepository usuarioRepository;
    private final ParlanteRepository parlanteRepository;
    private final MicrofonoRepository microfonoRepository;
    private final SalaRepository salaRepository;
    private final MedicionRepository medicionRepository;
    private final DatoFrecuenciaRepository datoFrecuenciaRepository;

    public Medicion crearMedicion(MedicionRequest request) {
        AppUser usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Parlante parlante = parlanteRepository.findById(request.parlanteId())
                .orElseThrow(() -> new IllegalArgumentException("Parlante no encontrado."));

        Microfono microfono = microfonoRepository.findById(request.microfonoId())
                .orElseThrow(() -> new IllegalArgumentException("Micrófono no encontrado."));

        Sala sala = salaRepository.findById(request.salaId())
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada."));

        if (request.frecuenciaFin().compareTo(request.frecuenciaInicio()) <= 0) {
            throw new IllegalArgumentException("La frecuencia final debe ser mayor que la inicial.");
        }

        Medicion medicion = Medicion.builder()
                .usuario(usuario)
                .parlante(parlante)
                .microfono(microfono)
                .sala(sala)
                .nombre(request.nombre())
                .nivelSpl(request.nivelSpl())
                .frecuenciaInicio(request.frecuenciaInicio())
                .frecuenciaFin(request.frecuenciaFin())
                .ruidoAmbienteDb(request.ruidoAmbienteDb())
                .estado(EstadoMedicion.PENDIENTE)
                .observaciones(request.observaciones())
                .build();

        return medicionRepository.save(medicion);
    }

    public List<DatoFrecuencia> guardarDatos(Long medicionId, List<DatoFrecuenciaRequest> datos) {
        Medicion medicion = medicionRepository.findById(medicionId)
                .orElseThrow(() -> new IllegalArgumentException("Medición no encontrada."));

        List<DatoFrecuencia> entidades = datos.stream()
                .map(d -> DatoFrecuencia.builder()
                        .medicion(medicion)
                        .frecuenciaHz(d.frecuenciaHz())
                        .nivelDb(d.nivelDb())
                        .faseGrados(d.faseGrados())
                        .build())
                .toList();

        return datoFrecuenciaRepository.saveAll(entidades);
    }

    public List<DatoFrecuencia> obtenerDatos(Long medicionId) {
        return datoFrecuenciaRepository.findByMedicionIdOrderByFrecuenciaHzAsc(medicionId);
    }
}