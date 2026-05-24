package com.acousticweb.entity;

import com.acousticweb.enums.TipoFiltroEQ;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "filtros_eq")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiltroEQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "perfil_id")
    private PerfilEcualizacion perfil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFiltroEQ tipo;

    @Column(name = "frecuencia_hz", nullable = false)
    private BigDecimal frecuenciaHz;

    @Column(name = "ganancia_db", nullable = false)
    private BigDecimal gananciaDb;

    @Column(nullable = false)
    private BigDecimal q;

    @Column(nullable = false)
    private Integer orden;
}