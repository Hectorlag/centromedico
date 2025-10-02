package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.dtos.ConsultorioDTO;
import com.centromedico.sistema_turnos.model.Consultorio;

import java.util.List;
import java.util.Optional;

public interface ConsultorioService {

    // Métodos de consulta
    List<ConsultorioDTO> listarActivos();
    Optional<ConsultorioDTO> buscarPorId(Long id);
    boolean existeYEstaActivo(Long id);
    Consultorio obtenerPorId(Long id);
    Optional<ConsultorioDTO> buscarPorNumero(String numero);

    //Métodos CRUD
    ConsultorioDTO crear(ConsultorioDTO consultorioDTO);
    ConsultorioDTO actualizar(Long id, ConsultorioDTO consultorioDTO);
    void eliminar(Long id);
    List<ConsultorioDTO> listarTodos();

}
