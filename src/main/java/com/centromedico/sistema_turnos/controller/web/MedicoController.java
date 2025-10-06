package com.centromedico.sistema_turnos.controller.web;

import com.centromedico.sistema_turnos.dtos.MedicoDTO;
import com.centromedico.sistema_turnos.dtos.MedicoDetalleDTO;
import com.centromedico.sistema_turnos.service.interfaces.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", medicoService.listarActivos());
        model.addAttribute("titulo", "Lista de Médicos");
        return "medicos/lista";
    }

    @GetMapping("/{id}")
    public String verMedico(@PathVariable Long id, Model model) {
        MedicoDetalleDTO medico = medicoService.buscarDetallePorId(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        model.addAttribute("medico", medico);
        model.addAttribute("titulo", "Detalle del Médico");
        return "medicos/detalle";
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("medico", new MedicoDTO());
        model.addAttribute("titulo", "Nuevo Médico");
        model.addAttribute("accion", "crear");
        return "medicos/formulario";
    }

    @PostMapping("/nuevo")
    public String crearMedico(@Valid @ModelAttribute("medico") MedicoDTO medicoDTO,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Médico");
            model.addAttribute("accion", "crear");
            return "medicos/formulario";
        }

        MedicoDTO medicoCreado = medicoService.crear(medicoDTO);
        redirectAttributes.addFlashAttribute("success",
                "Médico creado exitosamente: Dr . Dra. " + medicoCreado.getNombreCompleto());

        return "redirect:/medicos/" + medicoCreado.getId();
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("medico", medicoService.buscarPorId(id).get());
        model.addAttribute("titulo", "Editar Médico");
        model.addAttribute("accion", "editar");
        return "medicos/formulario";
    }

    @PostMapping("/{id}/editar")
    public String actualizarMedico(@PathVariable Long id,
                                   @Valid @ModelAttribute("medico") MedicoDTO medicoDTO,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Médico");
            model.addAttribute("accion", "editar");
            return "medicos/formulario";
        }

        MedicoDTO medicoActualizado = medicoService.actualizar(id, medicoDTO);
        redirectAttributes.addFlashAttribute("success",
                "Médico actualizado exitosamente: Dr./Dra. " + medicoActualizado.getNombreCompleto());
        return "redirect:/medicos/" + id;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarMedico(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        MedicoDTO medico = medicoService.buscarPorId(id).get();
        medicoService.eliminar(id);
        redirectAttributes.addFlashAttribute("success",
                "Médico eliminado exitosamente: Dr./Dra. " + medico.getNombreCompleto());
        return "redirect:/medicos";
    }
}
