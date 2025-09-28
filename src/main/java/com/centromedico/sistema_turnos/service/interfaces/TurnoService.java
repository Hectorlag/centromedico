package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.dtos.CrearTurnoDto;
import com.centromedico.sistema_turnos.dtos.TurnoDTO;
import com.centromedico.sistema_turnos.model.Turno;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TurnoService {

    List<TurnoDTO> listarTodos();

    /**
     * Lista solo los turnos activos
     */
    List<TurnoDTO> listarActivos();

    /**
     * Busca un turno por su ID
     */
    Optional<TurnoDTO> buscarPorId(Long id);

    /**
     * Crea un nuevo turno con validaciones
     */
    TurnoDTO crearTurno(CrearTurnoDto crearTurnoDto);

    /**
     * Actualiza un turno existente
     */
    TurnoDTO actualizarTurno(Long id, CrearTurnoDto crearTurnoDto);

    /**
     * Elimina un turno (soft delete)
     */
    void eliminarTurno(Long id);

    /**
     * Verifica si existe un turno con el ID especificado
     */
    boolean existeTurno(Long id);

    /**
     * Valida que el turno pueda ser modificado (no está finalizado/cancelado)
     */
    boolean puedeSerModificado(Long id);

}
