package com.acousticweb.controller;

import com.acousticweb.entity.Sala;
import com.acousticweb.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@RequiredArgsConstructor
public class SalaController {

    private final SalaRepository repository;

    @GetMapping
    public List<Sala> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Sala obtener(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada."));
    }

    @PostMapping
    public Sala crear(@RequestBody Sala sala) {
        return repository.save(sala);
    }

    @PutMapping("/{id}")
    public Sala actualizar(@PathVariable Long id, @RequestBody Sala datos) {
        Sala existente = obtener(id);

        existente.setNombre(datos.getNombre());
        existente.setTipo(datos.getTipo());
        existente.setAnchoM(datos.getAnchoM());
        existente.setLargoM(datos.getLargoM());
        existente.setAltoM(datos.getAltoM());
        existente.setMaterialParedes(datos.getMaterialParedes());
        existente.setMaterialPiso(datos.getMaterialPiso());
        existente.setDescripcion(datos.getDescripcion());

        return repository.save(existente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}