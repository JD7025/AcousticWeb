package com.acousticweb.controller;

import com.acousticweb.dto.CrearMedicionRequest;
import com.acousticweb.dto.DatoFrecuenciaRequest;
import com.acousticweb.entity.DatoFrecuencia;
import com.acousticweb.entity.Medicion;
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

    private final MedicionService medicionService;
    private final MedicionRepository medicionRepository;

    @GetMapping
    public List<Medicion> listar() {
        return medicionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Medicion obtenerPorId(@PathVariable Long id) {
        return medicionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medición no encontrada."));
    }

    @PostMapping
    public Medicion crear(@Valid @RequestBody CrearMedicionRequest request) {
        return medicionService.crearMedicion(request);
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
        return medicionService.obtenerDatos(id);
    }
}