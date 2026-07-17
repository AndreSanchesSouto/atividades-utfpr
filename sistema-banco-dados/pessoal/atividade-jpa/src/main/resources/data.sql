-- ===========================
-- DEPARTAMENTOS
-- ===========================

INSERT INTO departamento (codigo_departamento, nome) VALUES (1, 'Tecnologia');
INSERT INTO departamento (codigo_departamento, nome) VALUES (2, 'Recursos Humanos');
INSERT INTO departamento (codigo_departamento, nome) VALUES (3, 'Financeiro');
INSERT INTO departamento (codigo_departamento, nome) VALUES (4, 'Comercial');

-- ===========================
-- FUNCIONÁRIOS
-- ===========================

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (1, 'Ana Souza', 0, 3500.00, 'Desenvolvedora Júnior', 1);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (2, 'Bruno Lima', 2, 7200.00, 'Desenvolvedor Pleno', 1);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (3, 'Carlos Henrique', 1, 9500.00, 'Arquiteto de Software', 1);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (4, 'Daniela Martins', 3, 4200.00, 'Analista de RH', 2);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (5, 'Eduardo Silva', 0, 5800.00, 'Recrutador', 2);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (6, 'Fernanda Costa', 2, 8100.00, 'Coordenadora RH', 2);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (7, 'Gabriel Almeida', 1, 6700.00, 'Analista Financeiro', 3);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (8, 'Helena Rocha', 0, 12000.00, 'Gerente Financeira', 3);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (9, 'Igor Santos', 4, 5100.00, 'Vendedor', 4);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (10, 'Juliana Ferreira', 2, 8900.00, 'Gerente Comercial', 4);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (11, 'Mariana Alves', 0, 7600.00, 'UX Designer', 1);

INSERT INTO funcionario
(codigo_funcionario, nome, quantidade_dependentes, salario, cargo, codigo_departamento)
VALUES
    (12, 'Lucas Oliveira', 1, 15000.00, 'Diretor de Tecnologia', 1);