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

}
