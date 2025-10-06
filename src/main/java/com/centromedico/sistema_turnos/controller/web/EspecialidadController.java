package com.centromedico.sistema_turnos.controller.web;

import com.centromedico.sistema_turnos.dtos.EspecialidadDTO;
import com.centromedico.sistema_turnos.model.Especialidad;
import com.centromedico.sistema_turnos.service.interfaces.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {


        private final EspecialidadService especialidadService;

        @GetMapping
        public String listarEspecialidades(Model model) {
            model.addAttribute("especialidades", especialidadService.listarActivos());
            model.addAttribute("titulo", "Lista de Especialidades");
            return "especialidades/lista";
        }

        // VER DETALLE (solo lectura)
        @GetMapping("/{id}")
        public String detalle(@PathVariable Long id, Model model) {
            EspecialidadDTO especialidad = especialidadService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
            model.addAttribute("especialidad", especialidad);
            return "especialidades/detalle";
        }

        // NUEVO - Mostrar formulario
        @GetMapping("/nuevo")
        public String mostrarFormularioCrear(Model model) {
            model.addAttribute("especialidad", new EspecialidadDTO());
            model.addAttribute("titulo", "Nueva Especialidad");
            model.addAttribute("accion", "crear");
            return "especialidades/formulario";
        }

        // NUEVO - Guardar
        @PostMapping("/nuevo")
        public String crearEspecialidad(@Valid @ModelAttribute("especialidad") EspecialidadDTO especialidadDTO,
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

        // EDITAR - Mostrar formulario
        @GetMapping("/{id}/editar")
        public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
            EspecialidadDTO especialidad = especialidadService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
            model.addAttribute("especialidad", especialidad);
            model.addAttribute("titulo", "Editar Especialidad");
            model.addAttribute("accion", "editar");
            return "especialidades/formulario";
        }

        // EDITAR - Guardar cambios
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

        // ELIMINAR
        @PostMapping("/{id}/eliminar")
        public String eliminarEspecialidad(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            EspecialidadDTO especialidad = especialidadService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
            especialidadService.eliminar(id);
            redirectAttributes.addFlashAttribute("success",
                    "Especialidad eliminada exitosamente: " + especialidad.getNombre());
            return "redirect:/especialidades";
        }

        // BUSCAR
        @GetMapping("/buscar")
        public String buscarEspecialidades(@RequestParam(required = false) String nombre,
                                           Model model) {
            model.addAttribute("especialidades", especialidadService.listarActivos());
            model.addAttribute("titulo", "Buscar Especialidades");
            model.addAttribute("nombre", nombre);
            return "especialidades/buscar";
        }


}
