package com.acousticweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "perfiles_ecualizacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilEcualizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "medicion_id")
    private Medicion medicion;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "preamp_db", nullable = false)
    private BigDecimal preampDb;

    @Column(name = "curva_objetivo", nullable = false)
    private String curvaObjetivo;

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