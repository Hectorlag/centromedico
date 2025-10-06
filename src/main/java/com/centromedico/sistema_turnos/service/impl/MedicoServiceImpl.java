package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.dtos.MedicoDTO;
import com.centromedico.sistema_turnos.dtos.MedicoDetalleDTO;
import com.centromedico.sistema_turnos.exception.BadRequestException;
import com.centromedico.sistema_turnos.exception.ResourceNotFoundException;
import com.centromedico.sistema_turnos.mappers.ConsultorioMapper;
import com.centromedico.sistema_turnos.mappers.EspecialidadMapper;
import com.centromedico.sistema_turnos.mappers.MedicoMapper;
import com.centromedico.sistema_turnos.model.Consultorio;
import com.centromedico.sistema_turnos.model.Especialidad;
import com.centromedico.sistema_turnos.model.Medico;
import com.centromedico.sistema_turnos.repository.ConsultorioRepository;
import com.centromedico.sistema_turnos.repository.EspecialidadRepository;
import com.centromedico.sistema_turnos.repository.MedicoRepository;
import com.centromedico.sistema_turnos.service.interfaces.MedicoService;
import javassist.NotFoundException;
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
public class MedicoServiceImpl implements MedicoService {

    private final ConsultorioRepository consultorioRepository;
    private final EspecialidadRepository especialidadRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadMapper especialidadMapper;
    private final ConsultorioMapper consultorioMapper;
    private final MedicoMapper medicoMapper;

    // ============== MÉTODOS DE LA INTERFAZ ==============

    @Override
    @Transactional(readOnly = true)
    public List<MedicoDTO> listarActivos() {
        return medicoRepository.findByActivoTrue()
                .stream()
                .map(medicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicoDetalleDTO> buscarDetallePorId(Long id) {
        return medicoRepository.findById(id)
                .map(medico -> {
                    MedicoDetalleDTO dto = MedicoDetalleDTO.builder()
                            .id(medico.getId())
                            .nombre(medico.getNombre())
                            .apellido(medico.getApellido())
                            .dni(medico.getDni())
                            .matricula(medico.getMatricula())
                            .telefono(medico.getTelefono())
                            .activo(medico.getActivo())
                            .createdAt(medico.getCreatedAt())
                            .updatedAt(medico.getUpdatedAt())
                            .build();

                    // Mapear especialidad (SINGULAR)
                    if (medico.getEspecialidad() != null) {
                        dto.setEspecialidad(especialidadMapper.toDTO(medico.getEspecialidad()));
                    }

                    // Mapear consultorio (SINGULAR)
                    if (medico.getConsultorio() != null) {
                        dto.setConsultorio(consultorioMapper.toDTO(medico.getConsultorio()));
                    }

                    return dto;
                });
    }

    @Override
    public Optional<MedicoDTO> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("ID inválido: " + id);
        }

        return medicoRepository.findByIdAndActivoTrue(id)
                .map(medicoMapper::toDTO)
                .or(() -> {
                    throw new ResourceNotFoundException("Médico no encontrado con ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeYEstaActivo(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return medicoRepository.existsByIdAndActivoTrue(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Medico obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("ID de médico inválido: " + id);
        }

        return medicoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicoDTO> buscarPorDni(String dni) {
        if (!StringUtils.hasText(dni)) {
            return Optional.empty();
        }

        return medicoRepository.findByDni(dni)
                .map(medicoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicoDTO> buscarPorMatricula(String matricula) {
        if (!StringUtils.hasText(matricula)) {
            return Optional.empty();
        }

        return medicoRepository.findByMatricula(matricula)
                .map(medicoMapper::toDTO);
    }

    // ============== MÉTODOS CRUD ADICIONALES ==============

    @Override
    @Transactional
    public MedicoDTO crear(MedicoDTO medicoDTO) {
        validarDatosObligatorios(medicoDTO);

        if (medicoRepository.existsByDni(medicoDTO.getDni())) {
            throw new BadRequestException("Ya existe un médico con el DNI: " + medicoDTO.getDni());
        }

        if (medicoRepository.existsByMatricula(medicoDTO.getMatricula())) {
            throw new BadRequestException("Ya existe un médico con la matrícula: " + medicoDTO.getMatricula());
        }

        // Convertir DTO a entidad (sin relaciones)
        Medico medico = medicoMapper.toEntity(medicoDTO);

        // Asignar especialidad
        if (medicoDTO.getEspecialidadId() != null) {
            Especialidad especialidad = especialidadRepository.findById(medicoDTO.getEspecialidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada"));
            medico.setEspecialidad(especialidad);
        }

        // Asignar consultorio
        if (medicoDTO.getConsultorioId() != null) {
            Consultorio consultorio = consultorioRepository.findById(medicoDTO.getConsultorioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado"));
            medico.setConsultorio(consultorio);
        }

        medico.setActivo(true);
        medico.setCreatedAt(LocalDateTime.now());

        Medico guardado = medicoRepository.save(medico);
        return medicoMapper.toDTO(guardado);
    }

    @Override
    @Transactional
    public MedicoDTO actualizar(Long id, MedicoDTO medicoDTO) {
        // Buscar el médico existente
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + id));

        // Verificar DNI si cambió
        if (!medico.getDni().equals(medicoDTO.getDni())) {
            if (medicoRepository.existsByDni(medicoDTO.getDni())) {
                throw new BadRequestException("Ya existe un médico con el DNI: " + medicoDTO.getDni());
            }
        }

        // Verificar matrícula si cambió
        if (!medico.getMatricula().equals(medicoDTO.getMatricula())) {
            if (medicoRepository.existsByMatricula(medicoDTO.getMatricula())) {
                throw new BadRequestException("Ya existe un médico con la matrícula: " + medicoDTO.getMatricula());
            }
        }

        // Actualizar campos básicos usando el mapper
        medicoMapper.updateEntityFromDTO(medicoDTO, medico);

        // Actualizar especialidad
        if (medicoDTO.getEspecialidadId() != null) {
            Especialidad especialidad = especialidadRepository.findById(medicoDTO.getEspecialidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con ID: " + medicoDTO.getEspecialidadId()));
            medico.setEspecialidad(especialidad);
        } else {
            medico.setEspecialidad(null);
        }

        // Actualizar consultorio
        if (medicoDTO.getConsultorioId() != null) {
            Consultorio consultorio = consultorioRepository.findById(medicoDTO.getConsultorioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + medicoDTO.getConsultorioId()));
            medico.setConsultorio(consultorio);
        } else {
            medico.setConsultorio(null);
        }

        medico.setUpdatedAt(LocalDateTime.now());

        Medico actualizado = medicoRepository.save(medico);
        return medicoMapper.toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Medico medico = obtenerPorId(id);
        medico.setActivo(false);
        medico.setUpdatedAt(LocalDateTime.now());
        medicoRepository.save(medico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicoDTO> listarTodos() {
        return medicoRepository.findAll()
                .stream()
                .map(medicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ============== MÉTODOS DE CONVERSIÓN ==============

    private Medico convertirAEntity(MedicoDTO dto) {
        Medico medico = new Medico();
        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setDni(dto.getDni());
        medico.setMatricula(dto.getMatricula());
        medico.setTelefono(dto.getTelefono());
        // Nota: especialidad y consultorio se manejarán después
        return medico;
    }

    private void actualizarCampos(Medico medico, MedicoDTO dto) {
        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setDni(dto.getDni());
        medico.setMatricula(dto.getMatricula());
        medico.setTelefono(dto.getTelefono());
        // Nota: especialidad y consultorio se manejarán después
    }

    // ============== MÉTODOS DE VALIDACIÓN ==============

    private void validarDatosObligatorios(MedicoDTO medicoDTO) {
        if (medicoDTO == null) {
            throw new BadRequestException("Los datos del médico no pueden ser nulos");
        }

        if (!StringUtils.hasText(medicoDTO.getNombre())) {
            throw new BadRequestException("El nombre es obligatorio");
        }

        if (!StringUtils.hasText(medicoDTO.getApellido())) {
            throw new BadRequestException("El apellido es obligatorio");
        }

        if (!StringUtils.hasText(medicoDTO.getDni())) {
            throw new BadRequestException("El DNI es obligatorio");
        }

        // Validación básica de formato de DNI (argentino)
        if (!medicoDTO.getDni().matches("\\d{7,8}")) {
            throw new BadRequestException("El DNI debe tener entre 7 y 8 dígitos");
        }

        if (!StringUtils.hasText(medicoDTO.getMatricula())) {
            throw new BadRequestException("La matrícula es obligatoria");
        }

        // Validación básica de teléfono si está presente
        if (StringUtils.hasText(medicoDTO.getTelefono()) &&
                !medicoDTO.getTelefono().matches("\\d{10,15}")) {
            throw new BadRequestException("El teléfono debe tener entre 10 y 15 dígitos");
        }
    }
}