package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.dtos.MedicoDTO;
import com.centromedico.sistema_turnos.dtos.MedicoDetalleDTO;
import com.centromedico.sistema_turnos.model.Medico;

import java.util.List;
import java.util.Optional;

public interface MedicoService {
    // Métodos de consulta
    List<MedicoDTO> listarActivos();

    Optional<MedicoDTO> buscarPorId(Long id);

    boolean existeYEstaActivo(Long id);

    Medico obtenerPorId(Long id);

    Optional<MedicoDTO> buscarPorDni(String dni);

    Optional<MedicoDTO> buscarPorMatricula(String matricula);

    // Métodos CRUD
    MedicoDTO crear(MedicoDTO medicoDTO);

    MedicoDTO actualizar(Long id, MedicoDTO medicoDTO);

    void eliminar(Long id);

    List<MedicoDTO> listarTodos();

    Optional<MedicoDetalleDTO> buscarDetallePorId(Long id);
}