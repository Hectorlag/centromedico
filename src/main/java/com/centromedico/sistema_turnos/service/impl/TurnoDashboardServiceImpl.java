package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.dtos.TurnoDTO;
import com.centromedico.sistema_turnos.mappers.TurnoMapper;
import com.centromedico.sistema_turnos.repository.TurnoRepository;
import com.centromedico.sistema_turnos.service.interfaces.TurnoDashboardService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TurnoDashboardServiceImpl implements TurnoDashboardService {

    private final TurnoRepository turnoRepository;
    private final TurnoMapper turnoMapper;

    @Override
    public List<TurnoDTO> listarTurnosDelDia() {
        LocalDate hoy = LocalDate.now();
        log.debug("Listando turnos del día: {}", hoy);

        return turnoRepository.findByActivoTrue()
                .stream()
                .filter(turno -> hoy.equals(turno.getFecha()))
                .map(turnoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TurnoDTO> listarPorEstado(String estado) {
        log.debug("Listando turnos por estado: {}", estado);

        return turnoRepository.findByActivoTrue()
                .stream()
                .filter(turno -> estado.equals(turno.getEstado()))
                .map(turnoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long contarPorEstado(String estado) {
        log.debug("Contando turnos por estado: {}", estado);

        return turnoRepository.findByActivoTrue()
                .stream()
                .filter(turno -> estado.equals(turno.getEstado()))
                .count();
    }

    @Override
    public long contarTurnosDelDia() {
        LocalDate hoy = LocalDate.now();
        log.debug("Contando turnos del día: {}", hoy);

        return turnoRepository.findByActivoTrue()
                .stream()
                .filter(turno -> hoy.equals(turno.getFecha()))
                .count();
    }

    @Override
    public List<TurnoDTO> listarParaTurnero() {
        log.debug("Listando turnos para turnero (TV)");

        List<TurnoDTO> todosLosTurnos = turnoRepository.findByActivoTrue()
                .stream()
                .map(turnoMapper::toDTO)
                .collect(Collectors.toList());

        // Turnos ATENDIENDO (máximo 4)
        List<TurnoDTO> turnosAtendiendo = todosLosTurnos.stream()
                .filter(t -> "ATENDIENDO".equals(t.getEstado()))
                .limit(4)
                .collect(Collectors.toList());

        // Turnos FINALIZADOS (máximo 4, más recientes primero)
        List<TurnoDTO> turnosFinalizados = todosLosTurnos.stream()
                .filter(t -> "FINALIZADO".equals(t.getEstado()))
                .sorted((t1, t2) -> t2.getHoraInicio().compareTo(t1.getHoraInicio()))
                .limit(4)
                .collect(Collectors.toList());

        // Combinar
        List<TurnoDTO> resultado = new ArrayList<>();
        resultado.addAll(turnosAtendiendo);
        resultado.addAll(turnosFinalizados);

        return resultado;
    }
}
