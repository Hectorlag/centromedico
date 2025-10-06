package com.centromedico.sistema_turnos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "especialidades")
public class Especialidad extends BaseEntity{

    private String nombre;
    private String descripcion;
    private Integer duracionTurnoMinutos;

    @OneToMany(mappedBy = "especialidad")
    private Set<Medico> medicos;
}
