package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.dtos.TurnoNotificacionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Notifica a todos los clientes conectados sobre cambios en turnos
     */
    public void notificarCambioTurno(String tipo, Long turnoId, String mensaje) {
        log.info("Enviando notificación WebSocket: {} - Turno ID: {}", tipo, turnoId);

        TurnoNotificacionDTO notificacion = TurnoNotificacionDTO.builder()
                .tipo(tipo)
                .turnoId(turnoId)
                .mensaje(mensaje)
                .timestamp(LocalDateTime.now())
                .build();

        // Enviar notificación al topic que escuchan todos los clientes
        messagingTemplate.convertAndSend("/topic/turnos", notificacion);
    }

    /**
     * Notifica cuando se crea un turno
     */
    public void notificarTurnoCreado(Long turnoId) {
        notificarCambioTurno("TURNO_CREADO", turnoId, "Se ha creado un nuevo turno");
    }

    /**
     * Notifica cuando se actualiza un turno
     */
    public void notificarTurnoActualizado(Long turnoId) {
        notificarCambioTurno("TURNO_ACTUALIZADO", turnoId, "Se ha actualizado un turno");
    }

    /**
     * Notifica cuando se elimina un turno
     */
    public void notificarTurnoEliminado(Long turnoId) {
        notificarCambioTurno("TURNO_ELIMINADO", turnoId, "Se ha eliminado un turno");
    }
}
