// @author André Sanches Souto

package local.redes;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Servidor extends Thread {

    private Socket conexao;

    public Servidor(Socket conexao) {
        this.conexao = conexao;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream entrada = new ObjectInputStream(conexao.getInputStream());

            Pessoa pessoa = (Pessoa) entrada.readObject();

            System.out.println("Dados recebidos:");
            System.out.println("Nome: " + pessoa.getNome());
            System.out.println("Idade: " + pessoa.getIdade());

            ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream());

            saida.writeObject("Objeto recebido corretamente!\n" +
                    "Nome: " + pessoa.getNome() + "\n" +
                    "Idade: " + pessoa.getIdade() + "\n"
            );

        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }

    }

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(50000);

            while (true) {
                System.out.println("Servidor aguardando conexão...");

                Socket conexao = servidor.accept();
                Servidor tServidor = new Servidor(conexao);
                tServidor.start();
            }

        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
}