-- Questão 2
DROP DATABASE IF EXISTS a2;
CREATE DATABASE a2;
USE a2;

CREATE TABLE departamento(
	codigo INT PRIMARY KEY,
	nome VARCHAR(100)
);

CREATE TABLE funcionario(
	codigo INT PRIMARY KEY,
	codigo_departamento INT,
	nome VARCHAR(100),
	quantidade_dependentes INT,
	salario FLOAT,
	cargo VARCHAR(100),
	FOREIGN KEY (codigo_departamento)
        REFERENCES departamento(codigo)
);


-- Questão 3

-- Departamentos
INSERT INTO departamento (codigo, nome) VALUES
(1, 'Diretoria Geral'),
(2, 'Diretoria Financeira'),
(3, 'Tecnologia da Informacao'),
(4, 'Recursos Humanos'),
(5, 'Marketing'),
(6, 'Vendas');

-- Funcionarios
INSERT INTO funcionario
(codigo, codigo_departamento, nome, quantidade_dependentes, salario, cargo)
VALUES
(101, 1, 'Carlos Mendes', 2, 15000.00, 'Diretor'),
(102, 1, 'Ana Paula', 1, 8500.00, 'Gerente'),
(103, 1, 'Ricardo Souza', 0, 6000.00, 'Analista'),
(201, 2, 'Fernanda Lima', 0, 12000.00, 'Diretor Financeiro'),
(202, 2, 'Bruno Alves', 0, 7000.00, 'Analista Financeiro'),
(301, 3, 'Joao Silva', 0, 9000.00, 'Desenvolvedor'),
(302, 3, 'Maria Oliveira', 1, 9500.00, 'Analista de Sistemas'),
(303, 3, 'Pedro Santos', 2, 11000.00, 'Gerente'),
(304, 3, 'Juliana Costa', 0, 8000.00, 'Desenvolvedor'),
(305, 3, 'Lucas Rocha', 1, 7800.00, 'Suporte'),
(401, 4, 'Patricia Gomes', 0, 6500.00, 'Analista RH'),
(402, 4, 'Gabriel Ferreira', 2, 10000.00, 'Gerente'),
(501, 5, 'Amanda Martins', 0, 5500.00, 'Assistente'),
(502, 5, 'Thiago Ribeiro', 1, 6200.00, 'Analista'),
(601, 6, 'Roberto Dias', 3, 7200.00, 'Vendedor'),
(602, 6, 'Camila Nunes', 0, 7300.00, 'Vendedor'),
(603, 6, 'Felipe Costa', 0, 10500.00, 'Gerente');


-- Questão 4

-- view A
CREATE VIEW vw_departamento_funcionario AS
WITH contagem1 AS (
	SELECT
		COUNT(f.codigo) AS qnt_funcionarios,
		f.codigo_departamento,
		d.nome AS departamento_nome
	FROM departamento d
		JOIN funcionario f ON f.codigo_departamento = d.codigo
	GROUP BY f.codigo_departamento
)
SELECT
	departamento_nome,
	qnt_funcionarios
FROM contagem1
WHERE qnt_funcionarios = (
	SELECT MAX(qnt_funcionarios)
	FROM contagem1
);

SELECT * FROM vw_departamento_funcionario;

-- view B
CREATE VIEW vw_departamento_menor_funcionario_depend AS
WITH contagem2 AS (
	SELECT
		COUNT(d.codigo) AS qtd_depart,
		d.nome AS d_nome,
		COUNT(*) AS qtd_func
	FROM departamento d
		JOIN funcionario f ON f.codigo_departamento = d.codigo
	GROUP BY d.codigo
)
SELECT
	d_nome AS nome_depart,
	qtd_func
FROM contagem2
WHERE qtd_depart = (
	SELECT MIN(qtd_depart)
	FROM contagem2
);

SELECT * FROM vw_departamento_menor_funcionario_depend;

-- view C
CREATE VIEW vw_departamento_nome_comeca_dir AS
SELECT
	d.nome AS depart_nome,
	f.nome AS func_nome
FROM departamento d
	JOIN funcionario f ON f.codigo_departamento = d.codigo
WHERE d.nome LIKE 'DIR%';

SELECT * FROM vw_departamento_nome_comeca_dir;

-- view D
CREATE VIEW vw_funcionario_depart_maior_salario AS
WITH contagem3 AS (
	SELECT
		d.nome AS departamento,
		f.nome AS funcionario,
		f.salario AS salario
	FROM departamento d
		JOIN funcionario f ON f.codigo_departamento = d.codigo
)
SELECT * FROM contagem3
WHERE salario = (
	SELECT MAX(salario)
	FROM contagem3
);

-- view E
CREATE VIEW vw_departamento_funcionario_cargo_gerente AS (
SELECT
	d.nome AS departamento,
	f.nome AS funcionario,
	f.cargo
FROM funcionario f
	JOIN departamento d ON d.codigo = f.codigo_departamento
WHERE f.cargo LIKE '%Gerente%'
);

SELECT * FROM vw_departamento_funcionario_cargo_gerente;


-- Questão 5
DROP USER IF EXISTS 'gerente'@'hostname';
CREATE USER 'gerente'@'localhost';
GRANT ALL PRIVILEGES ON a2.* TO 'gerente'@'localhost' WITH GRANT OPTION;

DROP USER IF EXISTS 'funcionario'@'hostname';
CREATE USER 'funcionario'@'localhost';
GRANT SELECT  ON a2.* TO 'funcionario'@'localhost' WITH GRANT OPTION;
