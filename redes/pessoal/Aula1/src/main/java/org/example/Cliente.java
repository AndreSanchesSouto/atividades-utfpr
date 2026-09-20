package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    private static Socket conexao;
    private static DataInputStream entrada;
    private static DataOutputStream saida;

    public static void main(String[] args) {

        try {
            conexao = new Socket("127.0.0.1", 55000);

            saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeInt(100);

            entrada = new DataInputStream(conexao.getInputStream());
            String resposta = entrada.readUTF();
            System.out.println("Resposta dos servidor: "+resposta);

        } catch (IOException exception) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
