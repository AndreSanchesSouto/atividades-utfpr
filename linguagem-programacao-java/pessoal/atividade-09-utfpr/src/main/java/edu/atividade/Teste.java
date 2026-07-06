package edu.atividade;

import edu.atividade.entities.Carga;
import edu.atividade.entities.Passeio;
import edu.atividade.entities.Veiculo;
import edu.atividade.exceptions.VeicExistException;
import edu.atividade.exceptions.VelocException;
import edu.atividade.repositories.BDVeiculos;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Teste {
    static final int larg = 800, alt = 600;
    static BDVeiculos veiculosDb = new BDVeiculos();

    public static void main(String[] args) {
        menuInicial();
    }

    public static JFrame criarJanela(String titulo) {
        JFrame frame = new JFrame(titulo);

        frame.setSize(larg, alt);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        return frame;
    }

    public static JButton criarBotao(
            String texto,
            JFrame frameAtual,
            Runnable proximaTela
    ) {
        JButton botao = new JButton(texto);

        botao.addActionListener(acao -> {
            frameAtual.dispose();
            proximaTela.run();
        });

        return botao;
    }

    public static void criarInput(
            JFrame frame,
            String texto,
            JTextField input,
            boolean enabled
    ) {
        input.setEnabled(enabled);
        frame.add(new JLabel(texto));
        frame.add(input);
    }


    public static void menuInicial() {
        JFrame menu = criarJanela("Gestão de Veiculos");

        menu.add(criarBotao("Passeio", menu, Teste::menuPasseio));
        menu.add(criarBotao("Carga", menu, Teste::menuCarga));
        menu.setVisible(true);
    }

    public static void criarMenuVeiculo(
            String titulo,
            Runnable cadastro,
            Runnable consultarPlaca,
            Runnable consultarTodos
    ) {

        JFrame menu = criarJanela(titulo);

        menu.add(criarBotao("Cadastrar", menu, cadastro));
        menu.add(criarBotao("Consultar / Excluir pela placa", menu, consultarPlaca));
        menu.add(criarBotao("Consultar / Excluir todos", menu, consultarTodos));
        menu.add(criarBotao("Sair", menu, Teste::menuInicial));

        menu.setVisible(true);
    }

    public static void menuPasseio() {
        criarMenuVeiculo(
                "Veiculos de Passeio",
                Teste::menuCadastroPasseio,
                Teste::menuConsultarOuExcluirPasseioPlaca,
                Teste::menuConsultarOuExcluirPasseio
        );
    }

    public static void menuCarga() {
        criarMenuVeiculo(
                "Veiculos de Carga",
                Teste::menuCadastroCarga,
                Teste::menuConsultarOuExcluirCargaPlaca,
                Teste::menuConsultarOuExcluirCarga
        );
    }

    public static JFrame criarMenuVeiculoCadastro(
            String titulo,
            List<JTextField> inputs,
            List<String> labelInput,
            List<Boolean> enabled,
            List<JButton> botoes
    ) {
        JFrame menu = criarJanela(titulo);

        for(int i = 0; i < inputs.size(); i++) {
            criarInput(menu, labelInput.get(i), inputs.get(i), enabled.get(i));
        }
        for (JButton botao : botoes) {
            menu.add(botao);
        }

        menu.setLayout(new GridLayout(0, 2, 10, 10));
        menu.setVisible(true);

        return menu;
    }


    public static void menuCadastroPasseio() {
        JTextField inputQuantidadePassageiros = new JTextField();
        JTextField inputPlaca = new JTextField();
        JTextField inputMarca = new JTextField();
        JTextField inputModelo = new JTextField();
        JTextField inputCor = new JTextField();
        JTextField inputQuantidadeRodas = new JTextField();
        JTextField inputVelocidadeMaxima = new JTextField();
        JTextField inputQuantidadePistoes = new JTextField();
        JTextField inputPotencia = new JTextField();

        JButton botaoCadastrarPasseio = new JButton("Cadastrar");
        JButton botaoLimparPasseio = new JButton("Limpar");
        JButton botaoSairPasseio = new JButton("Sair");

        JFrame menu = criarMenuVeiculoCadastro(
                "Cadastro de Passeio",
                Arrays.asList(
                        inputQuantidadePassageiros,
                        inputPlaca,
                        inputMarca,
                        inputModelo,
                        inputCor,
                        inputQuantidadeRodas,
                        inputVelocidadeMaxima,
                        inputQuantidadePistoes,
                        inputPotencia
                ),
                Arrays.asList(
                        "Marca:",
                        "Modelo:",
                        "Placa:",
                        "Cor:",
                        "Quantidade de Rodas:",
                        "Velocidade Máxima:",
                        "Potência:",
                        "Quantidade de Pistões:",
                        "Quantidade de Passageiros:"
                ),
                Arrays.asList(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true
                ),
                Arrays.asList(
                        botaoCadastrarPasseio,
                        botaoLimparPasseio,
                        botaoSairPasseio
                )
        );

        botaoCadastrarPasseio.addActionListener(acao -> {
            try {
                veiculosDb.adicionarVeiculoPasseio(cadastrarVeiculoPasseio(
                        inputQuantidadePassageiros.getText(),
                        inputPlaca.getText(),
                        inputMarca.getText(),
                        inputModelo.getText(),
                        inputCor.getText(),
                        inputQuantidadeRodas.getText(),
                        inputVelocidadeMaxima.getText(),
                        inputQuantidadePistoes.getText(),
                        inputPotencia.getText()
                ));
                boolean repetir = cadastrarNovo("Deseja cadastrar novo Veículo de Passeio");
                menu.dispose();
                if(!repetir) {
                    menuPasseio();
                } else {
                    menuCadastroPasseio();
                }
            } catch (Exception ex) {
                if(ex instanceof VeicExistException) {
                    dispararMensagem(ex.getMessage());
                    menu.dispose();
                    menuInicial();
                }
            }
        });

        botaoLimparPasseio.addActionListener(acao -> {
                    menu.dispose();
                    menuCadastroPasseio();
                }
        );

        botaoSairPasseio.addActionListener(acao -> {
                    menu.dispose();
                    menuPasseio();
                }
        );
    }

    public static void menuCadastroCarga() {
        JTextField inputCargaMaxima = new JTextField();
        JTextField inputTara = new JTextField();
        JTextField inputPlaca = new JTextField();
        JTextField inputMarca = new JTextField();
        JTextField inputModelo = new JTextField();
        JTextField inputCor = new JTextField();
        JTextField inputQuantidadeRodas = new JTextField();
        JTextField inputVelocidadeMaxima = new JTextField();
        JTextField inputQuantidadePistoes = new JTextField();
        JTextField inputPotencia = new JTextField();

        JButton botaoCadastrarCarga = new JButton("Cadastrar");
        JButton botaoLimparCarga = new JButton("Limpar");
        JButton botaoSairCarga = new JButton("Sair");

        JFrame menu = criarMenuVeiculoCadastro(
                "Cadastro de Passeio",
                Arrays.asList(
                        inputMarca,
                        inputModelo,
                        inputPlaca,
                        inputCor,
                        inputQuantidadeRodas,
                        inputVelocidadeMaxima,
                        inputPotencia,
                        inputQuantidadePistoes,
                        inputCargaMaxima,
                        inputTara
                ),
                Arrays.asList(
                        "Marca:",
                        "Modelo:",
                        "Placa:",
                        "Cor:",
                        "Quantidade de Rodas:",
                        "Velocidade Máxima:",
                        "Potência:",
                        "Quantidade de Pistões:",
                        "Carga Máxima:",
                        "Tara:"
                ),
                Arrays.asList(
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true
                ),
                Arrays.asList(
                        botaoCadastrarCarga,
                        botaoLimparCarga,
                        botaoSairCarga
                )
        );

        botaoCadastrarCarga.addActionListener(acao -> {
            try {
                veiculosDb.adicionarVeiculoCarga(cadastrarVeiculoCarga(
                    inputCargaMaxima.getText(),
                    inputTara.getText(),
                    inputPlaca.getText(),
                    inputMarca.getText(),
                    inputModelo.getText(),
                    inputCor.getText(),
                    inputQuantidadeRodas.getText(),
                    inputVelocidadeMaxima.getText(),
                    inputQuantidadePistoes.getText(),
                    inputPotencia.getText()
                ));
                boolean repetir = cadastrarNovo("Deseja cadastrar novo Veículo de Carga");
                menu.dispose();
                if(!repetir) {
                    menuCarga();
                } else {
                    menuCadastroCarga();
                }
            } catch (Exception ex) {
                if(ex instanceof VeicExistException) {
                    dispararMensagem(ex.getMessage());
                    menu.dispose();
                    menuInicial();
                }
            }
        });

        botaoLimparCarga.addActionListener(acao -> {
                    menu.dispose();
                    menuCadastroCarga();
                }
        );

        botaoSairCarga.addActionListener(acao -> {
                    menu.dispose();
                    menuCarga();
                }
        );

    }

    public static void menuConsultarOuExcluirPasseioPlaca() {
        JFrame menu = criarJanela("Consultar ou Excluir Veículo de Passeio por Placa");

        JTextField inputQuantidadePassageiros = new JTextField();
        JTextField inputPlaca = new JTextField();
        JTextField inputMarca = new JTextField();
        JTextField inputModelo = new JTextField();
        JTextField inputCor = new JTextField();
        JTextField inputQuantidadeRodas = new JTextField();
        JTextField inputVelocidadeMaxima = new JTextField();
        JTextField inputQuantidadePistoes = new JTextField();
        JTextField inputPotencia = new JTextField();

        JButton botaoConsultarPasseio = new JButton("Consultar");
        JButton botaoRemoverPasseio = new JButton("Remover");
        JButton botaoLimparPasseio = new JButton("Limpar");
        JButton botaoSairPasseio = new JButton("Sair");


        criarInput(menu, "Placa:", inputPlaca, true);
        criarInput(menu, "Marca:", inputMarca, false);
        criarInput(menu, "Modelo:", inputModelo, false);
        criarInput(menu, "Cor:", inputCor, false);
        criarInput(menu, "Quantidade de Rodas:", inputQuantidadeRodas, false);
        criarInput(menu, "Velocidade Máxima:", inputVelocidadeMaxima, false);
        criarInput(menu, "Potência:", inputPotencia, false);
        criarInput(menu, "Quantidade de Pistões:", inputQuantidadePistoes, false);
        criarInput(menu, "Quantidade de Passageiros:", inputQuantidadePassageiros, false);

        menu.add(botaoConsultarPasseio);
        menu.add(botaoRemoverPasseio);
        menu.add(botaoLimparPasseio);
        menu.add(botaoSairPasseio);

        botaoConsultarPasseio.addActionListener(acao -> {
            try {
                Passeio passeio = imprimirVeiculosPasseioPorPlaca(inputPlaca.getText());
                inputQuantidadePassageiros.setText(Integer.toString(passeio.getQtdPassageiros()));
                inputMarca.setText(passeio.getMarca());
                inputModelo.setText(passeio.getModelo());
                inputCor.setText(passeio.getCor());
                inputQuantidadeRodas.setText(Integer.toString(passeio.getQtdRodas()));
                inputVelocidadeMaxima.setText(Float.toString(passeio.getVelocMax()));
                inputQuantidadePistoes.setText(Integer.toString(passeio.getMotor().getQtdPist()));
                inputPotencia.setText(Integer.toString(passeio.getMotor().getPotencia()));
            } catch (Exception ex) {
                inputQuantidadePassageiros.setText("Não Encontrado!");
                inputMarca.setText("Não Encontrado!");
                inputModelo.setText("Não Encontrado!");
                inputCor.setText("Não Encontrado!");
                inputQuantidadeRodas.setText("Não Encontrado!");
                inputVelocidadeMaxima.setText("Não Encontrado!");
                inputQuantidadePistoes.setText("Não Encontrado!");
                inputPotencia.setText("Não Encontrado!");
            }
        });

        botaoRemoverPasseio.addActionListener(acao -> excluirVeiculosPasseioPorPlaca(inputPlaca.getText()));

        botaoLimparPasseio.addActionListener(acao -> {
            menu.dispose();
            menuConsultarOuExcluirPasseioPlaca();
        });

        botaoSairPasseio.addActionListener(acao -> {
                    menu.dispose();
                    menuPasseio();
        });

        menu.setLayout(new GridLayout(0, 2, 10, 10));
        menu.setVisible(true);
    }

    public static void menuConsultarOuExcluirCargaPlaca() {
        JFrame menu = criarJanela("Consultar ou Excluir Veículo de Carga por Placa");

        JTextField inputCargaMaxima = new JTextField();
        JTextField inputTara = new JTextField();
        JTextField inputPlaca = new JTextField();
        JTextField inputMarca = new JTextField();
        JTextField inputModelo = new JTextField();
        JTextField inputCor = new JTextField();
        JTextField inputQuantidadeRodas = new JTextField();
        JTextField inputVelocidadeMaxima = new JTextField();
        JTextField inputQuantidadePistoes = new JTextField();
        JTextField inputPotencia = new JTextField();

        JButton botaoConsultarCarga = new JButton("Consultar");
        JButton botaoRemoverCarga = new JButton("Remover");
        JButton botaoLimparCarga = new JButton("Limpar");
        JButton botaoSairCarga = new JButton("Sair");

        criarInput(menu, "Placa:", inputPlaca, true);
        criarInput(menu, "Marca:", inputMarca, false);
        criarInput(menu, "Modelo:", inputModelo, false);
        criarInput(menu, "Cor:", inputCor, false);
        criarInput(menu, "Quantidade de Rodas:", inputQuantidadeRodas, false);
        criarInput(menu, "Velocidade Máxima:", inputVelocidadeMaxima, false);
        criarInput(menu, "Potência:", inputPotencia, false);
        criarInput(menu, "Quantidade de Pistões:", inputQuantidadePistoes, false);
        criarInput(menu, "Carga máxima:", inputCargaMaxima, false);
        criarInput(menu, "Tara:", inputTara, false);

        menu.add(botaoConsultarCarga);
        menu.add(botaoRemoverCarga);
        menu.add(botaoLimparCarga);
        menu.add(botaoSairCarga);

        botaoConsultarCarga.addActionListener(acao -> {
            try {
                Carga passeio = imprimirVeiculosCargaPorPlaca(inputPlaca.getText());
                inputTara.setText(Integer.toString(passeio.getTara()));
                inputCargaMaxima.setText(Integer.toString(passeio.getCargaMax()));
                inputMarca.setText(passeio.getMarca());
                inputModelo.setText(passeio.getModelo());
                inputCor.setText(passeio.getCor());
                inputQuantidadeRodas.setText(Integer.toString(passeio.getQtdRodas()));
                inputVelocidadeMaxima.setText(Float.toString(passeio.getVelocMax()));
                inputQuantidadePistoes.setText(Integer.toString(passeio.getMotor().getQtdPist()));
                inputPotencia.setText(Integer.toString(passeio.getMotor().getPotencia()));
            } catch (Exception ex) {
                inputCargaMaxima.setText("Não Encontrado!");
                inputTara.setText("Não Encontrado!");
                inputMarca.setText("Não Encontrado!");
                inputModelo.setText("Não Encontrado!");
                inputCor.setText("Não Encontrado!");
                inputQuantidadeRodas.setText("Não Encontrado!");
                inputVelocidadeMaxima.setText("Não Encontrado!");
                inputQuantidadePistoes.setText("Não Encontrado!");
                inputPotencia.setText("Não Encontrado!");
            }
        });

        botaoRemoverCarga.addActionListener(acao -> excluirVeiculosCargaPorPlaca(inputPlaca.getText()));

        botaoLimparCarga.addActionListener(acao -> {
            menu.dispose();
            menuConsultarOuExcluirCargaPlaca();
        });

        botaoSairCarga.addActionListener(acao -> {
                    menu.dispose();
                    menuCarga();
                }
        );

        menu.setLayout(new GridLayout(0, 2, 10, 10));
        menu.setVisible(true);
    }

    public static void menuConsultarOuExcluirPasseio() {
        JFrame menu = criarJanela("Consultar ou Excluir Veículos de Passeio");
        JButton botaoDeletar = new JButton("Deletar tudo");
        JButton botaoSair = new JButton("Sair");

        String[] colunas = {
                "Placa",
                "Marca",
                "Modelo",
                "Cor",
                "Velocidade Máxima",
                "Qtd. Rodas",
                "Qtd. Passageiros",
                "Potencia",
                "Quantidade de Pistões",
                "CalcVel",
                "Calcular"
        };

        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        List<Passeio> veiculos = veiculosDb.getVeiculosPasseio();

        for (Passeio passeio : veiculos) {

            Object[] linha = {
                    passeio.getPlaca(),
                    passeio.getMarca(),
                    passeio.getModelo(),
                    passeio.getCor(),
                    passeio.getVelocMax(),
                    passeio.getQtdRodas(),
                    passeio.getQtdPassageiros(),
                    passeio.getMotor().getPotencia(),
                    passeio.getMotor().getQtdPist(),
                    passeio.calcVel(passeio.getVelocMax()),
                    passeio.calcular()
            };

            model.addRow(linha);
        }

        JTable tabela = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tabela);

        scrollPane.setPreferredSize(new Dimension(larg, alt));

        menu.setLayout(new FlowLayout());

        menu.add(scrollPane);
        menu.add(botaoDeletar);
        menu.add(botaoSair);

        botaoDeletar.addActionListener(acao -> {
            deletarListaPasseio();
            menu.dispose();
            menuConsultarOuExcluirPasseio();
        });

        botaoSair.addActionListener(acao -> {
            menu.dispose();
            menuPasseio();
        });

        menu.setSize(Math.round(larg*1.5f), alt);
        menu.setVisible(true);
    }

    public static void menuConsultarOuExcluirCarga() {
        JFrame menu = criarJanela("Consultar ou Excluir Veículos de Carga");
        JButton botaoDeletar = new JButton("Deletar tudo");
        JButton botaoSair = new JButton("Sair");

        String[] colunas = {
                "Placa",
                "Marca",
                "Modelo",
                "Cor",
                "Velocidade Máxima",
                "Qtd. Rodas",
                "Tara",
                "Carga máxima",
                "Qtd. Passageiros",
                "Potencia",
                "Quantidade de Pistões",
                "CalcVel",
                "Calcular"
        };

        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        List<Carga> veiculos = veiculosDb.getVeiculosCarga();

        for (Carga carga : veiculos) {

            Object[] linha = {
                    carga.getPlaca(),
                    carga.getMarca(),
                    carga.getModelo(),
                    carga.getCor(),
                    carga.getVelocMax(),
                    carga.getQtdRodas(),
                    carga.getTara(),
                    carga.getCargaMax(),
                    carga.getMotor().getPotencia(),
                    carga.getMotor().getQtdPist(),
                    carga.calcVel(carga.getVelocMax()),
                    carga.calcular()
            };

            model.addRow(linha);
        }

        JTable tabela = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tabela);

        scrollPane.setPreferredSize(new Dimension(larg, alt));

        menu.setLayout(new FlowLayout());

        menu.add(scrollPane);
        menu.add(botaoDeletar);
        menu.add(botaoSair);

        botaoDeletar.addActionListener(acao -> {
            deletarListaCarga();
            menu.dispose();
            menuConsultarOuExcluirCarga();
        });

        botaoSair.addActionListener(acao -> {
            menu.dispose();
            menuCarga();
        });

        menu.setSize(larg*2, alt);
        menu.setVisible(true);
    }

    public static void deletarListaPasseio() {
        veiculosDb.limparListPasseio();
    }

    public static void deletarListaCarga() {
        veiculosDb.limparListCarga();
    }

    public static void cadastrarVeiculo (
            Veiculo veiculo,
            String placa,
            String marca,
            String modelo,
            String cor,
            String quantidadeRodas,
            String velocidadeMaxima,
            String quantidadePistoes,
            String potencia
    ) throws Exception {
        veiculo.setMarca(validarEntradaString(marca, "Valor no campo 'Marca' é inválido"));
        veiculo.setModelo(validarEntradaString(modelo, "Valor no campo 'Modelo' é inválido"));
        veiculo.setPlaca(validarPlaca(placa));
        veiculo.setCor(validarEntradaString(cor, "Valor no campo 'Cor' é inválido"));
        veiculo.setQtdRodas(validarEntradaInt(quantidadeRodas, "Valor no campo 'Quantidade Rodas' é inválido"));

        try {
            veiculo.setVelocMax(validarVelocidade(validarEntradaFloat(velocidadeMaxima, "Valor no campo 'Velocidade Máxima' é inválido")));
        } catch (VelocException ex) {
            if (veiculo instanceof Carga) {
                dispararMensagem(ex.getMessage());
                veiculo.setVelocMax(90f);
                dispararMensagem("Velocidade máxima definida como: 90");
            } else if (veiculo instanceof Passeio) {
                dispararMensagem(ex.getMessage());
                veiculo.setVelocMax(100f);
                dispararMensagem("Velocidade máxima definida como: 100");
            }
        }

        veiculo.getMotor().setPotencia(validarEntradaInt(potencia, "Valor no campo 'Potência' é inválido"));
        veiculo.getMotor().setQtdPist(validarEntradaInt(quantidadePistoes, "Valor no campo 'Quantidade de Pistões' é inválido"));
    }

    public static String validarPlaca(String placa) throws Exception {
        validarEntradaString(placa, "Valor no campo 'Placa' é inválido");
        if (veiculosDb.getVeiculosCarga().stream().anyMatch(veiculo -> veiculo.getPlaca().equals(placa)) ||
            veiculosDb.getVeiculosPasseio().stream().anyMatch(veiculo -> veiculo.getPlaca().equals(placa))
        ) {
            throw new VeicExistException();
        }
        return placa;

    }

    public static float validarVelocidade(float velocidade) throws VelocException {
        if (velocidade < 80 || velocidade > 110) throw new VelocException();
        return velocidade;
    }

    public static Passeio cadastrarVeiculoPasseio(
            String quantidadePassageiros,
            String placa,
            String marca,
            String modelo,
            String cor,
            String quantidadeRodas,
            String velocidadeMaxima,
            String quantidadePistoes,
            String potencia
    ) throws Exception {
        Passeio passeio = new Passeio();
        cadastrarVeiculo(
                passeio,
                placa,
                marca,
                modelo,
                cor,
                quantidadeRodas,
                velocidadeMaxima,
                quantidadePistoes,
                potencia
            );
        passeio.setQtdPassageiros(validarEntradaInt(quantidadePassageiros, "Valor no campo 'Quantidade de passageiros' é inválido"));
        return passeio;
    }

    public static boolean cadastrarNovo(String mensagem) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(mensagem));

        int resultado = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Novo cadastro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return resultado == JOptionPane.YES_OPTION;
    }

    public static Carga cadastrarVeiculoCarga (
            String cargaMaxima,
            String tara,
            String placa,
            String marca,
            String modelo,
            String cor,
            String quantidadeRodas,
            String velocidadeMaxima,
            String quantidadePistoes,
            String potencia

    ) throws Exception {
        Carga carga = new Carga();
        cadastrarVeiculo(
                carga,
                placa,
                marca,
                modelo,
                cor,
                quantidadeRodas,
                velocidadeMaxima,
                quantidadePistoes,
                potencia
            );
        carga.setCargaMax(validarEntradaInt(cargaMaxima, "Valor no campo 'Carga Máxima' é inválido"));
        carga.setTara(validarEntradaInt(tara, "Valor no campo 'Tara' é inválido"));
        return carga;
    }

    public static int validarEntradaInt(String entradaDados, String erroMensagem) {
        int entrada = 0;
        try {
            entrada = Integer.parseInt(entradaDados);
            if (entrada > 0) { return entrada; }
            dispararErroMensagem("Digite um número inteiro maior que zero! " + erroMensagem);
        } catch (NumberFormatException error) {
            dispararErroMensagem("Digite um número inteiro! " + erroMensagem);
        }
        return entrada;
    }

    public static float validarEntradaFloat(String entradaDado, String erroMensagem) {
        float entrada = 0;
        try {
            entrada = Float.parseFloat(entradaDado);
            if (entrada > 0) { return entrada; }
            dispararErroMensagem("Digite um número maior que zero! " + erroMensagem);
        } catch (NumberFormatException error) {
            dispararErroMensagem("Digite um número! " + erroMensagem);
        }
        return entrada;
    }

    public static String validarEntradaString(String entradaDado, String erroMensagem) {
        if(entradaDado.isBlank()) {
            dispararErroMensagem("Digite um valor válido! " + erroMensagem);
        }
        return entradaDado;
    }

    public static void dispararErroMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem,"ERRO", JOptionPane.INFORMATION_MESSAGE);
        throw new RuntimeException(mensagem);
    }

    public static void dispararMensagem(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem,"ALERTA", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Passeio imprimirVeiculosPasseioPorPlaca(String placa) {
        Optional<Passeio> veiculo = pegarVeiculoPasseioPorPlaca(placa);
        if (veiculo.isPresent()) {
            return veiculo.get();
        } else {
            dispararErroMensagem("Nenhum veículo de passeio encontrado");
            return null;
        }
    }

    public static Carga imprimirVeiculosCargaPorPlaca(String placa) {
        Optional<Carga> veiculo = pegarVeiculoCargaPorPlaca(placa);
        if (veiculo.isPresent()) {
            return veiculo.get();
        } else {
            dispararMensagem("Nenhum veículo de carga encontrado");
            return null;
        }
    }

    public static void excluirVeiculosPasseioPorPlaca(String placa) {
        Optional<Passeio> veiculo = pegarVeiculoPasseioPorPlaca(placa);
        if (veiculo.isPresent()) {
            veiculosDb.getVeiculosPasseio().remove(veiculo.get());
            dispararMensagem("Veiculo removido");
        } else {
            dispararMensagem("Nenhum veículo de passeio encontrado\n");
        }
    }

    public static Optional<Passeio> pegarVeiculoPasseioPorPlaca(String procurarPlaca) {
        return veiculosDb.getVeiculosPasseio()
                .stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(procurarPlaca))
                .findAny();
    }

    public static Optional<Carga> pegarVeiculoCargaPorPlaca(String procurarPlaca) {
        return veiculosDb.getVeiculosCarga()
                .stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(procurarPlaca))
                .findAny();
    }

    public static void excluirVeiculosCargaPorPlaca(String placa) {
        Optional<Carga> veiculo = pegarVeiculoCargaPorPlaca(placa);
        if (veiculo.isPresent()) {
            veiculosDb.getVeiculosCarga().remove(veiculo.get());
            dispararMensagem("Veiculo removido");
        } else {
            dispararMensagem("Nenhum veículo de carga encontrado\n");
        }
    }
}