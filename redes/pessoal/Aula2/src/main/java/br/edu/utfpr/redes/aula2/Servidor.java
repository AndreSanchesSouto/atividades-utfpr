package br.edu.utfpr.redes.aula2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Servidor extends Thread {

    private Socket conexao;

    public Servidor() {}

    public Servidor(Socket conexao) {
        this.conexao = conexao;
    }

    @Override
    public void run() {
        try {
            DataInputStream entrada = new DataInputStream(conexao.getInputStream());
            String frase = entrada.readUTF();

            String novaFrase = frase.toUpperCase();

            DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeUTF(novaFrase);
            conexao.close();

        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(52000);
            while (true) {
                System.out.println("Aguardando conexao...");
                Socket conexao = servidor.accept();

                Servidor theadtServidor = new Servidor(conexao);
                theadtServidor.start();
            }

        } catch(IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }

}
