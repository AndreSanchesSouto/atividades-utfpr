package br.edu.utfpr.jakartaee.web;



import br.edu.utfpr.jakartaee.ejb.JogoEJB;
import br.edu.utfpr.jakartaee.ejb.RankingEJB;
import br.edu.utfpr.jakartaee.model.Jogador;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named("jogoBean")
@ViewScoped
public class JogoBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EJB
    private JogoEJB jogoEJB;

    @EJB
    private RankingEJB rankingEJB;

    private String nome;
    private int numero1;
    private int numero2;
    private Integer resposta;
    private String resultado;
    private boolean iniciado;

    @PostConstruct
    public void inicializar() {
        gerarPergunta();
    }

    public void iniciarJogo() {
        if (nome == null || nome.isBlank()) {
            resultado = "Informe seu nome para começar.";
            return;
        }

        nome = nome.trim();
        rankingEJB.registrarJogador(nome);
        iniciado = true;
        resultado = null;
        resposta = null;

        gerarPergunta();
    }

    public void verificarResposta() {
        if (!iniciado || resposta == null) {
            resultado = "Informe uma resposta válida.";
            return;
        }

        boolean acertou = jogoEJB.verificarResposta(
                numero1,
                numero2,
                resposta
        );

        if (acertou) {
            rankingEJB.adicionarPonto(nome);
            resultado = "Acertou! Você ganhou 1 ponto.";
        } else {
            resultado = "Errou! Tente novamente na próxima rodada.";
        }

        resposta = null;
        gerarPergunta();
    }

    private void gerarPergunta() {
        int[] numeros = jogoEJB.gerarPergunta();
        numero1 = numeros[0];
        numero2 = numeros[1];
    }

    public List<Jogador> getRanking() {
        return rankingEJB.listarRanking();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumero1() {
        return numero1;
    }

    public int getNumero2() {
        return numero2;
    }

    public Integer getResposta() {
        return resposta;
    }

    public void setResposta(Integer resposta) {
        this.resposta = resposta;
    }

    public String getResultado() {
        return resultado;
    }

    public boolean isIniciado() {
        return iniciado;
    }
}

