package com.utfpr.atividadejpa.repository;

import com.utfpr.atividadejpa.entity.Funcionario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    /// --------------------- ATIVIDADE ANTERIOR
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

    /// --------------------- ATIVIDADE ATUAL
    @Procedure("aumentar_salario")
    @Modifying
    void procedureAumentarSalarioEmXPorcento(Integer valor);

    @Query("""
        SELECT f
        FROM Funcionario f
        WHERE f.departamento.nome = :departamento
            AND f.quantidadeDependentes = 0
    """)
    List<Funcionario> buscarPorFuncionariosSemDependentesPorNomeDepartamento(@Param("departamento") String departamento);

    @Modifying
    @Query("""
        UPDATE Funcionario f
        SET f.departamento.id = :departamentoY
        WHERE f.departamento.id = :departamentoX
    """)
    void trocarTodosFuncionariosDeXDepartamentoParaYDepartamentoPorId(
            @Param("departamentoX") Integer departamentoX,
            @Param("departamentoY") Integer departamentoY
    );

    @Modifying
    @Query("""
        DELETE Funcionario f
        WHERE f.departamento.id = :departamentoId
    """)
    void deletarFuncionariosPorDepartamento(@Param("departamentoId") Integer departamentoId);

}
