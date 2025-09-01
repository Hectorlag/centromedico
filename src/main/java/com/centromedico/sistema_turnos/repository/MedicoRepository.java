package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // Buscar médicos activos
    List<Medico> findByActivoTrue();

    // Buscar por DNI
    Optional<Medico> findByDni(String dni);

    // Buscar por matrícula
    Optional<Medico> findByMatricula(String matricula);

    // Buscar por especialidad
    List<Medico> findByEspecialidadIdAndActivoTrue(Long especialidadId);

    // Buscar por consultorio
    List<Medico> findByConsultorioIdAndActivoTrue(Long consultorioId);

    // Buscar por nombre o apellido (búsqueda flexible)
    @Query("SELECT m FROM Medico m WHERE m.activo = true AND (LOWER(m.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(m.apellido) LIKE LOWER(CONCAT('%', :texto, '%')))")
    List<Medico> buscarPorNombreOApellido(@Param("texto") String texto);

    // Médicos con turnos para fecha específica
    @Query("SELECT DISTINCT m FROM Medico m JOIN m.turnos t WHERE m.activo = true AND t.fecha = :fecha AND t.activo = true")
    List<Medico> findMedicosConTurnosPorFecha(@Param("fecha") LocalDate fecha);

    // Médicos sin turnos para fecha específica
    @Query("SELECT m FROM Medico m WHERE m.activo = true AND NOT EXISTS (SELECT t FROM Turno t WHERE t.medico = m AND t.fecha = :fecha AND t.activo = true)")
    List<Medico> findMedicosSinTurnosPorFecha(@Param("fecha") LocalDate fecha);

    // Contar pacientes atendidos por médico en fecha
    @Query("SELECT COUNT(t) FROM Turno t WHERE t.medico.id = :medicoId AND t.fecha = :fecha AND t.estado = 'FINALIZADO' AND t.activo = true")
    Long contarPacientesAtendidos(@Param("medicoId") Long medicoId, @Param("fecha") LocalDate fecha);
}
