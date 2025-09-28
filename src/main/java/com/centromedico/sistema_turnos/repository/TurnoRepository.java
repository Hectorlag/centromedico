package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

// ===== MÉTODOS PARA CRUD BÁSICO =====

    /**
     * Buscar todos los turnos activos
     * Para: listarActivos()
     */
    List<Turno> findByActivoTrue();

    /**
     * Buscar turno activo por ID
     * Para: buscarPorId() con validación de activo
     */
    Optional<Turno> findByIdAndActivoTrue(Long id);


    List<Turno> findByMedicoIdAndFechaAndHoraProgramadaAndActivoTrue(
            Long medicoId, LocalDate fecha, LocalTime horaProgramada);

    /**
     * Verificar si médico tiene otros turnos en la misma fecha
     * Para: validaciones adicionales en crearTurno()
     */
    List<Turno> findByMedicoIdAndFechaAndActivoTrue(Long medicoId, LocalDate fecha);

}