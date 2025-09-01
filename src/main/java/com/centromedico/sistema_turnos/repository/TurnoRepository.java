package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno, Long> {


    List<Turno> findByFechaAndActivoTrueOrderByHoraProgramadaAsc(LocalDate fecha);
    List<Turno> findByMedicoIdAndFechaAndActivoTrue(Long medicoId, LocalDate fecha);
    List<Turno> findByPacienteIdAndFechaAndActivoTrue(Long pacienteId, LocalDate fecha);
    List<Turno> findByEstadoAndFechaAndActivoTrueOrderByHoraProgramadaAsc(String estado, LocalDate fecha);

    // Agregar este que es esencial para CRUD
    Optional<Turno> findByIdAndActivoTrue(Long id);

}
