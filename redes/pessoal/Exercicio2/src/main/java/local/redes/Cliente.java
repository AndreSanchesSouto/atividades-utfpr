// @author André Sanches Souto

package local.redes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {

    private static Socket conexao;
    private static ObjectOutputStream saida;
    private static ObjectInputStream entrada;
    private static String resposta;

    public static String conexao(Pessoa pessoa) throws IOException, ClassNotFoundException {
        conexao = new Socket("127.0.0.1", 50000);
        saida = new ObjectOutputStream(conexao.getOutputStream());

        saida.writeObject(pessoa);
        saida.flush();

        entrada = new ObjectInputStream(conexao.getInputStream());
        resposta = (String) entrada.readObject();

        conexao.close();
        return resposta;
    }

    public static void main(String[] args) {
        Janela tela = new Janela();
        tela.setVisible(true);
    }
}