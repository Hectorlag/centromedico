package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    // Buscar especialidades activas
    List<Especialidad> findByActivoTrue();

    // Buscar por nombre (case insensitive)
    Optional<Especialidad> findByNombreIgnoreCase(String nombre);

    // Buscar por nombre que contenga texto
    List<Especialidad> findByNombreContainingIgnoreCaseAndActivoTrue(String texto);

    // Especialidades con médicos activos
    @Query("SELECT e FROM Especialidad e WHERE e.activo = true AND EXISTS (SELECT m FROM Medico m WHERE m.especialidad = e AND m.activo = true)")
    List<Especialidad> findEspecialidadesConMedicosActivos();

    // Contar médicos por especialidad
    @Query("SELECT COUNT(m) FROM Medico m WHERE m.especialidad.id = :especialidadId AND m.activo = true")
    Long contarMedicosActivosPorEspecialidad(Long especialidadId);
}

