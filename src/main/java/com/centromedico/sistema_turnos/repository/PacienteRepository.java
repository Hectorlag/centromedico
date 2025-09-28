package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.dtos.PacienteDTO;
import com.centromedico.sistema_turnos.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Métodos básicos para pacientes activos
    List<Paciente> findByActivoTrue();

    Optional<Paciente> findByIdAndActivoTrue(Long id);

    boolean existsByIdAndActivoTrue(Long id);

    // Buscar por DNI (útil para validaciones)
    Optional<PacienteDTO> findByDni(String dni);

    boolean existsByDni(String dni);
}
