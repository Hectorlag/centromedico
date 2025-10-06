package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.dtos.EspecialidadDTO;
import com.centromedico.sistema_turnos.model.Especialidad;

import java.util.List;
import java.util.Optional;

public interface EspecialidadService {

    // Métodos de consulta
    List<EspecialidadDTO> listarActivos();
    Optional<EspecialidadDTO> buscarPorId(Long id);
    boolean existeYEstaActivo(Long id);
    Especialidad obtenerPorId(Long id);
    Optional<EspecialidadDTO> buscarPorNombre(String nombre);

    // Métodos CRUD
    EspecialidadDTO crear(EspecialidadDTO especialidadDTO);
    EspecialidadDTO actualizar(Long id, EspecialidadDTO especialidadDTO);
    void eliminar(Long id);
    List<EspecialidadDTO> listarTodos();
}

