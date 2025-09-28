package com.centromedico.sistema_turnos.mappers;


import com.centromedico.sistema_turnos.dtos.MedicoDTO;
import com.centromedico.sistema_turnos.model.Medico;
import org.springframework.stereotype.Component;


@Component
public class MedicoMapper {

    public MedicoDTO toDTO(Medico medico) {
        if (medico == null) return null;

        MedicoDTO dto = new MedicoDTO();
        dto.setId(medico.getId());
        dto.setNombre(medico.getNombre());
        dto.setApellido(medico.getApellido());
        dto.setDni(medico.getDni());
        dto.setMatricula(medico.getMatricula());
        dto.setTelefono(medico.getTelefono());
        dto.setActivo(true); // Hardeado

        // Campos opcionales con null-check
        if (medico.getEspecialidad() != null) {
            dto.setEspecialidadId(medico.getEspecialidad().getId());
            dto.setEspecialidadNombre(medico.getEspecialidad().getNombre());
        }
        if (medico.getConsultorio() != null) {
            dto.setConsultorioId(medico.getConsultorio().getId());
            dto.setConsultorioNumero(medico.getConsultorio().getNumero());
        }

        return dto;
    }
}

