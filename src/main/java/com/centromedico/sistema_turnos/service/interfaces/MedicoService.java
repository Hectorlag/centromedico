package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.dtos.MedicoDTO;
import com.centromedico.sistema_turnos.model.Medico;

import java.util.List;
import java.util.Optional;

public interface MedicoService {

    // CRUD Básico para tu feature actual
    List<MedicoDTO> listarActivos();
    Optional<MedicoDTO> buscarPorId(Long id);

    // Lo que necesitas para Turnos (por ID)
    boolean existeYEstaActivo(Long id);
    Medico obtenerPorId(Long id); // Para crear turnos
}
