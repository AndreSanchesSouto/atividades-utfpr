package br.edu.utfpr.redes.aula3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Cliente {

    private static Socket conexao;
    private static ObjectOutputStream saida;
    private static DataInputStream entrada;

    public static void main(String[] args) {
        try {
            conexao = new Socket("localhost", 55000);

            Pessoa pessoa = new Pessoa();
            pessoa.setNome("Andre");
            pessoa.setIdade(13);

            saida = new ObjectOutputStream(conexao.getOutputStream());
            saida.writeObject(pessoa);

            entrada = new DataInputStream(conexao.getInputStream());
            String responsta = entrada.readUTF();
            System.out.print("Resposta do servidor: " + responsta);

            conexao.close();
        } catch (IOException exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
    }

}
