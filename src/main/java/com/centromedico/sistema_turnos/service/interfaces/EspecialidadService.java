package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.model.Especialidad;

import java.util.List;
import java.util.Optional;

public interface EspecialidadService {

    // CRUD básico
    List<Especialidad> findAll();
    Optional<Especialidad> findById(Long id);
    Especialidad save(Especialidad especialidad);
    void deleteById(Long id);
    void activar(Long id);
    void desactivar(Long id);

    // Búsquedas específicas
    List<Especialidad> findActivas();
    Optional<Especialidad> findByNombre(String nombre);
    List<Especialidad> findByTexto(String texto);
    List<Especialidad> findConMedicosActivos();

    // Validaciones
    boolean existsByNombre(String nombre);
    boolean tieneMedicosActivos(Long especialidadId);

    // Estadísticas
    long countActivas();
    long countMedicosActivos(Long especialidadId);
    List<Especialidad> findMasSolicitadas();
}

