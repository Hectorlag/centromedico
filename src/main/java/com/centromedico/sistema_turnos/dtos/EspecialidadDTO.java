package com.centromedico.sistema_turnos.dtos;

import jakarta.validation.constraints.Min;
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
public class EspecialidadDTO {

    private Long id;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String descripcion;

    @Min(value = 1, message = "La duración del turno debe ser mayor a 0 minutos")
    private Integer duracionTurnoMinutos;

    private boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
