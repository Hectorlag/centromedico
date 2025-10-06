package com.centromedico.sistema_turnos.controller.web;

import com.centromedico.sistema_turnos.dtos.ConsultorioDTO;
import com.centromedico.sistema_turnos.service.interfaces.ConsultorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/consultorios")
public class ConsultorioController {

    private final ConsultorioService consultorioService;

    @GetMapping
    public String listarConsultorios(Model model) {
        model.addAttribute("consultorios", consultorioService.listarActivos());
        model.addAttribute("titulo", "Lista de Consultorios");
        return "consultorios/lista";
    }

    @GetMapping("/{id}")
    public String verConsultorio(@PathVariable Long id, Model model) {
        model.addAttribute("consultorio", consultorioService.buscarPorId(id).get());
        model.addAttribute("titulo", "Detalle del Consultorio");
        return "consultorios/detalle";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("consultorio", new ConsultorioDTO());
        model.addAttribute("titulo", "Nuevo Consultorio");
        model.addAttribute("accion", "crear");
        return "consultorios/formulario";
    }

    @PostMapping("/nuevo")
    public String crearConsultorio(@Valid @ModelAttribute("consultorio") ConsultorioDTO consultorioDTO,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Consultorio");
            model.addAttribute("accion", "crear");
            return "consultorios/formulario";
        }

        ConsultorioDTO consultorioCreado = consultorioService.crear(consultorioDTO);
        redirectAttributes.addFlashAttribute("success",
                "Consultorio creado exitosamente: " + consultorioCreado.getNumero());
        return "redirect:/consultorios/" + consultorioCreado.getId();
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("consultorio", consultorioService.buscarPorId(id).get());
        model.addAttribute("titulo", "Editar Consultorio");
        model.addAttribute("accion", "editar");
        return "consultorios/formulario";
    }

    @PostMapping("/{id}/editar")
    public String actualizarConsultorio(@PathVariable Long id,
                                        @Valid @ModelAttribute("consultorio") ConsultorioDTO consultorioDTO,
                                        BindingResult result,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Consultorio");
            model.addAttribute("accion", "editar");
            return "consultorios/formulario";
        }

        ConsultorioDTO consultorioActualizado = consultorioService.actualizar(id, consultorioDTO);
        redirectAttributes.addFlashAttribute("success",
                "Consultorio actualizado exitosamente: " + consultorioActualizado.getNumero());
        return "redirect:/consultorios/" + id;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarConsultorio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ConsultorioDTO consultorio = consultorioService.buscarPorId(id).get();
        consultorioService.eliminar(id);
        redirectAttributes.addFlashAttribute("success",
                "Consultorio eliminado exitosamente: " + consultorio.getNumero());
        return "redirect:/consultorios";
    }

    @GetMapping("/buscar")
    public String buscarConsultorios(@RequestParam(required = false) String numero,
                                     @RequestParam(required = false) String piso,
                                     Model model) {

        model.addAttribute("consultorios", consultorioService.listarActivos());
        model.addAttribute("titulo", "Buscar Consultorios");
        model.addAttribute("numero", numero);
        model.addAttribute("piso", piso);
        return "consultorios/buscar";
    }
}

