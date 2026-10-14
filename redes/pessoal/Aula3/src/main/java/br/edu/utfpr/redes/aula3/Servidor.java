package br.edu.utfpr.redes.aula3;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Servidor {

    private static ServerSocket servidor;
    private static Socket conexao;
    private static ObjectInputStream entrada;
    private static DataOutputStream saida;

    public static void main(String[] args) {
        try {
            servidor = new ServerSocket(55000);
            System.out.println("Agardando conexao...");
            conexao = servidor.accept();

            entrada = new ObjectInputStream(conexao.getInputStream());

            Pessoa pessoa = (Pessoa) entrada.readObject();
            System.out.println("Nome: " + pessoa.getNome());
            System.out.println("Idade: " + pessoa.getIdade());

            saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeUTF("Objeto recebido");

            conexao.close();
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }

}
