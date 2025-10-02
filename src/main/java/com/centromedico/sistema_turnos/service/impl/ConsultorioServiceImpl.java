package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.dtos.ConsultorioDTO;
import com.centromedico.sistema_turnos.exception.BadRequestException;
import com.centromedico.sistema_turnos.exception.ResourceNotFoundException;
import com.centromedico.sistema_turnos.mappers.ConsultorioMapper;
import com.centromedico.sistema_turnos.model.Consultorio;
import com.centromedico.sistema_turnos.repository.ConsultorioRepository;
import com.centromedico.sistema_turnos.service.interfaces.ConsultorioService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ConsultorioServiceImpl implements ConsultorioService {

    private final ConsultorioRepository consultorioRepository;
    private final ConsultorioMapper consultorioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ConsultorioDTO> listarActivos() {
        return consultorioRepository.findByActivoTrue()
                .stream()
                .map(consultorioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConsultorioDTO> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("ID inválido: " + id);
        }

        return consultorioRepository.findByIdAndActivoTrue(id)
                .map(consultorioMapper::toDTO)
                .or(() -> {
                    throw new ResourceNotFoundException("Consultorio no encontrado con ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeYEstaActivo(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return consultorioRepository.existsByIdAndActivoTrue(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Consultorio obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("ID de consultorio inválido: " + id);
        }

        return consultorioRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsultorioDTO> buscarPorNumero(String numero) {
        if (!StringUtils.hasText(numero)) {
            return Optional.empty();
        }

        return consultorioRepository.findByNumero(numero)
                .map(consultorioMapper::toDTO);
    }

    @Override
    public ConsultorioDTO crear(ConsultorioDTO consultorioDTO) {
        validarDatosObligatorios(consultorioDTO);

        if (consultorioRepository.existsByNumero(consultorioDTO.getNumero())) {
            throw new BadRequestException("Ya existe un consultorio con el número: " + consultorioDTO.getNumero());
        }

        Consultorio consultorio = consultorioMapper.toEntity(consultorioDTO);
        consultorio.setActivo(true);
        consultorio.setCreatedAt(LocalDateTime.now());

        Consultorio guardado = consultorioRepository.save(consultorio);
        return consultorioMapper.toDTO(guardado);
    }

    @Override
    public ConsultorioDTO actualizar(Long id, ConsultorioDTO consultorioDTO) {
        validarDatosObligatorios(consultorioDTO);

        Consultorio consultorio = obtenerPorId(id);

        if (!consultorio.getNumero().equals(consultorioDTO.getNumero()) &&
                consultorioRepository.existsByNumero(consultorioDTO.getNumero())) {
            throw new BadRequestException("Ya existe un consultorio con el número: " + consultorioDTO.getNumero());
        }

        consultorioMapper.updateEntityFromDTO(consultorioDTO, consultorio);
        consultorio.setUpdatedAt(LocalDateTime.now());

        Consultorio actualizado = consultorioRepository.save(consultorio);
        return consultorioMapper.toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Consultorio consultorio = obtenerPorId(id);
        consultorio.setActivo(false);
        consultorio.setUpdatedAt(LocalDateTime.now());
        consultorioRepository.save(consultorio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultorioDTO> listarTodos() {
        return consultorioRepository.findAll()
                .stream()
                .map(consultorioMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validarDatosObligatorios(ConsultorioDTO consultorioDTO) {
        if (consultorioDTO == null) {
            throw new BadRequestException("Los datos del consultorio no pueden ser nulos");
        }

        if (!StringUtils.hasText(consultorioDTO.getNumero())) {
            throw new BadRequestException("El número del consultorio es obligatorio");
        }
    }


}
