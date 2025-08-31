package com.centromedico.sistema_turnos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "medicos")
public class Medico extends BaseEntity{

    private String nombre;
    private String apellido;
    private String dni;
    private String matricula;
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "consultorio_id")
    private Consultorio consultorio;

    @OneToMany(mappedBy = "medico")
    private Set<Turno> turnos;

}
