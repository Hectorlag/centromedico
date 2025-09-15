package com.centromedico.sistema_turnos.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteDTO {

    private Long id;

    @NotBlank(message = "El DNI es requerido")
    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener 7 u 8 dígitos")
    private String dni;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @Pattern(regexp = "\\d{10,15}", message = "El teléfono debe tener entre 10 y 15 dígitos")
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Size(max = 100, message = "La obra social no puede exceder 100 caracteres")
    private String obraSocial;

    private boolean activo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Método helper para mostrar nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

}