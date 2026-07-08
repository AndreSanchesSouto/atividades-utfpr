package com.utfpr.atividadejpa.repository;

import com.utfpr.atividadejpa.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
