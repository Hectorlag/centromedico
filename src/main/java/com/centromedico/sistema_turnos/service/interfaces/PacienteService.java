package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.dtos.PacienteDTO;
import com.centromedico.sistema_turnos.model.Paciente;

import java.util.List;
import java.util.Optional;

public interface PacienteService {

    List<PacienteDTO> listarActivos();
    Optional<PacienteDTO> buscarPorId(Long id);

    // Lo que necesitas para Turnos (por ID)
    boolean existeYEstaActivo(Long id);
    Paciente obtenerPorId(Long id); // Para crear turnos

    // ============== MÉTODOS CRUD ADICIONALES ==============

    PacienteDTO crear(PacienteDTO pacienteDTO);

    PacienteDTO actualizar(Long id, PacienteDTO pacienteDTO);

    void eliminar(Long id);

    List<PacienteDTO> listarTodos();
}
