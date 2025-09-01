package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {

    // Buscar consultorios activos
    List<Consultorio> findByActivoTrue();

    // Buscar por número
    Optional<Consultorio> findByNumero(String numero);

    // Buscar por piso
    List<Consultorio> findByPisoAndActivoTrue(String piso);

    // Consultorios ordenados por piso y número
    List<Consultorio> findByActivoTrueOrderByPisoAscNumeroAsc();

    // Consultorios disponibles (sin médicos asignados)
    @Query("SELECT c FROM Consultorio c WHERE c.activo = true AND NOT EXISTS (SELECT m FROM Medico m WHERE m.consultorio = c AND m.activo = true)")
    List<Consultorio> findConsultoriosDisponibles();

    // Consultorios con médicos
    @Query("SELECT c FROM Consultorio c WHERE c.activo = true AND EXISTS (SELECT m FROM Medico m WHERE m.consultorio = c AND m.activo = true)")
    List<Consultorio> findConsultoriosConMedicos();
}
