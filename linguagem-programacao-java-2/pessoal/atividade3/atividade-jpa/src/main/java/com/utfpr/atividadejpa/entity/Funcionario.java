package com.utfpr.atividadejpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "funcionario")
@Data
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_funcionario", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "quantidade_dependentes", nullable = false)
    private int quantidadeDependentes;

    @Column(name = "salario", nullable = false)
    private Float salario;

    @Column(name = "cargo", nullable = false, length = 50)
    private String cargo;

    @ManyToOne
    @JoinColumn(name="codigo_departamento", nullable = false)
    private Departamento departamento;
}
