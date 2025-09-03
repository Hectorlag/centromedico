package com.centromedico.sistema_turnos.service.impl;

import com.centromedico.sistema_turnos.model.Consultorio;
import com.centromedico.sistema_turnos.repository.ConsultorioRepository;
import com.centromedico.sistema_turnos.service.interfaces.ConsultorioService;
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
public class ConsultorioServiceImpl implements ConsultorioService {

    private final ConsultorioRepository consultorioRepository;

    // ==================== CRUD BÁSICO ====================

    @Override
    @Transactional(readOnly = true)
    public List<Consultorio> findAll() {
        log.debug("Buscando todos los consultorios");
        return consultorioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Consultorio> findById(Long id) {
        log.debug("Buscando consultorio con ID: {}", id);
        return consultorioRepository.findById(id);
    }

    @Override
    public Consultorio save(Consultorio consultorio) {
        log.info("Guardando consultorio: {}", consultorio.getNombre());

        // Validaciones
        if (consultorio.getNombre() == null || consultorio.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del consultorio no puede estar vacío");
        }

        // Verificar si ya existe (solo para nuevos)
        if (consultorio.getId() == null && existsByNombre(consultorio.getNombre())) {
            throw new IllegalArgumentException("Ya existe un consultorio con ese nombre");
        }

        // Set timestamps si es nuevo
        if (consultorio.getId() == null) {
            consultorio.setCreatedAt(LocalDateTime.now());
            consultorio.setActivo(true);
        }
        consultorio.setUpdatedAt(LocalDateTime.now());

        return consultorioRepository.save(consultorio);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Eliminando (borrado lógico) consultorio con ID: {}", id);

        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado con ID: " + id));

        // Verificar si tiene médicos activos
        boolean tieneMedicosActivos = consultorio.getMedicos() != null &&
                consultorio.getMedicos().stream().anyMatch(medico -> medico.isActivo());

        if (tieneMedicosActivos) {
            throw new IllegalStateException("No se puede eliminar un consultorio con médicos activos");
        }

        // BORRADO LÓGICO usando BaseEntity
        consultorio.desactivar();
        consultorioRepository.save(consultorio);
    }

    @Override
    public void activar(Long id) {
        log.info("Activando consultorio con ID: {}", id);
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado con ID: " + id));

        consultorio.setActivo(true);
        consultorio.setUpdatedAt(LocalDateTime.now());
        consultorioRepository.save(consultorio);
    }

    @Override
    public void desactivar(Long id) {
        log.info("Desactivando consultorio con ID: {}", id);
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado con ID: " + id));

        // Verificar si tiene médicos activos
        boolean tieneMedicosActivos = consultorio.getMedicos() != null &&
                consultorio.getMedicos().stream().anyMatch(medico -> medico.isActivo());

        if (tieneMedicosActivos) {
            throw new IllegalStateException("No se puede desactivar un consultorio con médicos activos");
        }

        consultorio.setActivo(false);
        consultorio.setUpdatedAt(LocalDateTime.now());
        consultorioRepository.save(consultorio);
    }

    // ==================== BÚSQUEDAS ESPECÍFICAS ====================

    @Override
    @Transactional(readOnly = true)
    public List<Consultorio> findActivos() {
        log.debug("Buscando consultorios activos");
        return consultorioRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Consultorio> findByNombre(String nombre) {
        log.debug("Buscando consultorio por nombre: {}", nombre);
        // Nota: Necesitarías agregar este método al repo si usas "nombre"
        // return consultorioRepository.findByNombre(nombre);

        // O si usas "numero":
        return consultorioRepository.findByNumero(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Consultorio> findByPiso(String piso) {
        log.debug("Buscando consultorios en piso: {}", piso);
        return consultorioRepository.findByPisoAndActivoTrue(piso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Consultorio> findActivosOrdenados() {
        log.debug("Buscando consultorios activos ordenados");
        return consultorioRepository.findByActivoTrueOrderByPisoAscNumeroAsc();
    }

    // ==================== DISPONIBILIDAD ====================

    @Override
    @Transactional(readOnly = true)
    public List<Consultorio> findDisponibles() {
        log.debug("Buscando consultorios disponibles");
        return consultorioRepository.findConsultoriosDisponibles();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Consultorio> findConMedicos() {
        log.debug("Buscando consultorios con médicos");
        return consultorioRepository.findConsultoriosConMedicos();
    }

    // ==================== VALIDACIONES ====================

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        // Nota: Agregar al repo si necesitas este método
        return consultorioRepository.findByNumero(nombre).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDisponible(Long consultorioId) {
        return consultorioRepository.findById(consultorioId)
                .map(consultorio -> consultorio.isActivo() &&
                        (consultorio.getMedicos() == null || consultorio.getMedicos().isEmpty()))
                .orElse(false);
    }

    // ==================== ESTADÍSTICAS ====================

    @Override
    @Transactional(readOnly = true)
    public long countActivos() {
        return consultorioRepository.findByActivoTrue().size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countDisponibles() {
        return consultorioRepository.findConsultoriosDisponibles().size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getPisosDisponibles() {
        return consultorioRepository.findByActivoTrue()
                .stream()
                .map(Consultorio::getPiso)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
