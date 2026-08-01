package com.utfpr.atividadejpa.service;

import com.utfpr.atividadejpa.entity.Departamento;
import com.utfpr.atividadejpa.entity.Funcionario;
import com.utfpr.atividadejpa.repository.DepartamentoRepository;
import com.utfpr.atividadejpa.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    @Autowired
    private FuncionarioService funcionarioService;

    /// --------------------- ATIVIDADE ANTERIOR
    public List<Departamento> listarTodosDepartamentos() {
        return repository.findAll();
    }

    public Departamento listarPrimeiroCadastro() {
        return repository.findFirstByOrderByIdAsc();
    }

    /// --------------------- ATIVIDADE ATUAL
    @Transactional
    public void criarNovoDepartamentoEAssociarAFuncionario(String nomeDepartamento, Long idFuncionario) {
        Departamento departamento = new Departamento();
        departamento.setNome(nomeDepartamento);

        departamento = repository.save(departamento);

        Funcionario funcionario = funcionarioService.pegarPorId(idFuncionario).orElseThrow(
                () -> new RuntimeException("Funcionário não encontrado")
        );
        funcionario.setDepartamento(departamento);
        funcionarioService.salvarFuncionario(funcionario);
    }


}
