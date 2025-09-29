package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // ============== MÉTODOS BÁSICOS PARA MÉDICOS ACTIVOS ==============

    /**
     * Encuentra todos los médicos activos.
     */
    List<Medico> findByActivoTrue();

    /**
     * Encuentra un médico activo por su ID.
     */
    Optional<Medico> findByIdAndActivoTrue(Long id);

    /**
     * Verifica si existe un médico activo con el ID dado.
     */
    boolean existsByIdAndActivoTrue(Long id);

    // ============== BÚSQUEDAS POR DNI ==============

    /**
     * Busca un médico por DNI (independientemente de si está activo).
     */
    Optional<Medico> findByDni(String dni);

    /**
     * Verifica si existe un médico con el DNI dado.
     */
    boolean existsByDni(String dni);

    // ============== BÚSQUEDAS POR MATRÍCULA ==============

    /**
     * Busca un médico por matrícula (independientemente de si está activo).
     */
    Optional<Medico> findByMatricula(String matricula);

    /**
     * Verifica si existe un médico con la matrícula dada.
     */
    boolean existsByMatricula(String matricula);
}