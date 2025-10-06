package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    // Métodos básicos para especialidades activas
    List<Especialidad> findByActivoTrue();
    Optional<Especialidad> findByIdAndActivoTrue(Long id);
    boolean existsByIdAndActivoTrue(Long id);

    // Buscar por nombre (útil para validaciones)
    Optional<Especialidad> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}

