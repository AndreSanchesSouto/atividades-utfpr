// @author André Sanches Souto

package local.redes;

import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.io.IOException;

public class Janela extends JFrame {

    private JTextField campoNome;
    private JTextField campoIdade;
    private JTextArea areaRetorno;

    public Janela() {
        setTitle("Cliente");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel labelNome = new JLabel("Nome");
        labelNome.setBounds(20, 10, 100, 25);
        add(labelNome);

        campoNome = new JTextField();
        campoNome.setBounds(20, 40, 540, 30);
        add(campoNome);

        JLabel labelIdade = new JLabel("Idade");
        labelIdade.setBounds(20, 80, 100, 25);
        add(labelIdade);

        campoIdade = new JTextField();
        campoIdade.setBounds(20, 110, 540, 30);
        add(campoIdade);

        JLabel labelRetorno = new JLabel("Retorno do Servidor");
        labelRetorno.setBounds(20, 150, 200, 25);
        add(labelRetorno);

        areaRetorno = new JTextArea();
        areaRetorno.setEditable(false);
        areaRetorno.setBounds(20, 180, 540, 130);
        add(areaRetorno);

        JButton botaoEnviar = new JButton("Enviar");
        botaoEnviar.setBounds(420, 330, 140, 35);
        botaoEnviar.addActionListener(e -> this.enviarDados());
        add(botaoEnviar);
    }

    private void enviarDados() {
        final String nome = campoNome.getText();
        final int idade = Integer.parseInt(campoIdade.getText());
        final Pessoa pessoa = new Pessoa(nome, idade);

        try {
            final String resposta = Cliente.conexao(pessoa);
            areaRetorno.setText("Recebeu do servidor:\n" + resposta);
        } catch (IOException | ClassNotFoundException exception) {
            areaRetorno.setText("Erro ao conectar com o servidor.");
        }
    }

}
