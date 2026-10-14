package br.edu.utfpr.redes.aula2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Cliente {

    private static Socket conexao;
    private static DataInputStream entrada;
    private static DataOutputStream saida;

    public static void main(String[] args) {
        try {
            conexao = new Socket("localhost", 52000);
            System.out.println("Digite uma frase: ");
            Scanner scanner = new Scanner(System.in);
            String frase = scanner.nextLine();
            saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeUTF(frase);
            scanner.close();

            entrada = new DataInputStream(conexao.getInputStream());
            String resposta = entrada.readUTF();
            System.out.println("Resposta do servidor: " + resposta);
        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }

}
