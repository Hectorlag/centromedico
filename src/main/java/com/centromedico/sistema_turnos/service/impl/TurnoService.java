package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.exception.BadRequestException;
import com.centromedico.sistema_turnos.exception.ResourceNotFoundException;
import com.centromedico.sistema_turnos.model.Turno;
import com.centromedico.sistema_turnos.repository.TurnoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TurnoService implements com.centromedico.sistema_turnos.service.interfaces.TurnoService {

    private final TurnoRepository turnoRepository;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @Override
    public void llamarPaciente(Long turnoId) {
        Turno turno = obtenerTurnoPorId(turnoId);

        if (!turno.estaEsperando()) {
            throw new BadRequestException("Solo se puede llamar a pacientes esperando");
        }

        turno.cambiarEstadoALlamado();
        turnoRepository.save(turno);
    }

    @Override
    public void iniciarAtencion(Long turnoId) {
        Turno turno = obtenerTurnoPorId(turnoId);

        if (!"LLAMADO".equals(turno.getEstado())) {
            throw new BadRequestException("Solo se puede iniciar atención si el paciente fue llamado. Estado actual: " + turno.getEstado());
        }

        turno.cambiarEstadoAAtendiendo();
        turnoRepository.save(turno);
    }

    @Override
    public void finalizarAtencion(Long turnoId) {
        Turno turno = obtenerTurnoPorId(turnoId);

        if (!"ATENDIENDO".equals(turno.getEstado())) {
            throw new BadRequestException("Solo se puede finalizar si está siendo atendido. Estado actual: " + turno.getEstado());
        }

        turno.cambiarEstadoAFinalizado();
        turnoRepository.save(turno);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> obtenerTurnosDelDia(LocalDate fecha) {
        return turnoRepository.findByFechaAndActivoTrueOrderByHoraProgramadaAsc(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public Turno obtenerTurnoPorId(Long turnoId) {
        return turnoRepository.findByIdAndActivoTrue(turnoId)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado: " + turnoId));
    }

    @Override
    public void cancelarTurno(Long turnoId) {
        Turno turno = obtenerTurnoPorId(turnoId);

        if ("FINALIZADO".equals(turno.getEstado())) {
            throw new BadRequestException("No se puede cancelar un turno finalizado");
        }

        turno.cambiarEstadoACancelado();
        turnoRepository.save(turno);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> obtenerTurnosPorMedico(Long medicoId, LocalDate fecha) {
        return turnoRepository.findByMedicoIdAndFechaAndActivoTrue(medicoId, fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> obtenerTurnosPorPaciente(Long pacienteId, LocalDate fecha) {
        return turnoRepository.findByPacienteIdAndFechaAndActivoTrue(pacienteId, fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> obtenerTurnosPorEstado(String estado, LocalDate fecha) {
        return turnoRepository.findByEstadoAndFechaAndActivoTrueOrderByHoraProgramadaAsc(estado, fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> obtenerTurnosEnEspera(LocalDate fecha) {
        return obtenerTurnosPorEstado("ESPERANDO", fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> obtenerTurnosEnEsperaPorMedico(Long medicoId, LocalDate fecha) {
        return obtenerTurnosPorMedico(medicoId, fecha)
                .stream()
                .filter(turno -> "ESPERANDO".equals(turno.getEstado()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public int contarTurnosDelDia(LocalDate fecha) {
        return obtenerTurnosDelDia(fecha).size();
    }

    @Override
    @Transactional(readOnly = true)
    public int contarTurnosPorMedico(Long medicoId, LocalDate fecha) {
        return obtenerTurnosPorMedico(medicoId, fecha).size();
    }
}




