package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.model.Turno;

import java.time.LocalDate;
import java.util.List;

public interface TurnoService {

    void llamarPaciente(Long turnoId);
    void iniciarAtencion(Long turnoId);
    void finalizarAtencion(Long turnoId);
    List<Turno> obtenerTurnosDelDia(LocalDate fecha);

    // CRUD básico
    Turno obtenerTurnoPorId(Long turnoId);
    void cancelarTurno(Long turnoId);

    // Consultas por entidad (usas los métodos del repo que ya tienes)
    List<Turno> obtenerTurnosPorMedico(Long medicoId, LocalDate fecha);
    List<Turno> obtenerTurnosPorPaciente(Long pacienteId, LocalDate fecha);
    List<Turno> obtenerTurnosPorEstado(String estado, LocalDate fecha);

    // Utilidades para el flujo de trabajo
    List<Turno> obtenerTurnosEnEspera(LocalDate fecha);
    List<Turno> obtenerTurnosEnEsperaPorMedico(Long medicoId, LocalDate fecha);
    int contarTurnosDelDia(LocalDate fecha);
    int contarTurnosPorMedico(Long medicoId, LocalDate fecha);

}
