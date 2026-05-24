package com.acousticweb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "microfonos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Microfono {

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

    @Column(name = "tiene_calibracion", nullable = false)
    private Boolean tieneCalibracion;

    @Column(name = "archivo_calibracion_url")
    private String archivoCalibracionUrl;

    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (tieneCalibracion == null) {
            tieneCalibracion = false;
        }
    }
}