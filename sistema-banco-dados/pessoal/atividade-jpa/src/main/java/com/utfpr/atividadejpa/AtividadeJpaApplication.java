package com.utfpr.atividadejpa;

import com.utfpr.atividadejpa.entity.Departamento;
import com.utfpr.atividadejpa.entity.Funcionario;
import com.utfpr.atividadejpa.service.DepartamentoService;
import com.utfpr.atividadejpa.service.FuncionarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AtividadeJpaApplication {

	private static final Logger log = LoggerFactory.getLogger(AtividadeJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AtividadeJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(DepartamentoService departamentoService, FuncionarioService funcionarioService) {
		return (args) -> {
			log.info("");
			log.info("");
			log.info("====================== LISTAGEM DOS DEPARTAMENTOS");
			for(Departamento departamento : departamentoService.listarTodosDepartamentos()) {
				log.info(departamento.toString());
			}

			log.info("");
			log.info("");
			log.info("====================== LISTAGEM DOS DEPARTAMENTOS");
			for(Funcionario funcionario : funcionarioService.listarTodosFuncionarios()) {
				log.info(funcionario.toString());
			}

			log.info("");
			log.info("");
			log.info("====================== ATIVIDADE");
			log.info("Ex1");
			log.info(funcionarioService.listarFuncionarioPorNomeEQuantidadeDependentes("Bruno Lima", 2).toString());
			log.info("Ex2");
			log.info(funcionarioService.listarFuncionariosPordepartamento("Tecnologia").toString());
			log.info("Ex3");
			log.info(departamentoService.listarPrimeiroCadastro().toString());
			log.info("Ex4");
			log.info(funcionarioService.listarFuncionarioMaiorSalario().toString());
			log.info("Ex5");
			log.info(funcionarioService.listarFuncionariosMaiorSalario(3).toString());
			log.info("Ex6");
			log.info(funcionarioService.listarFuncionariosSemDependentes().toString());
			log.info("Ex7");
			log.info(funcionarioService.listarFuncionariosSalarioMaiorQue(5000f).toString());
			log.info("Ex8");
			log.info(funcionarioService.listarFuncionariosDeSalarioMaiorQue(9000f).toString());
			log.info("Ex9");
			log.info(funcionarioService.listarFuncionariosPorQuantidadeDependentes(2).toString());
			log.info("Ex10");
			log.info(funcionarioService.listarFuncionariosPorNomeContem("Ana").toString());
		};
	}
}
