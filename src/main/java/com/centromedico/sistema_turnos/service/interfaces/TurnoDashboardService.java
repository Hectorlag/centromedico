package com.centromedico.sistema_turnos.service.interfaces;

import com.centromedico.sistema_turnos.dtos.TurnoDTO;

import java.util.List;

public interface TurnoDashboardService {

    //Obtiene turnos del día actual
    List<TurnoDTO> listarTurnosDelDia();


     //Obtiene turnos filtrados por estado

    List<TurnoDTO> listarPorEstado(String estado);


    //Cuenta turnos por estado

    long contarPorEstado(String estado);


    //Cuenta total de turnos del día

    long contarTurnosDelDia();
}
