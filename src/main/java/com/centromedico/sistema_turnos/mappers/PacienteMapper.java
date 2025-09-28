package com.centromedico.sistema_turnos.mappers;


import com.centromedico.sistema_turnos.dtos.PacienteDTO;
import com.centromedico.sistema_turnos.model.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public PacienteDTO toDTO(Paciente paciente) {
        if (paciente == null) return null;

        return PacienteDTO.builder()
                .id(paciente.getId())
                .dni(paciente.getDni())
                .nombre(paciente.getNombre())
                .apellido(paciente.getApellido())
                .telefono(paciente.getTelefono())
                .email(paciente.getEmail())
                .obraSocial(paciente.getObraSocial())
                .activo(paciente.isActivo())
                .build();
    }
}