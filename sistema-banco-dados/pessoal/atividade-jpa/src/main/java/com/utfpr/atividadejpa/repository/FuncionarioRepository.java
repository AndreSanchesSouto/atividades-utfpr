package com.utfpr.atividadejpa.repository;

import com.utfpr.atividadejpa.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByNomeAndQuantidadeDependentes(String nome, int quantidadeDependentes);

    @Query("""
        SELECT f
        FROM Funcionario f
        WHERE f.departamento.nome = :departamentoNome
    """)
    List<Funcionario> findByDepartamentoNome(
            @Param("departamentoNome") String departamentoNome
    );

    Funcionario findFirstByOrderBySalarioDesc();

    @Query(value = """
        SELECT
            *
        FROM funcionario
        ORDER BY salario DESC
        LIMIT :tamanhoLista
    """, nativeQuery = true)
    List<Funcionario> findTopSalario(
            @Param("tamanhoLista") int tamanhoLista
    );

    @Query("""
        SELECT f
        FROM Funcionario f
        WHERE f.quantidadeDependentes = 0
        ORDER BY f.nome ASC
    """)
    List<Funcionario> findFuncionariosWithoutDependentes();

    @Query("""
        SELECT f
        FROM Funcionario f
        WHERE f.salario > :valorSalario
    """)
    List<Funcionario> findFuncionariosBySalarioGreaterThan(@Param("valorSalario") float valorSalario);

    @Query(value = """
        SELECT
            *
        FROM funcionario
        WHERE salario > :valorSalario
    """, nativeQuery = true)
    List<Funcionario> findFuncionariosWhereSalarioGreaterThan(@Param("valorSalario") float valorSalario);

    List<Funcionario> findByQuantidadeDependentes(@Param("quantidadeDependentes") int quantidadeDependentes);

    List<Funcionario> findByNomeLike(@Param("nome") String nome);

}
