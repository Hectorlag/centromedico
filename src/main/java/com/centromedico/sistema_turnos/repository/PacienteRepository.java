package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Buscar pacientes activos
    List<Paciente> findByActivoTrue();

    // Buscar por DNI (único)
    Optional<Paciente> findByDni(String dni);

    // Verificar si existe DNI
    boolean existsByDni(String dni);

    // Buscar por nombre completo (flexible)
    @Query("SELECT p FROM Paciente p WHERE p.activo = true AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :texto, '%')))")
    List<Paciente> buscarPorNombreOApellido(@Param("texto") String texto);

    // Buscar por obra social
    List<Paciente> findByObraSocialContainingIgnoreCaseAndActivoTrue(String obraSocial);

    // Pacientes con turnos para fecha específica
    @Query("SELECT DISTINCT p FROM Paciente p JOIN p.turnos t WHERE p.activo = true AND t.fecha = :fecha AND t.activo = true")
    List<Paciente> findPacientesConTurnosPorFecha(@Param("fecha") LocalDate fecha);


    // Últimos pacientes registrados
    List<Paciente> findTop10ByActivoTrueOrderByCreatedAtDesc();
}
