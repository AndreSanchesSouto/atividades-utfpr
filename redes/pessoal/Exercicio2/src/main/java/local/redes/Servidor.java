// @author André Sanches Souto

package local.redes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private static ServerSocket servidor;
    private static Socket conexao;
    private static ObjectInputStream entrada;
    private static ObjectOutputStream saida;

    public static void main(String[] args) {
        try {
            servidor = new ServerSocket(50000);
            System.out.println("Servidor aguardando conexão...");

            conexao = servidor.accept();

            entrada = new ObjectInputStream(conexao.getInputStream());

            Pessoa pessoa = (Pessoa) entrada.readObject();

            System.out.println("Dados recebidos:");
            System.out.println("Nome: " + pessoa.getNome());
            System.out.println("Idade: " + pessoa.getIdade());

            saida = new ObjectOutputStream(conexao.getOutputStream());

            saida.writeObject("Objeto recebido corretamente!\n" +
                    "Nome: " + pessoa.getNome() + "\n" +
                    "Idade: " + pessoa.getIdade() + "\n"
            );

            saida.flush();
            saida.close();
            entrada.close();
            conexao.close();
            servidor.close();

            System.out.println("Servidor encerrado.");
        } catch (IOException | ClassNotFoundException exception) {
            System.err.println(exception.getMessage());
        }
    }
}