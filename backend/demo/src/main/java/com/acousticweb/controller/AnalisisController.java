package com.acousticweb.controller;

import com.acousticweb.dto.AnalisisResponse;
import com.acousticweb.service.AnalisisAcusticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analisis")
@RequiredArgsConstructor
public class AnalisisController {

    private final AnalisisAcusticoService analisisAcusticoService;

    @PostMapping("/medicion/{id}")
    public AnalisisResponse analizar(@PathVariable Long id) {
        return analisisAcusticoService.analizarMedicion(id);
    }
}