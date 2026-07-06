package br.edu.utfpr.dominio;

public sealed interface ContaBancaria
        permits ContaPessoaFisica, ContaPessoaJuridica
{

    // TODO revisar e implementar corretamente conforme requisitos

    String agencia();

    String numero();
}
