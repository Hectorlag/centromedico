package com.centromedico.sistema_turnos.controller.web;


import com.centromedico.sistema_turnos.dtos.PacienteDTO;
import com.centromedico.sistema_turnos.exception.ResourceNotFoundException;
import com.centromedico.sistema_turnos.service.interfaces.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Optional;

@Controller
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    // ============== MÉTODOS DE LISTADO ==============

    @GetMapping
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.listarActivos());
        model.addAttribute("titulo", "Lista de Pacientes");
        return "pacientes/lista";
    }

    @GetMapping("/todos")
    public String listarTodosLosPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.listarTodos());
        model.addAttribute("titulo", "Todos los Pacientes");
        model.addAttribute("mostrarInactivos", true);
        return "pacientes/lista";
    }

    // ============== MÉTODOS DE VISUALIZACIÓN ==============

    @GetMapping("/{id}")
    public String verPaciente(@PathVariable Long id, Model model) {
        Optional<PacienteDTO> paciente = pacienteService.buscarPorId(id);

        if (paciente.isEmpty()) {
            throw new ResourceNotFoundException("Paciente no encontrado con ID: " + id);
        }

        model.addAttribute("paciente", paciente.get());
        model.addAttribute("titulo", "Detalle del Paciente");
        return "pacientes/detalle";
    }

    // ============== MÉTODOS DE CREACIÓN ==============

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("paciente", new PacienteDTO());
        model.addAttribute("titulo", "Nuevo Paciente");
        model.addAttribute("accion", "crear");
        return "pacientes/formulario";
    }

    @PostMapping("/nuevo")
    public String crearPaciente(@Valid @ModelAttribute("paciente") PacienteDTO pacienteDTO,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Paciente");
            model.addAttribute("accion", "crear");
            return "pacientes/formulario";
        }

        PacienteDTO pacienteCreado = pacienteService.crear(pacienteDTO);
        redirectAttributes.addFlashAttribute("success",
                "Paciente creado exitosamente: " + pacienteCreado.getNombre() + " " + pacienteCreado.getApellido());
        return "redirect:/pacientes/" + pacienteCreado.getId();
    }

    // ============== MÉTODOS DE EDICIÓN ==============

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<PacienteDTO> paciente = pacienteService.buscarPorId(id);

        if (paciente.isEmpty()) {
            throw new ResourceNotFoundException("Paciente no encontrado con ID: " + id);
        }

        model.addAttribute("paciente", paciente.get());
        model.addAttribute("titulo", "Editar Paciente");
        model.addAttribute("accion", "editar");
        return "pacientes/formulario";
    }

    @PostMapping("/{id}/editar")
    public String actualizarPaciente(@PathVariable Long id,
                                     @Valid @ModelAttribute("paciente") PacienteDTO pacienteDTO,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Paciente");
            model.addAttribute("accion", "editar");
            return "pacientes/formulario";
        }

        PacienteDTO pacienteActualizado = pacienteService.actualizar(id, pacienteDTO);
        redirectAttributes.addFlashAttribute("success",
                "Paciente actualizado exitosamente: " + pacienteActualizado.getNombre() + " " + pacienteActualizado.getApellido());
        return "redirect:/pacientes/" + id;
    }

    // ============== MÉTODOS DE ELIMINACIÓN ==============

    @PostMapping("/{id}/eliminar")
    public String eliminarPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Obtener el paciente antes de eliminarlo para el mensaje
        Optional<PacienteDTO> paciente = pacienteService.buscarPorId(id);

        if (paciente.isEmpty()) {
            throw new ResourceNotFoundException("Paciente no encontrado con ID: " + id);
        }

        pacienteService.eliminar(id);
        redirectAttributes.addFlashAttribute("success",
                "Paciente eliminado exitosamente: " + paciente.get().getNombre() + " " + paciente.get().getApellido());

        return "redirect:/pacientes";
    }

    // ============== MÉTODOS DE BÚSQUEDA ==============

    @GetMapping("/buscar")
    public String buscarPacientes(@RequestParam(required = false) String dni, Model model) {
        if (dni != null && !dni.trim().isEmpty()) {
            Optional<PacienteDTO> paciente = pacienteService.obtenerPorDni(dni.trim());

            if (paciente.isPresent()) {
                return "redirect:/pacientes/" + paciente.get().getId();
            } else {
                model.addAttribute("error", "No se encontró ningún paciente con el DNI: " + dni);
            }
        }

        model.addAttribute("pacientes", pacienteService.listarActivos());
        model.addAttribute("titulo", "Buscar Pacientes");
        model.addAttribute("dni", dni);
        return "pacientes/buscar";
    }
}