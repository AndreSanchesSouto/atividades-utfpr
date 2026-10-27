package br.edu.utfpr.jakartaee.model;

import java.io.Serial;
import java.io.Serializable;

public class Jogador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private int pontuacao;

    public Jogador() {}

    public Jogador(String nome) {
        this.nome = nome;
        this.pontuacao = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void adicionarPonto() {
        this.pontuacao++;
    }
}
