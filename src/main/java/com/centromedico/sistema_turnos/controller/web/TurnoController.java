package com.centromedico.sistema_turnos.controller.web;

import com.centromedico.sistema_turnos.dtos.CrearTurnoDto;
import com.centromedico.sistema_turnos.dtos.TurnoDTO;
import com.centromedico.sistema_turnos.mappers.TurnoMapper;
import com.centromedico.sistema_turnos.service.interfaces.MedicoService;
import com.centromedico.sistema_turnos.service.interfaces.PacienteService;
import com.centromedico.sistema_turnos.service.interfaces.TurnoDashboardService;
import com.centromedico.sistema_turnos.service.interfaces.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;
    private final TurnoMapper turnoMapper;
    private final TurnoDashboardService turnoDashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.debug("Mostrando dashboard de turnos");

        // Obtener todos los turnos activos del día
        List<TurnoDTO> turnosActivos = turnoService.listarActivos();

        // Filtrar por estado
        List<TurnoDTO> turnosEspera = turnosActivos.stream()
                .filter(t -> "ESPERANDO".equals(t.getEstado()))
                .collect(Collectors.toList());

        List<TurnoDTO> turnosAtendiendo = turnosActivos.stream()
                .filter(t -> "ATENDIENDO".equals(t.getEstado()))
                .collect(Collectors.toList());

        List<TurnoDTO> turnosFinalizados = turnosActivos.stream()
                .filter(t -> "FINALIZADO".equals(t.getEstado()))
                .collect(Collectors.toList());

        // Agregar al modelo
        model.addAttribute("totalTurnos", turnosActivos.size());
        model.addAttribute("turnosEnEspera", turnosEspera.size());
        model.addAttribute("turnosAtendiendo", turnosAtendiendo.size());
        model.addAttribute("turnosFinalizados", turnosFinalizados.size());
        model.addAttribute("listaTurnosEspera", turnosEspera);
        model.addAttribute("listaTurnosAtendiendo", turnosAtendiendo);

        // Fecha actual formateada
        model.addAttribute("fechaFormateada",
                LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"))));

        return "dashboard";
    }

    /**
     * Página principal - Redirige al dashboard
     * GET /turnos
     */
    @GetMapping
    public String inicio() {
        log.debug("Redirigiendo a dashboard");
        return "redirect:/turnos/dashboard";
    }

    /**
     * Listar turnos básico
     * GET /turnos/lista
     */
    @GetMapping("/lista")
    public String listarTurnos(Model model) {
        log.debug("Listando turnos activos");

        List<TurnoDTO> turnos = turnoService.listarActivos();
        model.addAttribute("turnos", turnos);
        model.addAttribute("titulo", "Turnos Activos");

        return "turnos/lista"; // -> templates/turnos/lista.html
    }

    @GetMapping("/{id}")
    public String verTurno(@PathVariable Long id, Model model) {
        TurnoDTO turnoDTO = turnoService.buscarPorId(id).get();
        model.addAttribute("turno", turnoDTO);
        model.addAttribute("titulo", "Detalle del Turno");
        return "turnos/detalle";
    }

    //Mostrar formulario para un nuevo turno
    @GetMapping("/nuevo")
    public String mostrarFromularioCrear(Model model) {
        model.addAttribute("turno", new CrearTurnoDto());
        model.addAttribute("medicos", medicoService.listarActivos());
        model.addAttribute("pacientes", pacienteService.listarActivos());
        model.addAttribute("titulo", "Nuevo Turno");
        model.addAttribute("accion", "crear");
        return "turnos/formulario";
    }

    //Envío de datos al servicio
    @PostMapping("/nuevo")
    public String crearTurno(@Valid @ModelAttribute("turno") CrearTurnoDto crearTurnoDto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("medicos", medicoService.listarActivos());
            model.addAttribute("pacientes", pacienteService.listarActivos());
            model.addAttribute("titulo", "Nuevo Turno");
            model.addAttribute("accion", "crear");
            return "turnos/formulario";
        }

        TurnoDTO turnoCreado = turnoService.crearTurno(crearTurnoDto);
        redirectAttributes.addFlashAttribute("success," +
                "Turno creado exitosamente para " + turnoCreado.getPacienteNombreCompleto());
        return "redirect:/turnos/" + turnoCreado.getId();
    }

    //Mostrar formulario para un actualizar un turno
    @GetMapping("/{id}/editar")
    public String actualizarTurno(@Valid @PathVariable Long id, Model model){

        TurnoDTO turnoDTO = turnoService.buscarPorId(id).get();
        model.addAttribute("turno", turnoMapper.toCrearDto(turnoDTO));//Limpio
        model.addAttribute("turnoId", id);
        model.addAttribute("medicos", medicoService.listarActivos());
        model.addAttribute("pacientes", pacienteService.listarActivos());
        model.addAttribute("titulo", "Editar Turno");
        model.addAttribute("accion", "editar");
        return "turnos/formulario";
    }

    //Actualizar turno
    @PostMapping("/{id}/editar")
    public String editarTurno(@PathVariable Long id, @ModelAttribute("turno") CrearTurnoDto crearTurnoDto,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            model.addAttribute("turnoId", id);
            model.addAttribute("medicos", medicoService.listarActivos());
            model.addAttribute("pacientes", pacienteService.listarActivos());
            model.addAttribute("titulo", "Editar Turno");
            model.addAttribute("accion", "editar");
            return "turnos/formulario";
        }

        turnoService.actualizarTurno(id, crearTurnoDto);
        redirectAttributes.addFlashAttribute("success", "Turno actualizado exitosamente");
        return "redirect:/turnos/" + id;
    }

    //Eliminar turno
    @PostMapping("/{id}/eliminar")
    public String eliminarTurno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        turnoService.eliminarTurno(id);
        redirectAttributes.addFlashAttribute("success", "Turno eliminado exitosamente");
        return "redirect:/turnos/lista";
    }

    /**
     * Vista de turnero para TV
     * GET /turnos/turnero
     */
    @GetMapping("/turnero")
    public String turnero(Model model) {
        log.debug("Mostrando vista de turnero para TV");
        model.addAttribute("turnos", turnoDashboardService.listarParaTurnero());
        return "turnos/turnero";
    }


}

