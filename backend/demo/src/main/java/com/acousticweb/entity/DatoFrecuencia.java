package com.acousticweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "datos_frecuencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatoFrecuencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medicion_id")
    private Medicion medicion;

    @Column(name = "frecuencia_hz", nullable = false)
    private BigDecimal frecuenciaHz;

    @Column(name = "nivel_db", nullable = false)
    private BigDecimal nivelDb;

    @Column(name = "fase_grados")
    private BigDecimal faseGrados;
}