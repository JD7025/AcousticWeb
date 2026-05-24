package com.acousticweb.controller;

import com.acousticweb.entity.Parlante;
import com.acousticweb.repository.ParlanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parlantes")
@RequiredArgsConstructor
public class ParlanteController {

    private final ParlanteRepository repository;

    @GetMapping
    public List<Parlante> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Parlante obtener(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parlante no encontrado."));
    }

    @PostMapping
    public Parlante crear(@RequestBody Parlante parlante) {
        return repository.save(parlante);
    }

    @PutMapping("/{id}")
    public Parlante actualizar(@PathVariable Long id, @RequestBody Parlante datos) {
        Parlante existente = obtener(id);

        existente.setMarca(datos.getMarca());
        existente.setModelo(datos.getModelo());
        existente.setTipo(datos.getTipo());
        existente.setPotenciaWatts(datos.getPotenciaWatts());
        existente.setRangoFrecuenciaMin(datos.getRangoFrecuenciaMin());
        existente.setRangoFrecuenciaMax(datos.getRangoFrecuenciaMax());
        existente.setDescripcion(datos.getDescripcion());

        return repository.save(existente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}