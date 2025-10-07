package com.centromedico.sistema_turnos.dtos;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Data  // ✅ Agregada para generar getters, setters, equals, hashCode, toString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private String estado;
    private String observaciones;
    private boolean activo;

    // Datos del médico
    private Long medicoId;
    private String medicoNombre;
    private String medicoApellido;

    // Datos del paciente
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;

    // NUEVO - Especialidad y consultorio
    private String especialidadNombre;
    private String consultorioNumero;

    // Métodos helper
    public String getMedicoNombreCompleto() {
        return medicoApellido + ", " + medicoNombre;
    }

    public String getPacienteNombreCompleto() {
        return pacienteApellido + ", " + pacienteNombre;
    }
}
