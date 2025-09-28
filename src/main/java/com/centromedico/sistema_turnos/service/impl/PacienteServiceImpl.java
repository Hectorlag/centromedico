package com.centromedico.sistema_turnos.service.impl;


import com.centromedico.sistema_turnos.dtos.PacienteDTO;
import com.centromedico.sistema_turnos.mappers.PacienteMapper;
import com.centromedico.sistema_turnos.model.Paciente;
import com.centromedico.sistema_turnos.repository.PacienteRepository;
import com.centromedico.sistema_turnos.service.interfaces.PacienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    // ============== MÉTODOS DE LA INTERFAZ ==============

    @Override
    public List<PacienteDTO> listarActivos() {
        return pacienteRepository.findByActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PacienteDTO> buscarPorId(Long id) {
        return pacienteRepository.findByIdAndActivoTrue(id)
                .map(this::convertirADTO);
    }

    @Override
    public boolean existeYEstaActivo(Long id) {
        return pacienteRepository.existsByIdAndActivoTrue(id);
    }

    @Override
    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
    }

    @Override
    public Optional<PacienteDTO> obtenerPorDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    // ============== MÉTODOS CRUD ADICIONALES ==============

    public PacienteDTO crear(PacienteDTO pacienteDTO) {
        // Validar que no exista el DNI
        if (pacienteRepository.existsByDni(pacienteDTO.getDni())) {
            throw new RuntimeException("Ya existe un paciente con el DNI: " + pacienteDTO.getDni());
        }

        Paciente paciente = convertirAEntity(pacienteDTO);
        paciente.setActivo(true);
        paciente.setCreatedAt(LocalDateTime.now());

        Paciente guardado = pacienteRepository.save(paciente);
        return convertirADTO(guardado);
    }

    public PacienteDTO actualizar(Long id, PacienteDTO pacienteDTO) {
        Paciente paciente = pacienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Verificar DNI si cambió
        if (!paciente.getDni().equals(pacienteDTO.getDni()) &&
                pacienteRepository.existsByDni(pacienteDTO.getDni())) {
            throw new RuntimeException("Ya existe un paciente with el DNI: " + pacienteDTO.getDni());
        }

        actualizarCampos(paciente, pacienteDTO);
        paciente.setUpdatedAt(LocalDateTime.now());

        Paciente actualizado = pacienteRepository.save(paciente);
        return convertirADTO(actualizado);
    }

    public void eliminar(Long id) {
        Paciente paciente = pacienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        paciente.setActivo(false);
        paciente.setUpdatedAt(LocalDateTime.now());
        pacienteRepository.save(paciente);
    }

    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ============== MÉTODOS DE CONVERSIÓN ==============

    private PacienteDTO convertirADTO(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNombre(paciente.getNombre());
        dto.setApellido(paciente.getApellido());
        dto.setDni(paciente.getDni());
        dto.setEmail(paciente.getEmail());
        dto.setTelefono(paciente.getTelefono());
        dto.setObraSocial(paciente.getObraSocial());
        dto.setActivo(paciente.isActivo());
        return dto;
    }

    private Paciente convertirAEntity(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setDni(dto.getDni());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefono(dto.getTelefono());
        paciente.setObraSocial(dto.getObraSocial());
        return paciente;
    }

    private void actualizarCampos(Paciente paciente, PacienteDTO dto) {
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setDni(dto.getDni());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefono(dto.getTelefono());
        paciente.setObraSocial(dto.getObraSocial());
    }
}