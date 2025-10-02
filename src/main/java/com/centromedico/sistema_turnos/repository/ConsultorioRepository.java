package com.centromedico.sistema_turnos.repository;

import com.centromedico.sistema_turnos.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {

    //Médicos básicos para consultorios activos
   List<Consultorio> findByActivoTrue();
   Optional<Consultorio> findByIdAndActivoTrue(Long id);
   boolean existsByIdAndActivoTrue(Long id);

   //Buscar por número (para validaciones)
    Optional<Consultorio> findByNumero(String numero);
    boolean existsByNumero(String numero);

}
