// @author André Sanches Souto

package local.redes;

import javax.swing.*;
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

            String cpf = JOptionPane.showInputDialog( null, "Digite um CPF para verificação:" );

            saida = new DataOutputStream(conexao.getOutputStream());
            saida.writeUTF(cpf);

            entrada = new DataInputStream(conexao.getInputStream());
            String resposta = entrada.readUTF();
            JOptionPane.showMessageDialog( null, "Resposta do servidor: " + resposta );

            conexao.close();
        } catch (IOException exception) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
