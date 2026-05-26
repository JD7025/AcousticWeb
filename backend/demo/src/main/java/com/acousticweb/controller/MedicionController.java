package com.acousticweb.controller;

import com.acousticweb.dto.DatoFrecuenciaRequest;
import com.acousticweb.dto.MedicionRequest;
import com.acousticweb.entity.DatoFrecuencia;
import com.acousticweb.entity.Medicion;
import com.acousticweb.repository.DatoFrecuenciaRepository;
import com.acousticweb.repository.MedicionRepository;
import com.acousticweb.service.MedicionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mediciones")
@RequiredArgsConstructor
public class MedicionController {

    private final MedicionRepository medicionRepository;
    private final DatoFrecuenciaRepository datoFrecuenciaRepository;
    private final MedicionService medicionService;

    @GetMapping
    public List<Medicion> listar() {
        return medicionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Medicion obtener(@PathVariable Long id) {
        return medicionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medición no encontrada."));
    }

    @PostMapping
    public Medicion crear(@Valid @RequestBody MedicionRequest request) {
        return medicionService.crearMedicion(request);
    }

    @PutMapping("/{id}")
    public Medicion actualizar(@PathVariable Long id, @RequestBody Medicion datos) {
        Medicion existente = obtener(id);

        existente.setNombre(datos.getNombre());
        existente.setNivelSpl(datos.getNivelSpl());
        existente.setFrecuenciaInicio(datos.getFrecuenciaInicio());
        existente.setFrecuenciaFin(datos.getFrecuenciaFin());
        existente.setRuidoAmbienteDb(datos.getRuidoAmbienteDb());
        existente.setEstado(datos.getEstado());
        existente.setObservaciones(datos.getObservaciones());

        return medicionRepository.save(existente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        medicionRepository.deleteById(id);
    }

    @PostMapping("/{id}/datos")
    public List<DatoFrecuencia> guardarDatos(
            @PathVariable Long id,
            @Valid @RequestBody List<DatoFrecuenciaRequest> datos
    ) {
        return medicionService.guardarDatos(id, datos);
    }

    @GetMapping("/{id}/datos")
    public List<DatoFrecuencia> obtenerDatos(@PathVariable Long id) {
        return datoFrecuenciaRepository.findByMedicionIdOrderByFrecuenciaHzAsc(id);
    }
}