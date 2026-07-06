package br.edu.utfpr.dominio;

import br.edu.utfpr.anotacoes.NaoNulo;
import br.edu.utfpr.anotacoes.Tamanho;
import java.time.LocalDate;

public final class ContaPessoaFisica implements ContaBancaria {
    // TODO revisar e implementar corretamente conforme requisitos
    @NaoNulo(mensagem = "Agencia obrigatória")
    @Tamanho(max = 4)
    final private String agencia;

    @NaoNulo(mensagem = "Número obrigatório")
    final private String numero;

    @NaoNulo(mensagem = "Titular obrigatório")
    final private String titular;

    @NaoNulo(mensagem = "CPF obrigatório")
    @Tamanho(max = 11)
    final private String cpf;

    @NaoNulo(mensagem = "Email obrigatório")
    final private String email;

    @NaoNulo(mensagem = "a data de nascimento e obrigatoria")
    final private LocalDate dataNascimento;

    public ContaPessoaFisica(String agencia, String numero, String titular, String cpf, String email, LocalDate dataNascimento) {
        this.agencia = agencia;
        this.numero = numero;
        this.titular = titular;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String agencia() {
        return this.agencia;
    }

    @Override
    public String numero() {
        return this.numero;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}
