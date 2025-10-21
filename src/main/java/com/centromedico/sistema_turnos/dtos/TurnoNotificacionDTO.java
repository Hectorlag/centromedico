package com.centromedico.sistema_turnos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoNotificacionDTO {

    private String tipo; // "TURNO_ACTUALIZADO", "TURNO_CREADO", "TURNO_ELIMINADO"
    private Long turnoId;
    private String mensaje;
    private LocalDateTime timestamp;
}
