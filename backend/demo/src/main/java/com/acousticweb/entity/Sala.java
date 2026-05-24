package com.acousticweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "salas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private AppUser usuario;

    @Column(nullable = false)
    private String nombre;

    private String tipo;

    @Column(name = "ancho_m")
    private BigDecimal anchoM;

    @Column(name = "largo_m")
    private BigDecimal largoM;

    @Column(name = "alto_m")
    private BigDecimal altoM;

    @Column(name = "material_paredes")
    private String materialParedes;

    @Column(name = "material_piso")
    private String materialPiso;

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