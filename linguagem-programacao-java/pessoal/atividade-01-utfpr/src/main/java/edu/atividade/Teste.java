package edu.atividade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Teste {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int quantidadeCarros = validarEntradaInt(scanner, "Quantas vezes deseja fazer um veículo?: ");

        List<Veiculo> veiculos = new ArrayList<Veiculo>();

        for(int loop = 0; loop < quantidadeCarros; loop++) {
            Veiculo v = cadastrarVeiculo(scanner);
            System.out.println("Veículo cadastrado!\n");
            veiculos.add(v);
        }

        System.out.println("Total de veículos cadastrados: " + veiculos.toArray().length);
        System.out.println("Veículos cadastrados:\n" + veiculos);

    }

    public static Veiculo cadastrarVeiculo(Scanner scanner) {
        Veiculo v = new Veiculo();

        System.out.print("Marca: ");
        v.setMarca(scanner.nextLine());

        System.out.print("Modelo: ");
        v.setModelo(scanner.nextLine());

        System.out.print("Placa: ");
        v.setPlaca(scanner.nextLine());

        v.setQtdRodas(validarEntradaInt(scanner, "Qtd rodas: "));
        v.setVelocMax(validarEntradaFloat(scanner, "Velocidade máxima: "));
        v.getMotor().setPotencia(validarEntradaInt(scanner, "Potência do motor: "));
        v.getMotor().setQtdPist(validarEntradaInt(scanner, "Qtd pistões: "));

        return v;
    }

    public static Integer validarEntradaInt(Scanner scanner, String message) {
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

    public static Float validarEntradaFloat(Scanner scanner, String message) {
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