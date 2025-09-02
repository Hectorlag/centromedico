package com.centromedico.sistema_turnos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "medicos")
@Entity
public class Turno {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private LocalDateTime horaProgramada;
    private LocalDateTime horaLlamado;
    private LocalDateTime horaInicioAtencion;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    // ✅ OK - Solo cambia datos, sin lógica compleja
    public void cambiarEstadoALlamado() {
        this.estado = "LLAMADO";
        this.horaLlamado = LocalDateTime.now();
    }

    public void cambiarEstadoAAtendiendo() {
        this.estado = "ATENDIENDO";
        this.horaInicioAtencion = LocalDateTime.now();
    }

    // ✅ OK - Query simple sobre datos propios
    public boolean estaEsperando() {
        return "ESPERANDO".equals(estado);
    }

    // Agregar a la clase Turno
    public void cambiarEstadoAFinalizado() {
        this.estado = "FINALIZADO";
    }

    public void cambiarEstadoACancelado() {
        this.estado = "CANCELADO";
    }

}
