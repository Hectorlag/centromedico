package com.centromedico.sistema_turnos.mappers;

import com.centromedico.sistema_turnos.dtos.EspecialidadDTO;
import com.centromedico.sistema_turnos.model.Especialidad;
import org.springframework.stereotype.Component;

@Component
public class EspecialidadMapper {

    public EspecialidadDTO toDTO(Especialidad especialidad){
        if(especialidad == null) return null;

        EspecialidadDTO dto = new EspecialidadDTO();
        dto.setId(especialidad.getId());
        dto.setNombre(especialidad.getNombre());
        dto.setDescripcion(especialidad.getDescripcion());
        dto.setDuracionTurnoMinutos(especialidad.getDuracionTurnoMinutos());
        dto.setActivo(especialidad.isActivo());
        dto.setCreatedAt(especialidad.getCreatedAt());
        dto.setUpdatedAt(especialidad.getUpdatedAt());
        return dto;
    }

    public Especialidad toEntity(EspecialidadDTO dto){
        if(dto == null) return null;

        Especialidad especialidad = new Especialidad();
        especialidad.setId(dto.getId());
        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());
        especialidad.setDuracionTurnoMinutos(dto.getDuracionTurnoMinutos());
        return especialidad;
    }

    public void updateEntityFromDTO(EspecialidadDTO dto, Especialidad especialidad) {
        if (dto == null || especialidad == null) return;

        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());
        especialidad.setDuracionTurnoMinutos(dto.getDuracionTurnoMinutos());
    }
}
