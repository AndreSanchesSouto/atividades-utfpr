package com.utfpr.atividadejpa.service;

import com.utfpr.atividadejpa.entity.Departamento;
import com.utfpr.atividadejpa.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    public List<Departamento> listarTodosDepartamentos() {
        return repository.findAll();
    }

    public Departamento listarPrimeiroCadastro() {
        return repository.findFirstByOrderByIdAsc();
    }
}
