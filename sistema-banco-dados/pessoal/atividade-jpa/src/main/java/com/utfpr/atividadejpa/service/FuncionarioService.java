package com.utfpr.atividadejpa.service;

import com.utfpr.atividadejpa.entity.Funcionario;
import com.utfpr.atividadejpa.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    public List<Funcionario> listarTodosFuncionarios() {
        return repository.findAll();
    }

    public Funcionario listarFuncionarioPorNomeEQuantidadeDependentes(String nome, int quantidadePedendentes) {
        return repository.findByNomeAndQuantidadeDependentes(nome, quantidadePedendentes);
    }

    public List<Funcionario> listarFuncionariosPordepartamento(String nome) {
        return repository.findByDepartamentoNome(nome);
    }

    public Funcionario listarFuncionarioMaiorSalario() {
        return repository.findFirstByOrderBySalarioDesc();
    }

    public List<Funcionario> listarFuncionariosMaiorSalario(int tamanhoLista) {
        return repository.findTopSalario(tamanhoLista);
    }

    public List<Funcionario> listarFuncionariosSemDependentes() {
        return repository.findFuncionariosWithoutDependentes();
    }

    public List<Funcionario> listarFuncionariosSalarioMaiorQue(float valorSalario) {
        return repository.findFuncionariosBySalarioGreaterThan(valorSalario);
    }

    public List<Funcionario> listarFuncionariosDeSalarioMaiorQue(float valorSalario) {
        return repository.findFuncionariosWhereSalarioGreaterThan(valorSalario);
    }

    public List<Funcionario> listarFuncionariosPorQuantidadeDependentes(int quantidadeDependentes) {
        return repository.findByQuantidadeDependentes(quantidadeDependentes);
    }

    public List<Funcionario> listarFuncionariosPorNomeContem(String nome) {
        return repository.findByNomeLike(nome);
    }

}
