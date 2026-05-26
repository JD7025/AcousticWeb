package com.acousticweb.controller;

import com.acousticweb.entity.FiltroEQ;
import com.acousticweb.entity.PerfilEcualizacion;
import com.acousticweb.service.ExportacionService;
import com.acousticweb.service.PerfilEqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfiles-eq")
@RequiredArgsConstructor
public class PerfilEqController {

    private final PerfilEqService perfilEqService;
    private final ExportacionService exportacionService;

    @GetMapping
    public List<PerfilEcualizacion> listar() {
        return perfilEqService.listar();
    }

    @GetMapping("/{id}")
    public PerfilEcualizacion obtenerPorId(@PathVariable Long id) {
        return perfilEqService.obtenerPorId(id);
    }

    @GetMapping("/medicion/{medicionId}")
    public PerfilEcualizacion obtenerPorMedicion(@PathVariable Long medicionId) {
        return perfilEqService.obtenerPorMedicion(medicionId);
    }

    @PostMapping("/generar/medicion/{medicionId}")
    public PerfilEcualizacion generarDesdeMedicion(@PathVariable Long medicionId) {
        return perfilEqService.generarPerfilDesdeMedicion(medicionId);
    }

    @GetMapping("/{id}/filtros")
    public List<FiltroEQ> obtenerFiltros(@PathVariable Long id) {
        return perfilEqService.obtenerFiltros(id);
    }

    @GetMapping("/{id}/export/equalizer-apo")
    public ResponseEntity<String> exportarEqualizerApo(@PathVariable Long id) {
        String contenido = exportacionService.exportarEqualizerApo(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=acousticweb-perfil-" + id + ".txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(contenido);
    }

    @GetMapping("/{id}/export/resumen")
    public ResponseEntity<String> exportarResumen(@PathVariable Long id) {
        String contenido = exportacionService.exportarResumenTexto(id);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(contenido);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        perfilEqService.eliminar(id);
    }
}