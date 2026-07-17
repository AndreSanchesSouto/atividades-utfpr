package com.utfpr.atividadejpa.repository;

import com.utfpr.atividadejpa.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Departamento findFirstByOrderByIdAsc();
}
