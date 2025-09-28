package com.centromedico.sistema_turnos.controller.web;

import com.centromedico.sistema_turnos.dtos.TurnoDTO;
import com.centromedico.sistema_turnos.service.interfaces.TurnoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    /**
     * Dashboard principal - Vista principal del sistema
     * GET /turnos/dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.debug("Mostrando dashboard de turnos");

        // Debug: verificar que el método se ejecuta
        log.info("=== ACCEDIENDO AL DASHBOARD ===");

        // Puedes agregar datos al modelo si necesitas mostrar algo en el dashboard
        // Por ejemplo, estadísticas básicas:
        // model.addAttribute("totalTurnos", turnoService.contarTurnos());

        return "dashboard"; // -> templates/dashboard.html
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
     * Listar turnos básico (si lo necesitas más adelante)
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
}