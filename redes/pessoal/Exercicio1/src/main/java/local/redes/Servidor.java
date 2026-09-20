// @author André Sanches Souto

package local.redes;

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

    public static void main(String args[]) {
        try {
            servidor = new ServerSocket(55000);
            conexao = servidor.accept();

            entrada = new DataInputStream(conexao.getInputStream());
            String cpf = entrada.readUTF().trim().replaceAll("\\D", "");

            String resultado = cpfEhValido(cpf) ? "Este CPF é válido" : "Este CPF é inválido";
            saida = new DataOutputStream(conexao.getOutputStream());

            saida.writeUTF(resultado);
            conexao.close();
            servidor.close();

        } catch (IOException exception) {
            Logger.getLogger(Servidor.class.getName()) .log(Level.SEVERE, null, exception);
        }
    }

    private static boolean cpfEhValido(String cpf) {
        if (cpf.length() != 11 || cpf.equals("00000000000")) return false;

        char[] cpfChar = cpf.toCharArray();

        int somador = 0;
        for(int valor = 9; valor >= 1; valor--) {
            somador += Character.getNumericValue(cpfChar[9 - valor]) * (valor + 1);
        }

        int resto = somador % 11;

        int primeiroValidador = (resto < 2) ? 0 : (11 - resto);

        somador = 0;

        for (int valor = 10; valor >=1; valor--) {
            int indice = 10 - valor;

            somador += (indice == 9)
                    ? primeiroValidador * (valor + 1)
                    : Character.getNumericValue(cpfChar[indice]) * (valor + 1);

        }

        resto = somador % 11;

        int segundoValidador = (resto < 2) ? 0 : (11 - resto);

        return primeiroValidador == Character.getNumericValue(cpfChar[9])
                && segundoValidador == Character.getNumericValue(cpfChar[10]);
    }

}