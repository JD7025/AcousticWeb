package com.acousticweb.controller;

import com.acousticweb.entity.Microfono;
import com.acousticweb.repository.MicrofonoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/microfonos")
@RequiredArgsConstructor
public class MicrofonoController {

    private final MicrofonoRepository repository;

    @GetMapping
    public List<Microfono> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Microfono obtener(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Micrófono no encontrado."));
    }

    @PostMapping
    public Microfono crear(@RequestBody Microfono microfono) {
        return repository.save(microfono);
    }

    @PutMapping("/{id}")
    public Microfono actualizar(@PathVariable Long id, @RequestBody Microfono datos) {
        Microfono existente = obtener(id);

        existente.setMarca(datos.getMarca());
        existente.setModelo(datos.getModelo());
        existente.setTipo(datos.getTipo());
        existente.setTieneCalibracion(datos.getTieneCalibracion());
        existente.setArchivoCalibracionUrl(datos.getArchivoCalibracionUrl());
        existente.setDescripcion(datos.getDescripcion());

        return repository.save(existente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}