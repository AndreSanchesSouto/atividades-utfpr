package edu.atividade;

import edu.atividade.entities.Carga;
import edu.atividade.entities.Passeio;
import edu.atividade.entities.Veiculo;
import edu.atividade.exceptions.VeicExistException;
import edu.atividade.exceptions.VelocException;
import edu.atividade.repositories.BDVeiculos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Teste {
    static BDVeiculos veiculosDb = new BDVeiculos();

    public static void main(String[] args) {
            Leitura leitura = new Leitura();
            Scanner scanner = new Scanner(System.in);
            menuSistema(scanner, leitura);
    }

    public static void menuSistema(Scanner scanner, Leitura leitura) {
        int escolha;
        do {
            escolha = validarEntradaInt(scanner, leitura, """
                    Sistema de Gestão de Veículos - Menu Inicial\n
                    1. Cadastrar Veículo de Passeio
                    2. Cadastrar Veículo de Carga
                    3. Imprimir Todos os Veículos de Passeio
                    4. Imprimir Todos os Veículos de Carga
                    5. Imprimir Veículo de Passeio pela Placa
                    6. Imprimir Veículo de Carga pela Placa
                    7. Excluir Veículo de Passeio pela Placa
                    8. Excluir Veículo de Carga pela Placa
                    9. Sair do Sistema: """);
            switch (escolha) {
                case 1: cadastrarVeiculoPasseioMenu(scanner, leitura);
                break;
                case 2: cadastrarVeiculoCargaMenu(scanner, leitura);
                break;
                case 3: imprimirVeiculosPasseio();
                break;
                case 4: imprimirVeiculosCarga();
                break;
                case 5: imprimirVeiculosPasseioPorPlaca(scanner);
                break;
                case 6: imprimirVeiculosCargaPorPlaca(scanner);
                break;
                case 7: excluirVeiculosPasseioPorPlaca(scanner);
                break;
                case 8: excluirVeiculosCargaPorPlaca(scanner);
                break;
                case 9: sairDoSistema(scanner);
                break;
                default: System.out.println("Digite uma opção válida!");
            }
        } while (escolha != 9);
    }

    public static void cadastrarVeiculo(Veiculo veiculo, Scanner scanner, Leitura leitura) {
        veiculo.setMarca(validarEntradaString(scanner, leitura, "Marca: "));

        veiculo.setModelo(validarEntradaString(scanner, leitura, "Modelo: "));

        try {
            veiculo.setPlaca(validarPlaca(scanner, leitura));
        } catch (VeicExistException e) {
            System.out.println(e.getMessage());
            menuSistema(scanner, leitura);
        }

        veiculo.setCor(validarEntradaString(scanner, leitura, "Cor: "));

        veiculo.setQtdRodas(validarEntradaInt(scanner, leitura, "Qtd rodas: "));

        try {
            veiculo.setVelocMax(validarVelocidade(validarEntradaFloat(scanner, leitura, "Velocidade máxima: ")));
        } catch (VelocException ex) {
            if (veiculo instanceof Carga) {
                System.out.println(ex.getMessage());
                veiculo.setVelocMax(90f);
                System.out.println("Velocidade máxima definida como: 90");
            } else if (veiculo instanceof Passeio) {
                System.out.println(ex.getMessage());
                veiculo.setVelocMax(100f);
                System.out.println("Velocidade máxima definida como: 100");
            }
        }

        veiculo.getMotor().setPotencia(validarEntradaInt(scanner, leitura,"Potência do motor: "));
        veiculo.getMotor().setQtdPist(validarEntradaInt(scanner, leitura, "Qtd pistões: "));
    }

    public static String validarPlaca(Scanner scanner, Leitura leitura) throws VeicExistException {
        String placa = validarEntradaString(scanner, leitura, "Placa: ");
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

    public static void cadastrarVeiculoPasseioMenu(Scanner scanner, Leitura leitura) {
        int repetir;
        boolean escolhaInvalida;
        do {
            veiculosDb.adicionarVeiculoPasseio(cadastrarVeiculoPasseio(scanner, leitura));
            do {
                repetir = validarEntradaInt(scanner, leitura, """
                        \nDeseja cadastrar novamente um veículo de passeio?
                        1. Sim
                        2. Não: """);
                escolhaInvalida = repetir != 1 && repetir != 2;
                if (escolhaInvalida) System.out.println("\nEscolha uma opção válida");
            } while (escolhaInvalida);
        } while (repetir != 2);
    }

    public static Passeio cadastrarVeiculoPasseio(Scanner scanner, Leitura leitura) {
        Passeio passeio = new Passeio();
        cadastrarVeiculo(passeio, scanner, leitura);
        passeio.setQtdPassageiros(validarEntradaInt(scanner, leitura, "Quantidade de passageiros: "));
        return passeio;
    }

    public static void cadastrarVeiculoCargaMenu(Scanner scanner, Leitura leitura) {
        int repetir;
        boolean escolhaInvalida;
        do {
            veiculosDb.adicionarVeiculoCarga(cadastrarVeiculoCarga(scanner, leitura));

            do {
                repetir = validarEntradaInt(scanner, leitura, """
                        \nDeseja cadastrar novamente um veículo de carga?
                        1. Sim
                        2. Não: """);
                escolhaInvalida = repetir != 1 && repetir != 2;
                if (escolhaInvalida) System.out.println("\nEscolha uma opção válida");
            } while (escolhaInvalida);
        } while (repetir != 2);
    }

    public static Carga cadastrarVeiculoCarga(Scanner scanner, Leitura leitura) {
        Carga carga = new Carga();
        cadastrarVeiculo(carga, scanner, leitura);
        carga.setCargaMax(validarEntradaInt(scanner, leitura, "Carga máxima: "));
        carga.setTara(validarEntradaInt(scanner, leitura,"Tara (peso vazio) do veículo: "));
        return carga;
    }

    public static int validarEntradaInt(Scanner scanner, Leitura leitura, String message) {
        int entrada;
        do {
            System.out.print(message);
            String resposta = leitura.entDados(scanner.nextLine());
            try {
                entrada = Integer.parseInt(resposta);
                if (entrada > 0) { return entrada; }
                System.out.println("Digite um número inteiro maior que zero");
            } catch (NumberFormatException error) {
                System.out.println("Digite um número inteiro!");
            }
        } while (true);
    }

    public static float validarEntradaFloat(Scanner scanner, Leitura leitura, String message) {
        float entrada;
        do {
            System.out.print(message);
            String resposta = leitura.entDados(scanner.nextLine());
            try {
                entrada = Float.parseFloat(resposta);
                if (entrada > 0) { return entrada; }
                System.out.println("Digite um número maior que zero!");
            } catch (NumberFormatException error) {
                System.out.println("Digite um número!");
            }
        } while (true);
    }

    public static String validarEntradaString(Scanner scanner, Leitura leitura, String message) {
        String entrada;
        do {
            System.out.print(message);
            String resposta = leitura.entDados(scanner.nextLine());
            entrada = resposta;
            if(entrada.isBlank()) {
                System.out.println("Digite um valor válido!");
            } else {
                return resposta;
            }
        } while (true);
    }

    public static void sairDoSistema(Scanner scanner) {
        System.out.println("Até breve!");
        scanner.close();
    }

    public static void imprimirVeiculosPasseio() {
        System.out.println("Veículos de passeio cadastrados: " + veiculosDb.getVeiculosPasseio().size());
        System.out.println("\nVeículos de passeio");
        veiculosDb.getVeiculosPasseio().forEach( v -> {
            System.out.println(v);
            System.out.println("Função interface calcular() = " + v.calcular());
            System.out.println("Função herança calcVel() = " + v.calcVel(v.getVelocMax()));
        });
    }

    public static void imprimirVeiculosCarga() {
        System.out.println("Veículos de carga cadastrados: " + veiculosDb.getVeiculosCarga().size());
        System.out.println("\nVeículos de passeio");
        veiculosDb.getVeiculosCarga().forEach( v -> {
            System.out.println(v);
            System.out.println("Função interface calcular() = " + v.calcular());
            System.out.println("Função herança calcVel() = " + v.calcVel(v.getVelocMax()));
        });
    }

    public static void imprimirVeiculosPasseioPorPlaca(Scanner scanner) {
        System.out.print("Digite a placa do veículo de passeio que deseja buscar: ");
        Optional<Passeio> veiculo = pegarVeiculoPasseioPorPlaca(scanner.nextLine());
        if (veiculo.isPresent()) {
            System.out.println("Veiculo encontrado:");
            System.out.println(veiculo);
            System.out.println("Função interface calcular() = " + veiculo.get().calcular());
            System.out.println("Função herança calcVel() = " + veiculo.get().calcVel(veiculo.get().getVelocMax()));
        } else {
            System.out.println("Nenhum veículo de passeio encontrado");
        }
    }

    public static void imprimirVeiculosCargaPorPlaca(Scanner scanner) {
        System.out.print("Digite a placa do veículo de carga que deseja buscar: ");
        Optional<Carga> veiculo = pegarVeiculoCargaPorPlaca(scanner.nextLine());
        if (veiculo.isPresent()) {
            System.out.println("Veiculo encontrado:");
            System.out.println(veiculo);
            System.out.println("Função interface calcular() = " + veiculo.get().calcular());
            System.out.println("Função herança calcVel() = " + veiculo.get().calcVel(veiculo.get().getVelocMax()));
        } else {
            System.out.println("Nenhum veículo de carga encontrado");
        }
    }

    public static void excluirVeiculosPasseioPorPlaca(Scanner scanner) {
        System.out.print("Digite a placa do veículo de passeio que deseja remover: ");
        Optional<Passeio> veiculo = pegarVeiculoPasseioPorPlaca(scanner.nextLine());
        if (veiculo.isPresent()) {
            veiculosDb.getVeiculosPasseio().remove(veiculo.get());
            System.out.println("Veiculo removido");
        } else {
            System.out.println("Nenhum veículo de passeio encontrado\n");
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

    public static void excluirVeiculosCargaPorPlaca(Scanner scanner) {
        System.out.print("Digite a placa do veículo de carga que deseja remover: ");
        Optional<Carga> veiculo = pegarVeiculoCargaPorPlaca(scanner.nextLine());
        if (veiculo.isPresent()) {
            veiculosDb.getVeiculosCarga().remove(veiculo.get());
            System.out.println("Veiculo removido");
        } else {
            System.out.println("Nenhum veículo de carga encontrado\n");
        }
    }
}