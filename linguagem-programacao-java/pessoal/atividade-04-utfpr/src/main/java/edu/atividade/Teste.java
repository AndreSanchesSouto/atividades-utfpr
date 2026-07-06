package edu.atividade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Teste {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int quantidadeCarros = validarEntradaInt(scanner, "Quantos veículos deseja fazer?: ");

        List<Veiculo> veiculos = new ArrayList<Veiculo>();
        for(int loop = 0; loop < quantidadeCarros; loop++) {
            int tipo = seletorTipoVeiculo(scanner);
            Veiculo veiculo;
            if (tipo == 1) {
                veiculo = new Passeio();
                cadastrarVeiculo(veiculo, scanner);
                cadastrarVeiculoPasseio((Passeio) veiculo, scanner);
                veiculos.add(veiculo);
                System.out.println("A velocidade do veículo de passeio é de: " +  veiculo.calcVel(veiculo.getVelocMax()) + "m/h");
            } else if(tipo == 2) {
                veiculo = new Carga();
                cadastrarVeiculo(veiculo, scanner);
                cadastrarVeiculoCarga((Carga) veiculo, scanner);
                veiculos.add(veiculo);
                System.out.println("A velocidade do veículo de carga é de: " +  veiculo.calcVel(veiculo.getVelocMax()) + "cm/h");
            } else {
                throw new RuntimeException("Erro não esperado!");
            }
            System.out.println("Veículo cadastrado!\n");
        }
        scanner.close();

        imprimirDados(veiculos);
    }

    public static void imprimirDados(List<Veiculo> veiculos) {
        System.out.println("Total de veículos cadastrados: " + veiculos.size());

        System.out.println("Veículos de passeio cadastrados: " + veiculos
                .stream()
                .filter(v -> v instanceof Passeio)
                .count()
        );
        System.out.println("Veículos de carga cadastrados: " + veiculos
                .stream()
                .filter(v -> v instanceof Carga)
                .count()
        );
        System.out.println("\n==================================================");
        System.out.println("Veículos de passeio");
        veiculos
                .stream()
                .filter(v -> v instanceof Passeio)
                .forEach(v -> System.out.println(v));

        System.out.println("\n==================================================");
        System.out.println("Veículos de carga");
        veiculos
                .stream()
                .filter(v -> v instanceof Carga)
                .forEach(v -> System.out.println(v));
    }

    public static int seletorTipoVeiculo(Scanner scanner) {
        do {
            int veiculoTipo = validarEntradaInt(scanner, """
                    Qual tipo de veículo deseja?
                    [1] - Passeio
                    [2] - Carga
                    Escolha: """);
            switch (veiculoTipo) {
                case 1: return 1;
                case 2: return 2;
                default: System.out.println("Digite uma opção válida!");
            }
        } while (true);

    }

    public static void cadastrarVeiculo(Veiculo v, Scanner scanner) {
        System.out.print("Marca: ");
        v.setMarca(scanner.nextLine());

        System.out.print("Modelo: ");
        v.setModelo(scanner.nextLine());

        System.out.print("Placa: ");
        v.setPlaca(scanner.nextLine());

        System.out.print("Cor: ");
        v.setCor(scanner.nextLine());

        v.setQtdRodas(validarEntradaInt(scanner, "Qtd rodas: "));
        v.setVelocMax(validarEntradaFloat(scanner, "Velocidade máxima: "));
        v.getMotor().setPotencia(validarEntradaInt(scanner, "Potência do motor: "));
        v.getMotor().setQtdPist(validarEntradaInt(scanner, "Qtd pistões: "));
    }

    public static void cadastrarVeiculoPasseio(Passeio passeio, Scanner scanner) {
        passeio.setQtdPassageiros(validarEntradaInt(scanner, "Quantidade de passageiros: "));
    }

    public static void cadastrarVeiculoCarga(Carga carga, Scanner scanner) {
        carga.setCargaMax(validarEntradaInt(scanner, "Carga máxima: "));
        carga.setTara(validarEntradaInt(scanner, "Tara (peso vazio) do veículo: "));
    }

    public static int validarEntradaInt(Scanner scanner, String message) {
        int entrada;
        do {
            System.out.print(message);
            String resposta = scanner.nextLine();
            try {
                entrada = Integer.parseInt(resposta);
                if (entrada > 0) { return entrada; }
                    System.out.println("Digite um número inteiro maior que zero");
            } catch (NumberFormatException error) {
                System.out.println("Digite um número inteiro!");
            }
        } while (true);
    }

    public static float validarEntradaFloat(Scanner scanner, String message) {
        float entrada;
        do {
            System.out.print(message);
            String resposta = scanner.nextLine();
            try {
                entrada = Float.parseFloat(resposta);
                if (entrada > 0) { return entrada; }
                System.out.println("Digite um número maior que zero!");
            } catch (NumberFormatException error) {
                System.out.println("Digite um número!");
            }
        } while (true);
    }
}