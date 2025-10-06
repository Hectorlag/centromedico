package com.centromedico.sistema_turnos.controller.web;

import com.centromedico.sistema_turnos.dtos.EspecialidadDTO;
import com.centromedico.sistema_turnos.service.interfaces.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public String listarEspecialidades(Model model) {
        model.addAttribute("especialidades", especialidadService.listarActivos());
        model.addAttribute("titulo", "Lista de especialidades");
        return "especialidades/lista";
    }

    @GetMapping("/{id}")
    public String verEspecialidad(@PathVariable Long id, Model model) {
        model.addAttribute("especialidad", especialidadService.buscarPorId(id).get());
        model.addAttribute("titulo", "Detalle de la Especialidad");
        return "especialidades/detalle";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("especialidad", new EspecialidadDTO());
        model.addAttribute("titulo", "Nueva Especialidad");
        model.addAttribute("accion", "crear");
        return "especialidades/formulario";
    }

    @PostMapping("/nuevo")
    public String crearEspecilidad(@Valid @ModelAttribute("especialidad") EspecialidadDTO especialidadDTO,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nueva Especialidad");
            model.addAttribute("accion", "crear");
            return "especialidades/formulario";
        }

        EspecialidadDTO especialidadCreada = especialidadService.crear(especialidadDTO);
        redirectAttributes.addFlashAttribute("success",
                "Especialidad creada exitosamente: " + especialidadCreada.getNombre());
        return "redirect:/especialidades/" + especialidadCreada.getId();
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("especialidad", especialidadService.buscarPorId(id).get());
        model.addAttribute("titulo", "Editar Especialidad");
        model.addAttribute("accion", "editar");
        return "especialidades/formulario";
    }

    @PostMapping("/{id}/editar")
    public String actualizarEspecialidad(@PathVariable Long id,
                                         @Valid @ModelAttribute("especialidad") EspecialidadDTO especialidadDTO,
                                         BindingResult result,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Especialidad");
            model.addAttribute("accion", "editar");
            return "especialidades/formulario";
        }

        EspecialidadDTO especialidadActualizada = especialidadService.actualizar(id, especialidadDTO);
        redirectAttributes.addFlashAttribute("success",
                "Especialidad actualizada exitosamente: " + especialidadActualizada.getNombre());
        return "redirect:/especialidades/" + id;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarEspecialidad(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        EspecialidadDTO especialidad = especialidadService.buscarPorId(id).get();
        especialidadService.eliminar(id);
        redirectAttributes.addFlashAttribute("success",
                "Especialidad eliminada exitosamente: " + especialidad.getNombre());
        return "redirect:/especialidades";
    }

    @GetMapping("/buscar")
    public String buscarEspecialidades(@RequestParam(required = false) String nombre,
                                       Model model) {

        model.addAttribute("especialidades", especialidadService.listarActivos());
        model.addAttribute("titulo", "Buscar Especialidades");
        model.addAttribute("nombre", nombre);
        return "especialidades/buscar";
    }




}
