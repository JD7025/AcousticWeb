package com.acousticweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parlantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parlante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private AppUser usuario;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    private String tipo;

    @Column(name = "potencia_watts")
    private BigDecimal potenciaWatts;

    @Column(name = "rango_frecuencia_min")
    private BigDecimal rangoFrecuenciaMin;

    @Column(name = "rango_frecuencia_max")
    private BigDecimal rangoFrecuenciaMax;

    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}