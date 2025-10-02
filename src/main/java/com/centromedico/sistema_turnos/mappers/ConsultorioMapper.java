package com.centromedico.sistema_turnos.mappers;

import com.centromedico.sistema_turnos.dtos.ConsultorioDTO;
import com.centromedico.sistema_turnos.model.Consultorio;
import org.springframework.stereotype.Component;


@Component
public class ConsultorioMapper {

    // Mapeo para enviar al frontend
    // Entity → DTO (toDTO)
    public ConsultorioDTO toDTO(Consultorio consultorio) {
        if (consultorio == null) return null;

        ConsultorioDTO dto = new ConsultorioDTO();
        dto.setId(consultorio.getId());
        dto.setNumero(consultorio.getNumero());
        dto.setNombre(consultorio.getNombre());
        dto.setPiso(consultorio.getPiso());
        dto.setDescripcion(consultorio.getDescripcion());
        dto.setActivo(consultorio.isActivo());
        dto.setCreatedAt(consultorio.getCreatedAt());
        dto.setUpdatedAt(consultorio.getUpdatedAt());
        return dto;
    }

    // Mapeo para enviar al backend
    // DTO → Entity (toEntity)
    public Consultorio toEntity(ConsultorioDTO dto) {
        if (dto == null) return null;

        Consultorio consultorio = new Consultorio();
        consultorio.setNumero(dto.getNumero());
        consultorio.setNombre(dto.getNombre());
        consultorio.setPiso(dto.getPiso());
        consultorio.setDescripcion(dto.getDescripcion());
        return consultorio;
    }

    // updateEntityFromDTO - MODIFICA una existente
    public void updateEntityFromDTO(ConsultorioDTO dto, Consultorio consultorio) {
        if (dto == null || consultorio == null) return;

        consultorio.setNumero(dto.getNumero());
        consultorio.setNombre(dto.getNombre());
        consultorio.setPiso(dto.getPiso());
        consultorio.setDescripcion(dto.getDescripcion());
    }
}
