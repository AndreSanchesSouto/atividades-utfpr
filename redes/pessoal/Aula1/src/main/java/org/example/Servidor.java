package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private static ServerSocket servidor;
    private static Socket conexao;
    private static DataInputStream entrada;
    private static DataOutputStream saida;

    public static void main(String[] args) {
        try {
            servidor = new ServerSocket(55000);
            conexao = servidor.accept();

            int valor;
            entrada = new DataInputStream(conexao.getInputStream());
            valor = entrada.readInt();

            String resultado = valor > 10 ? "O valor é maior que dez" : "O valor é menor ou igual a dez";

            saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeUTF(resultado);

        } catch (IOException exception) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}