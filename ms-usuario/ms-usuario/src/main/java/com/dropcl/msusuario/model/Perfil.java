package com.dropcl.msusuario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "perfiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "usuario_id", nullable = false, unique = true)
    private Long usuarioId;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String apellido;

    @Pattern(regexp = "^[0-9]{8,9}-[0-9kK]$", message = "RUT inválido")
    @Column(unique = true)
    private String rut;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Teléfono inválido")
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;


    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(name = "reputacion_vendedor")
    private Double reputacionVendedor;


    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(name = "reputacion_comprador")
    private Double reputacionComprador;

    @Column(name = "total_ventas")
    private Integer totalVentas;

    @Column(name = "total_compras")
    private Integer totalCompras;


    @JsonIgnore
    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DireccionEnvio> direcciones;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.reputacionVendedor = 0.0;
        this.reputacionComprador = 0.0;
        this.totalVentas = 0;
        this.totalCompras = 0;
    }

}
