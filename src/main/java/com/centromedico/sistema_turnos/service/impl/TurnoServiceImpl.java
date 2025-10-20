package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.dtos.CrearTurnoDto;
import com.centromedico.sistema_turnos.dtos.TurnoDTO;
import com.centromedico.sistema_turnos.mappers.TurnoMapper;
import com.centromedico.sistema_turnos.model.Medico;
import com.centromedico.sistema_turnos.model.Paciente;
import com.centromedico.sistema_turnos.model.Turno;
import com.centromedico.sistema_turnos.repository.TurnoRepository;
import com.centromedico.sistema_turnos.service.interfaces.MedicoService;
import com.centromedico.sistema_turnos.service.interfaces.PacienteService;
import com.centromedico.sistema_turnos.service.interfaces.TurnoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;
    private final TurnoMapper turnoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDTO> listarTodos() {
        log.debug("Listando todos los turnos");
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDTO> listarActivos() {
        log.debug("Listando turnos activos");
        return turnoRepository.findByActivoTrue()
                .stream()
                .map(turnoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TurnoDTO> buscarPorId(Long id) {
        log.debug("Buscando turno por ID: {}", id);
        return turnoRepository.findById(id)
                .map(turnoMapper::toDTO);
    }

    @Override
    public TurnoDTO crearTurno(CrearTurnoDto crearTurnoDto) {
        log.info("Creando turno para médico ID: {} y paciente ID: {}",
                crearTurnoDto.getMedicoId(), crearTurnoDto.getPacienteId());

        // 1. Validar que médico existe y está activo
        if (!medicoService.existeYEstaActivo(crearTurnoDto.getMedicoId())) {
            throw new EntityNotFoundException("Médico no encontrado o inactivo con ID: " + crearTurnoDto.getMedicoId());
        }

        // 2. Validar que paciente existe y está activo
        if (!pacienteService.existeYEstaActivo(crearTurnoDto.getPacienteId())) {
            throw new EntityNotFoundException("Paciente no encontrado o inactivo con ID: " + crearTurnoDto.getPacienteId());
        }

        // 3. Validar conflictos de horario
        List<Turno> conflictos = turnoRepository.findByMedicoIdAndFechaAndHoraProgramadaAndActivoTrue(
                crearTurnoDto.getMedicoId(),
                crearTurnoDto.getFecha(),
                crearTurnoDto.getHoraProgramada());

        if (!conflictos.isEmpty()) {
            throw new IllegalArgumentException("Ya existe un turno programado para ese médico en la fecha y hora especificada");
        }

        // 4. Obtener entidades para crear el turno
        Medico medico = medicoService.obtenerPorId(crearTurnoDto.getMedicoId());
        Paciente paciente = pacienteService.obtenerPorId(crearTurnoDto.getPacienteId());

        // 5. Crear turno
        Turno turno = new Turno();
        turno.setMedico(medico);
        turno.setPaciente(paciente);
        turno.setFecha(crearTurnoDto.getFecha());
        turno.setHoraProgramada(crearTurnoDto.getHoraProgramada());
        turno.setEstado("ESPERANDO"); // Estado inicial según tu lógica
        // Note: activo se maneja en BaseEntity automáticamente (true por defecto)

        Turno turnoGuardado = turnoRepository.save(turno);
        log.info("Turno creado exitosamente con ID: {}", turnoGuardado.getId());

        return turnoMapper.toDTO(turnoGuardado);
    }

    @Override
    public TurnoDTO actualizarTurno(Long id, CrearTurnoDto crearTurnoDto) {
        log.info("Actualizando turno ID: {}", id);

        Turno turnoExistente = turnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turno no encontrado con ID: " + id));

        // Validar que puede ser modificado
        if (!puedeSerModificado(id)) {
            throw new IllegalStateException("El turno no puede ser modificado en su estado actual: " + turnoExistente.getEstado());
        }

        // Validar médico y paciente
        if (!medicoService.existeYEstaActivo(crearTurnoDto.getMedicoId())) {
            throw new EntityNotFoundException("Médico no encontrado o inactivo con ID: " + crearTurnoDto.getMedicoId());
        }

        if (!pacienteService.existeYEstaActivo(crearTurnoDto.getPacienteId())) {
            throw new EntityNotFoundException("Paciente no encontrado o inactivo con ID: " + crearTurnoDto.getPacienteId());
        }

        // Validar conflictos de horario (excluyendo el turno actual)
        List<Turno> conflictos = turnoRepository.findByMedicoIdAndFechaAndHoraProgramadaAndActivoTrue(
                crearTurnoDto.getMedicoId(),
                crearTurnoDto.getFecha(),
                crearTurnoDto.getHoraProgramada());

        // Excluir el turno actual de los conflictos
        conflictos = conflictos.stream()
                .filter(turno -> !turno.getId().equals(id))
                .collect(Collectors.toList());

        if (!conflictos.isEmpty()) {
            throw new IllegalArgumentException("Ya existe un turno programado para ese médico en la fecha y hora especificada");
        }

        // Actualizar datos
        turnoExistente.setMedico(medicoService.obtenerPorId(crearTurnoDto.getMedicoId()));
        turnoExistente.setPaciente(pacienteService.obtenerPorId(crearTurnoDto.getPacienteId()));
        turnoExistente.setFecha(crearTurnoDto.getFecha());
        turnoExistente.setHoraProgramada(crearTurnoDto.getHoraProgramada());

        Turno turnoActualizado = turnoRepository.save(turnoExistente);
        log.info("Turno actualizado exitosamente: {}", id);

        return turnoMapper.toDTO(turnoActualizado);
    }

    @Override
    public void eliminarTurno(Long id) {
        log.info("Eliminando turno ID: {}", id);

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turno no encontrado con ID: " + id));

        // Soft delete - desactivar el turno
        try {
            turno.setActivo(false);
        } catch (Exception e) {
            // Si no existe setActivo(), usar el método de BaseEntity que tengas
            log.warn("No se pudo usar setActivo(), usando método alternativo");
            // turno.desactivar(); // o el método que tengas en BaseEntity
        }

        turnoRepository.save(turno);
        log.info("Turno eliminado (desactivado): {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeTurno(Long id) {
        return turnoRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeSerModificado(Long id) {
        return turnoRepository.findById(id)
                .map(turno -> {
                    String estado = turno.getEstado();
                    // Solo se puede modificar si está ESPERANDO (según tu lógica de estados)
                    return "ESPERANDO".equals(estado);
                })
                .orElse(false);
    }


}


