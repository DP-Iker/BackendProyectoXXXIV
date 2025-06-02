package com.xxxiv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "calificacion")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "vehiculo_id", nullable = false)
    @NotNull
    private Integer vehiculoId;

    @Column(name = "usuario_id", nullable = false)
    @NotNull
    private Integer usuarioId;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    @Max(5)
    private Integer calificacion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
