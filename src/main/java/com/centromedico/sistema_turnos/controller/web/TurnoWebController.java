package com.centromedico.sistema_turnos.controller.web;

import com.centromedico.sistema_turnos.model.Turno;
import com.centromedico.sistema_turnos.service.impl.TurnoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping("/turnos")
@RequiredArgsConstructor
public class TurnoWebController {

    private final TurnoService turnoService;

    // ==================== PÁGINAS PRINCIPALES ====================

    @GetMapping("/dashboard")
    public String dashboard(Model model, Locale locale) {

        // Obtener fecha actual
        LocalDate hoy = LocalDate.now();

        // Formatear fecha para mostrar en español
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", locale);
        String fechaFormateada = hoy.format(formatter);

        // Tu lógica existente...
        List<Turno> turnosHoy = turnoService.obtenerTurnosDelDia(hoy);
        List<Turno> turnosEnEspera = turnoService.obtenerTurnosEnEspera(hoy);
        List<Turno> turnosAtendiendo = turnoService.obtenerTurnosPorEstado("ATENDIENDO", hoy);
        List<Turno> turnosFinalizados = turnoService.obtenerTurnosPorEstado("FINALIZADO", hoy);

        // Agregar atributos al modelo
        model.addAttribute("fecha", hoy);
        model.addAttribute("fechaFormateada", fechaFormateada); // Nueva línea
        model.addAttribute("totalTurnos", turnosHoy.size());
        model.addAttribute("turnosEnEspera", turnosEnEspera.size());
        model.addAttribute("turnosAtendiendo", turnosAtendiendo.size());
        model.addAttribute("turnosFinalizados", turnosFinalizados.size());
        model.addAttribute("listaTurnosEspera", turnosEnEspera);
        model.addAttribute("listaTurnosAtendiendo", turnosAtendiendo);

        log.debug("Dashboard cargado para fecha: {}", hoy);

        return "dashboard";
    }
    /**
     * Lista completa de turnos por fecha
     */
    @GetMapping
    public String listarTurnos(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        List<Turno> turnos = turnoService.obtenerTurnosDelDia(fecha);

        model.addAttribute("fecha", fecha);
        model.addAttribute("turnos", turnos);
        model.addAttribute("totalTurnos", turnos.size());

        log.debug("Lista de turnos cargada para fecha: {}", fecha);
        return "turnos/list";
    }

    /**
     * Detalle de un turno específico
     */
    @GetMapping("/{id}")
    public String verTurno(@PathVariable Long id, Model model) {
        Turno turno = turnoService.obtenerTurnoPorId(id);

        model.addAttribute("turno", turno);

        log.debug("Detalle de turno cargado: {}", id);
        return "turnos/detail";
    }

    // ==================== FLUJO DE TURNOS ====================

    /**
     * Llamar al siguiente paciente
     */
    @PostMapping("/{id}/llamar")
    public String llamarPaciente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        turnoService.llamarPaciente(id);

        redirectAttributes.addFlashAttribute("success", "Paciente llamado exitosamente");
        log.info("Paciente llamado para turno ID: {}", id);

        return "redirect:/turnos/dashboard";
    }

    /**
     * Iniciar atención médica
     */
    @PostMapping("/{id}/iniciar")
    public String iniciarAtencion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        turnoService.iniciarAtencion(id);

        redirectAttributes.addFlashAttribute("success", "Atención iniciada exitosamente");
        log.info("Atención iniciada para turno ID: {}", id);

        return "redirect:/turnos/dashboard";
    }

    /**
     * Finalizar atención médica
     */
    @PostMapping("/{id}/finalizar")
    public String finalizarAtencion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        turnoService.finalizarAtencion(id);

        redirectAttributes.addFlashAttribute("success", "Atención finalizada exitosamente");
        log.info("Atención finalizada para turno ID: {}", id);

        return "redirect:/turnos/dashboard";
    }

    /**
     * Cancelar turno
     */
    @PostMapping("/{id}/cancelar")
    public String cancelarTurno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        turnoService.cancelarTurno(id);

        redirectAttributes.addFlashAttribute("success", "Turno cancelado exitosamente");
        log.info("Turno cancelado ID: {}", id);

        return "redirect:/turnos";
    }

    // ==================== VISTAS ESPECÍFICAS ====================

    /**
     * Turnos en espera - Vista para secretarias
     */
    @GetMapping("/esperando")
    public String turnosEnEspera(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        List<Turno> turnosEnEspera = turnoService.obtenerTurnosEnEspera(fecha);

        model.addAttribute("fecha", fecha);
        model.addAttribute("turnos", turnosEnEspera);
        model.addAttribute("totalEnEspera", turnosEnEspera.size());

        log.debug("Turnos en espera cargados para fecha: {}", fecha);
        return "turnos/en-espera";
    }

    /**
     * Turnos por médico específico
     */
    @GetMapping("/medico/{medicoId}")
    public String turnosPorMedico(
            @PathVariable Long medicoId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        List<Turno> turnos = turnoService.obtenerTurnosPorMedico(medicoId, fecha);
        int totalTurnos = turnoService.contarTurnosPorMedico(medicoId, fecha);

        model.addAttribute("turnos", turnos);
        model.addAttribute("medicoId", medicoId);
        model.addAttribute("fecha", fecha);
        model.addAttribute("totalTurnos", totalTurnos);

        log.debug("Turnos por médico {} cargados para fecha: {}", medicoId, fecha);
        return "turnos/por-medico";
    }

    /**
     * Turnos por paciente específico
     */
    @GetMapping("/paciente/{pacienteId}")
    public String turnosPorPaciente(
            @PathVariable Long pacienteId,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        List<Turno> turnos = turnoService.obtenerTurnosPorPaciente(pacienteId, fecha);

        model.addAttribute("turnos", turnos);
        model.addAttribute("pacienteId", pacienteId);
        model.addAttribute("fecha", fecha);
        model.addAttribute("totalTurnos", turnos.size());

        log.debug("Turnos por paciente {} cargados para fecha: {}", pacienteId, fecha);
        return "turnos/por-paciente";
    }

    // ==================== PÁGINAS DE UTILIDAD ====================

    /**
     * Página de estadísticas del día
     */
    @GetMapping("/estadisticas")
    public String estadisticas(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        int totalTurnos = turnoService.contarTurnosDelDia(fecha);
        List<Turno> turnosEnEspera = turnoService.obtenerTurnosEnEspera(fecha);
        List<Turno> turnosFinalizados = turnoService.obtenerTurnosPorEstado("FINALIZADO", fecha);
        List<Turno> turnosCancelados = turnoService.obtenerTurnosPorEstado("CANCELADO", fecha);

        model.addAttribute("fecha", fecha);
        model.addAttribute("totalTurnos", totalTurnos);
        model.addAttribute("turnosEnEspera", turnosEnEspera.size());
        model.addAttribute("turnosFinalizados", turnosFinalizados.size());
        model.addAttribute("turnosCancelados", turnosCancelados.size());

        log.debug("Estadísticas cargadas para fecha: {}", fecha);
        return "turnos/estadisticas";
    }

    /**
     * Página principal - Redirect al dashboard
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/turnos/dashboard";
    }
}