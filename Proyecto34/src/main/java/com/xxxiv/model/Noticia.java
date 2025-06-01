package com.xxxiv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xxxiv.model.enums.Idioma;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "noticia")
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255, nullable = false)
    private String titulo;

    @Lob
    @Column(nullable = false)
    private String contenido;


//    @Column(name = "usuario_id")
//    private Integer usuarioId;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JsonIgnore
// //    @MapsId  // Indica que usa el mismo valor de PK que el usuario
//    @JoinColumn(name = "usuario_id")
//    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_noticia_usuario"))
    @JsonIgnore
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Idioma idiomaCodigo = Idioma.ESP;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
