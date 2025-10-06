package com.centromedico.sistema_turnos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoDetalleDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String matricula;
    private String telefono;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Asociaciones SINGULARES
    private EspecialidadDTO especialidad;
    private ConsultorioDTO consultorio;

    // Método helper
    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }
}