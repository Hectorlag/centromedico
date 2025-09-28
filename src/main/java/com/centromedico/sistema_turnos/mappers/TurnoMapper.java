package com.centromedico.sistema_turnos.mappers;

import com.centromedico.sistema_turnos.dtos.TurnoDTO;
import com.centromedico.sistema_turnos.model.Turno;
import org.springframework.stereotype.Component;


@Component
public class TurnoMapper {

    public TurnoDTO toDTO(Turno turno) {
        if (turno == null) return null;

        return TurnoDTO.builder()
                .id(turno.getId())
                .fecha(turno.getFecha())
                .horaInicio(turno.getHoraProgramada()) // ✅ Correcto: horaProgramada
                .estado(turno.getEstado())
                .observaciones(null) // ✅ Campo no existe en entidad por ahora
                .activo(getActivoValue(turno)) // ✅ Método helper para manejar activo
                // Datos del médico
                .medicoId(turno.getMedico() != null ? turno.getMedico().getId() : null)
                .medicoNombre(turno.getMedico() != null ? turno.getMedico().getNombre() : null)
                .medicoApellido(turno.getMedico() != null ? turno.getMedico().getApellido() : null)
                // Datos del paciente
                .pacienteId(turno.getPaciente() != null ? turno.getPaciente().getId() : null)
                .pacienteNombre(turno.getPaciente() != null ? turno.getPaciente().getNombre() : null)
                .pacienteApellido(turno.getPaciente() != null ? turno.getPaciente().getApellido() : null)
                .build();
    }

    public Turno toEntity(TurnoDTO dto) {
        if (dto == null) return null;

        Turno turno = new Turno();
        turno.setId(dto.getId());
        turno.setFecha(dto.getFecha());
        turno.setHoraProgramada(dto.getHoraInicio()); // ✅ Correcto
        turno.setEstado(dto.getEstado());
        // Note: medico y paciente se setean por separado en el service
        return turno;
    }

    // Método helper para manejar el campo activo
    private boolean getActivoValue(Turno turno) {
        try {
            // Opción 1: Si BaseEntity tiene isActivo()
            return turno.isActivo();
        } catch (Exception e) {
            try {
                // Opción 2: Si BaseEntity tiene getActivo()
                return Boolean.TRUE.equals(turno.getActivo());
            } catch (Exception e2) {
                // Opción 3: Por defecto true si no existe el campo
                return true;
            }
        }
    }
}
