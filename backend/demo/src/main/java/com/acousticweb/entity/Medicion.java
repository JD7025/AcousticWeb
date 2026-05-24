package com.acousticweb.entity;

import com.acousticweb.enums.EstadoMedicion;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mediciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private AppUser usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parlante_id")
    private Parlante parlante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "microfono_id")
    private Microfono microfono;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "nivel_spl")
    private BigDecimal nivelSpl;

    @Column(name = "frecuencia_inicio", nullable = false)
    private BigDecimal frecuenciaInicio;

    @Column(name = "frecuencia_fin", nullable = false)
    private BigDecimal frecuenciaFin;

    @Column(name = "ruido_ambiente_db")
    private BigDecimal ruidoAmbienteDb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMedicion estado;

    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoMedicion.PENDIENTE;
        }
    }
}