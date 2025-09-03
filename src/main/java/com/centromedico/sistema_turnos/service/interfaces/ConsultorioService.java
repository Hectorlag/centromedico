package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.model.Consultorio;

import java.util.List;
import java.util.Optional;

public interface ConsultorioService {


    // CRUD básico
    List<Consultorio> findAll();
    Optional<Consultorio> findById(Long id);
    Consultorio save(Consultorio consultorio);
    void deleteById(Long id);
    void activar(Long id);
    void desactivar(Long id);

    // Búsquedas específicas
    List<Consultorio> findActivos();
    Optional<Consultorio> findByNombre(String nombre);
    List<Consultorio> findByPiso(String piso);
    List<Consultorio> findActivosOrdenados();

    // Consultorios por disponibilidad
    List<Consultorio> findDisponibles();
    List<Consultorio> findConMedicos();

    // Validaciones
    boolean existsByNombre(String nombre);
    boolean isDisponible(Long consultorioId);

    // Estadísticas
    long countActivos();
    long countDisponibles();
    List<String> getPisosDisponibles();
}
