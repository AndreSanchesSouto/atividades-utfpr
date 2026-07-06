package edu.atividade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Teste {
    static List<Carga> veiculosCarga = new ArrayList<Carga>();
    static List<Passeio> veiculosPasseio = new ArrayList<Passeio>();

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
                    7. Sair do Sistema: """);
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
                case 7: sairDoSistema(scanner);
                break;
                default: System.out.println("Digite uma opção válida!");
            }
        } while (escolha != 7);
    }

    public static void cadastrarVeiculo(Veiculo veiculo, Scanner scanner, Leitura leitura) {
        System.out.print("Marca: ");
        veiculo.setMarca(scanner.nextLine());

        System.out.print("Modelo: ");
        veiculo.setModelo(scanner.nextLine());

        System.out.print("Placa: ");
        veiculo.setPlaca(validarPlaca(scanner, leitura));

        System.out.print("Cor: ");
        veiculo.setCor(scanner.nextLine());

        veiculo.setQtdRodas(validarEntradaInt(scanner, leitura, "Qtd rodas: "));
        veiculo.setVelocMax(validarEntradaFloat(scanner, leitura, "Velocidade máxima: "));
        veiculo.getMotor().setPotencia(validarEntradaInt(scanner, leitura,"Potência do motor: "));
        veiculo.getMotor().setQtdPist(validarEntradaInt(scanner, leitura, "Qtd pistões: "));
    }

    public static String validarPlaca(Scanner scanner, Leitura leitura) {
        String placa = scanner.nextLine();
        if (veiculosCarga.stream().anyMatch(veiculo -> veiculo.getPlaca().equals(placa)) ||
            veiculosPasseio.stream().anyMatch(veiculo -> veiculo.getPlaca().equals(placa))
        ) {
            System.out.printf("\nError: Placa '%s'1 já cadastrada!\n\n", placa);
            menuSistema(scanner, leitura);
        }
        return placa;

    }

    public static void cadastrarVeiculoPasseioMenu(Scanner scanner, Leitura leitura) {
        int repetir;
        boolean escolhaInvalida;
        do {
            veiculosPasseio.add(cadastrarVeiculoPasseio(scanner, leitura));
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
            veiculosCarga.add(cadastrarVeiculoCarga(scanner, leitura));
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

    public static void sairDoSistema(Scanner scanner) {
        System.out.println("Até breve!");
        scanner.close();
        System.exit(0);
    }

    public static void imprimirVeiculosPasseio() {
        System.out.println("Veículos de passeio cadastrados: " + veiculosPasseio.size());
        System.out.println("\nVeículos de passeio");
        veiculosPasseio.forEach( v -> {
            System.out.println(v);
            System.out.println("Função interface calcular() = " + v.calcular());
            System.out.println("Função herança calcVel() = " + v.calcVel(v.getVelocMax()));
        });
    }

    public static void imprimirVeiculosCarga() {
        System.out.println("Veículos de carga cadastrados: " + veiculosCarga.size());
        System.out.println("\nVeículos de passeio");
        veiculosCarga.forEach( v -> {
            System.out.println(v);
            System.out.println("Função interface calcular() = " + v.calcular());
            System.out.println("Função herança calcVel() = " + v.calcVel(v.getVelocMax()));
        });
    }

    public static void imprimirVeiculosPasseioPorPlaca(Scanner scanner) {
        System.out.print("Digite a placa do veículo de passeio que deseja buscar: ");
        String procurarPlaca = scanner.nextLine();
        Optional<Passeio> veiculo = veiculosPasseio
                .stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(procurarPlaca))
                .findAny();
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
        String procurarPlaca = scanner.nextLine();
        Optional<Carga> veiculo = veiculosCarga
                .stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(procurarPlaca))
                .findAny();
        if (veiculo.isPresent()) {
            System.out.println("Veiculo encontrado:");
            System.out.println(veiculo);
            System.out.println("Função interface calcular() = " + veiculo.get().calcular());
            System.out.println("Função herança calcVel() = " + veiculo.get().calcVel(veiculo.get().getVelocMax()));
        } else {
            System.out.println("Nenhum veículo de carga encontrado");
        }
    }
}