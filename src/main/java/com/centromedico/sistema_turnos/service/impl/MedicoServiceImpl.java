package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.dtos.MedicoDTO;
import com.centromedico.sistema_turnos.mappers.MedicoMapper;
import com.centromedico.sistema_turnos.model.Medico;
import com.centromedico.sistema_turnos.repository.MedicoRepository;
import com.centromedico.sistema_turnos.service.interfaces.MedicoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service  // ✅ Esta anotación es crítica para que Spring lo detecte
@RequiredArgsConstructor
@Transactional
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    @Override
    @Transactional(readOnly = true)
    public boolean existeYEstaActivo(Long id) {
        log.debug("Verificando si médico con ID {} existe y está activo", id);
        return medicoRepository.findById(id)
                .map(medico -> {
                    try {
                        return medico.isActivo();
                    } catch (Exception e) {
                        return true; // Por defecto, si no tiene método isActivo()
                    }
                })
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Medico obtenerPorId(Long id) {
        log.debug("Obteniendo médico por ID: {}", id);
        return medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicoDTO> listarActivos() {
        log.debug("Listando médicos activos");
        return medicoRepository.findByActivoTrue()
                .stream()
                .map(medicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MedicoDTO> buscarPorId(Long id) {
        return Optional.empty();
    }

}