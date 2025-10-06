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
        dto.setActivo(medico.isActivo());
        dto.setCreatedAt(medico.getCreatedAt());
        dto.setUpdatedAt(medico.getUpdatedAt());

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

    // NUEVO - Mapeo DTO → Entity
    public Medico toEntity(MedicoDTO dto) {
        if (dto == null) return null;

        Medico medico = new Medico();
        medico.setId(dto.getId());
        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setDni(dto.getDni());
        medico.setMatricula(dto.getMatricula());
        medico.setTelefono(dto.getTelefono());
        medico.setActivo(dto.isActivo());

        // Las relaciones (especialidad y consultorio) NO se mapean aquí
        // Se asignan manualmente en el servicio después de buscarlas en los repositorios

        return medico;
    }

    // NUEVO - Actualizar entidad existente desde DTO
    public void updateEntityFromDTO(MedicoDTO dto, Medico medico) {
        if (dto == null || medico == null) return;

        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setDni(dto.getDni());
        medico.setMatricula(dto.getMatricula());
        medico.setTelefono(dto.getTelefono());

    }
}

