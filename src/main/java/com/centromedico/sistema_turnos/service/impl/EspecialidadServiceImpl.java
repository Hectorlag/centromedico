package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.model.Especialidad;
import com.centromedico.sistema_turnos.repository.EspecialidadRepository;
import com.centromedico.sistema_turnos.service.interfaces.EspecialidadService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    // ==================== CRUD BÁSICO ====================

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> findAll() {
        log.debug("Buscando todas las especialidades");
        return especialidadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Especialidad> findById(Long id) {
        log.debug("Buscando especialidad con ID: {}", id);
        return especialidadRepository.findById(id);
    }

    @Override
    public Especialidad save(Especialidad especialidad) {
        log.info("Guardando especialidad: {}", especialidad.getNombre());

        // Validaciones
        if (especialidad.getNombre() == null || especialidad.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad no puede estar vacío");
        }

        if (especialidad.getDuracionTurnoMinutos() == null || especialidad.getDuracionTurnoMinutos() <= 0) {
            throw new IllegalArgumentException("La duración del turno debe ser mayor a 0 minutos");
        }

        // Verificar si ya existe (solo para nuevos)
        if (especialidad.getId() == null && existsByNombre(especialidad.getNombre())) {
            throw new IllegalArgumentException("Ya existe una especialidad con ese nombre");
        }

        // Si es nueva, activarla usando BaseEntity
        if (especialidad.getId() == null) {
            especialidad.activar();
        }

        return especialidadRepository.save(especialidad);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Eliminando (borrado lógico) especialidad con ID: {}", id);

        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));

        // Verificar si tiene médicos activos
        if (tieneMedicosActivos(id)) {
            throw new IllegalStateException("No se puede eliminar una especialidad con médicos activos");
        }

        // BORRADO LÓGICO usando BaseEntity
        especialidad.desactivar();
        especialidadRepository.save(especialidad);
    }

    @Override
    public void activar(Long id) {
        log.info("Activando especialidad con ID: {}", id);
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));

        // Usar método de BaseEntity
        especialidad.activar();
        especialidadRepository.save(especialidad);
    }

    @Override
    public void desactivar(Long id) {
        log.info("Desactivando especialidad con ID: {}", id);
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));

        // Verificar si tiene médicos activos
        if (tieneMedicosActivos(id)) {
            throw new IllegalStateException("No se puede desactivar una especialidad con médicos activos");
        }

        // Usar método de BaseEntity
        especialidad.desactivar();
        especialidadRepository.save(especialidad);
    }

    // ==================== BÚSQUEDAS ESPECÍFICAS ====================

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> findActivas() {
        log.debug("Buscando especialidades activas");
        return especialidadRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Especialidad> findByNombre(String nombre) {
        log.debug("Buscando especialidad por nombre: {}", nombre);
        return especialidadRepository.findByNombreIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> findByTexto(String texto) {
        log.debug("Buscando especialidades que contengan: {}", texto);
        return especialidadRepository.findByNombreContainingIgnoreCaseAndActivoTrue(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> findConMedicosActivos() {
        log.debug("Buscando especialidades con médicos activos");
        return especialidadRepository.findEspecialidadesConMedicosActivos();
    }

    // ==================== VALIDACIONES ====================

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return especialidadRepository.findByNombreIgnoreCase(nombre).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneMedicosActivos(Long especialidadId) {
        return especialidadRepository.contarMedicosActivosPorEspecialidad(especialidadId) > 0;
    }

    // ==================== ESTADÍSTICAS ====================

    @Override
    @Transactional(readOnly = true)
    public long countActivas() {
        return especialidadRepository.findByActivoTrue().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countMedicosActivos(Long especialidadId) {
        return especialidadRepository.contarMedicosActivosPorEspecialidad(especialidadId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> findMasSolicitadas() {
        // Ordenar por cantidad de médicos (las que más médicos tienen = más solicitadas)
        return especialidadRepository.findEspecialidadesConMedicosActivos()
                .stream()
                .sorted((e1, e2) -> {
                    Long count1 = especialidadRepository.contarMedicosActivosPorEspecialidad(e1.getId());
                    Long count2 = especialidadRepository.contarMedicosActivosPorEspecialidad(e2.getId());
                    return count2.compareTo(count1); // Orden descendente
                })
                .collect(Collectors.toList());
    }
}
