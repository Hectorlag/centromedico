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

    // Datos básicos del médico (sin cargar entidad completa)
    private Long medicoId;
    private String medicoNombre;
    private String medicoApellido;

    // Datos básicos del paciente (sin cargar entidad completa)
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;

    // Métodos helper
    public String getMedicoNombreCompleto() {
        return medicoNombre + " " + medicoApellido;
    }

    public String getPacienteNombreCompleto() {
        return pacienteNombre + " " + pacienteApellido;
    }
}
