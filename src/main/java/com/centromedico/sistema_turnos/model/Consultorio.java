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
@Table(name = "consultorios")
public class Consultorio extends BaseEntity {

    private String numero;
    private String nombre;
    private String piso;
    private String descripcion;

    @OneToMany(mappedBy = "consultorio")
    private Set<Medico> medicos;
}
