package com.centromedico.sistema_turnos.mappers;

import com.centromedico.sistema_turnos.dtos.CrearTurnoDto;
import com.centromedico.sistema_turnos.dtos.TurnoDTO;
import com.centromedico.sistema_turnos.model.Turno;
import org.springframework.stereotype.Component;


@Component
public class TurnoMapper {

    // ===== ENTITY → DTO (para mostrar) =====
    public TurnoDTO toDTO(Turno turno) {
        if (turno == null) return null;

        return TurnoDTO.builder()
                .id(turno.getId())
                .fecha(turno.getFecha())
                .horaInicio(turno.getHoraProgramada())
                .estado(turno.getEstado())
                .observaciones(null) // Si lo agregas a la entidad más adelante
                .activo(getActivoValue(turno))
                // Datos del médico
                .medicoId(turno.getMedico() != null ? turno.getMedico().getId() : null)
                .medicoNombre(turno.getMedico() != null ? turno.getMedico().getNombre() : null)
                .medicoApellido(turno.getMedico() != null ? turno.getMedico().getApellido() : null)
                // Datos del paciente
                .pacienteId(turno.getPaciente() != null ? turno.getPaciente().getId() : null)
                .pacienteNombre(turno.getPaciente() != null ? turno.getPaciente().getNombre() : null)
                .pacienteApellido(turno.getPaciente() != null ? turno.getPaciente().getApellido() : null)
                // Especialidad y consultorio
                .especialidadNombre(turno.getMedico() != null && turno.getMedico().getEspecialidad() != null
                        ? turno.getMedico().getEspecialidad().getNombre() : null)
                .consultorioNumero(turno.getMedico() != null && turno.getMedico().getConsultorio() != null
                        ? turno.getMedico().getConsultorio().getNumero() : null)
                .build();
    }

    // ===== CrearTurnoDto → ENTITY (para crear - campos básicos) =====
    public Turno toEntity(CrearTurnoDto dto) {
        if (dto == null) return null;

        Turno turno = new Turno();
        turno.setFecha(dto.getFecha());
        turno.setHoraProgramada(dto.getHoraProgramada());
        turno.setEstado("ESPERANDO"); // Estado inicial
        // Médico y Paciente se setean en el service después de buscarlos
        return turno;
    }

    // ===== Actualizar entidad desde CrearTurnoDto =====
    public void updateEntityFromDTO(CrearTurnoDto dto, Turno turno) {
        if (dto == null || turno == null) return;

        turno.setFecha(dto.getFecha());
        turno.setHoraProgramada(dto.getHoraProgramada());
        // Médico y Paciente se actualizan en el service
    }

    //Convierte TurnoDTO → CrearTurnoDto (para edición)

    public CrearTurnoDto toCrearDto(TurnoDTO dto) {
        if (dto == null) return null;

        CrearTurnoDto crearDto = new CrearTurnoDto();
        crearDto.setPacienteId(dto.getPacienteId());
        crearDto.setMedicoId(dto.getMedicoId());
        crearDto.setFecha(dto.getFecha());
        crearDto.setHoraProgramada(dto.getHoraInicio());
        return crearDto;
    }

    // ===== Helper para manejar activo =====
    private boolean getActivoValue(Turno turno) {
        try {
            return turno.isActivo();
        } catch (Exception e) {
            try {
                return Boolean.TRUE.equals(turno.getActivo());
            } catch (Exception e2) {
                return true;
            }
        }
    }
}