package com.centromedico.sistema_turnos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConsultorioDTO {

    private Long id;

    @NotBlank(message = "El número es requerido")
    @Size(min = 1, max = 10, message = "El número debe tener entre 1 y 10 caracteres")
    private String numero;

    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @Size(max = 10, message = "El piso no puede tener más de 10 caracteres")
    private String piso;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String descripcion;

    private boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
