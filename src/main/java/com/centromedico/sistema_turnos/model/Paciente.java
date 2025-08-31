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
@Table(name = "pacientes")
public class Paciente extends BaseEntity{

    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    @OneToMany(mappedBy = "paciente")
    private Set<Turno> turnos;
}
