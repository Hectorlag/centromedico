package com.centromedico.sistema_turnos.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.centromedico.sistema_turnos.dtos.EspecialidadDTO;
import com.centromedico.sistema_turnos.exception.BadRequestException;
import com.centromedico.sistema_turnos.exception.ResourceNotFoundException;
import com.centromedico.sistema_turnos.mappers.EspecialidadMapper;
import com.centromedico.sistema_turnos.model.Especialidad;
import com.centromedico.sistema_turnos.repository.EspecialidadRepository;
import com.centromedico.sistema_turnos.service.interfaces.EspecialidadService;
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
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;


    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadDTO> listarActivos(){
        return especialidadRepository.findByActivoTrue()
                .stream()
                .map(especialidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EspecialidadDTO> buscarPorId(Long id) {
        if(id == null || id <= 0){
            throw new BadRequestException("Id invalido" + id);
        }

        return  especialidadRepository.findByIdAndActivoTrue(id)
                .map(especialidadMapper::toDTO)
                .or(() -> {
                  throw  new BadRequestException("Especialidad no encontrada" + "con el id: " + id);
                });
    }

    @Override
    public boolean existeYEstaActivo(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return especialidadRepository.existsByIdAndActivoTrue(id);
    }

    @Override
    public Especialidad obtenerPorId(Long id) {
        if(id == null || id <= 0){
            throw new BadRequestException("ID invalido" + id);
        }

        return especialidadRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EspecialidadDTO> buscarPorNombre(String nombre) {
        if(!StringUtils.hasText(nombre)){
            return Optional.empty();
        }

        return especialidadRepository.findByNombre(nombre)
                .map(especialidadMapper::toDTO);
    }

    @Override
    public EspecialidadDTO crear(EspecialidadDTO especialidadDTO) {
        validarDatosObligatorios(especialidadDTO);

        if(especialidadRepository.existsByNombre(especialidadDTO.getNombre())){
            throw new BadRequestException("Ya existe una especilidad con ese nombre");
        }

        Especialidad especialidad = especialidadMapper.toEntity(especialidadDTO);
        especialidad.setActivo(true);
        especialidad.setCreatedAt(LocalDateTime.now());

        Especialidad guardado = especialidadRepository.save(especialidad);
        return especialidadMapper.toDTO(guardado);
    }

    @Override
    public EspecialidadDTO actualizar(Long id, EspecialidadDTO especialidadDTO) {
        validarDatosObligatorios(especialidadDTO);

        Especialidad especialidad = obtenerPorId(id);

        if (!especialidad.getNombre().equals(especialidadDTO.getNombre()) &&
                especialidadRepository.existsByNombre(especialidadDTO.getNombre())) {
            throw new BadRequestException("Ya existe una especialidad con el nombre: " + especialidadDTO.getNombre());
        }

        especialidadMapper.updateEntityFromDTO(especialidadDTO, especialidad);
        especialidad.setUpdatedAt(LocalDateTime.now());

        Especialidad actualizado = especialidadRepository.save(especialidad);
        return especialidadMapper.toDTO(actualizado);
    }


    @Override
    public void eliminar(Long id) {
    Especialidad especialidad = obtenerPorId(id);
    especialidad.setActivo(false);
    especialidad.setUpdatedAt(LocalDateTime.now());
    especialidadRepository.save(especialidad);
    }

    @Override
    public List<EspecialidadDTO> listarTodos() {
        return especialidadRepository.findAll()
                .stream()
                .map(especialidadMapper::toDTO)
                .collect(Collectors.toList());
    }


    //MÉTODO AUXILIAR
    private void validarDatosObligatorios(EspecialidadDTO especialidadDTO){
        if(especialidadDTO == null){
            throw new BadRequestException("Los datos de la especialidad no pueden ser nulos");
        }
        if(!StringUtils.hasText(especialidadDTO.getNombre())){
            throw new BadRequestException("El nombre de la especialidad es obligatorio");
        }
        if(especialidadDTO.getDuracionTurnoMinutos() != null &&
           especialidadDTO.getDuracionTurnoMinutos() <= 0){
           throw new BadRequestException("La duración del turno debe ser mayor a 0");
        }
    }
}
