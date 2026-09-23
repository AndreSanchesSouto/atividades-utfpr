package br.edu.utfpr.crudcidade.visao;

public final class Cidade {

    private final String nome;
    private final String estado;

    public Cidade(String nome, String estado) {
        this.nome = nome;
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public String getEstado() {
        return estado;
    }
}
