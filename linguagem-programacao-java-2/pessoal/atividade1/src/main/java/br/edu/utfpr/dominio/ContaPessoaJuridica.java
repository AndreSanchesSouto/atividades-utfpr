package br.edu.utfpr.dominio;

import br.edu.utfpr.anotacoes.NaoNulo;
import br.edu.utfpr.anotacoes.Positivo;
import br.edu.utfpr.anotacoes.Tamanho;

import java.math.BigDecimal;

public final class ContaPessoaJuridica implements ContaBancaria {
    // TODO revisar e implementar corretamente conforme requisitos
    @NaoNulo(mensagem = "Agencia obrigatória")
    @Tamanho(max = 4)
    private final String agencia;

    @NaoNulo(mensagem = "Número obrigatória")
    private final String numero;

    @NaoNulo(mensagem = "Razão social obrigatória")
    private final String razaoSocial;

    @NaoNulo(mensagem = "CNPJ obrigatória")
    @Tamanho(max = 14)
    private final String cnpj;

    @NaoNulo(mensagem = "o capital social e obrigatorio")
    @Positivo(mensagem = "Número inválido")
    private final BigDecimal capitalSocial;

    public ContaPessoaJuridica(String agencia, String numero, String razaoSocial, String cnpj, BigDecimal capitalSocial) {
        this.agencia = agencia;
        this.numero = numero;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.capitalSocial = capitalSocial;
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

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public BigDecimal getCapitalSocial() {
        return capitalSocial;
    }
}
