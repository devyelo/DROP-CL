package com.dropcl.msusuario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "direcciones_envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La calle es obligatoria")
    @Column(nullable = false)
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    @Column(nullable = false)
    private String numero;

    private String departamento;

    @NotBlank(message = "La comuna es obligatoria")
    @Column(nullable = false)
    private String comuna;

    @NotBlank(message = "La región es obligatoria")
    @Column(nullable = false)
    private String region;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false)
    private String ciudad;

    @Pattern(regexp = "^[0-9]{7}$", message = "Código postal debe tener 7 dígitos")
    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    @ToString.Exclude
    private Perfil perfil;

    @PrePersist
    public void prePersist() {
        if (this.esPrincipal == null) {
            this.esPrincipal = false;
        }
    }
}