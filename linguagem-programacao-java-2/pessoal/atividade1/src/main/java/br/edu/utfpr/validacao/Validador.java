package br.edu.utfpr.validacao;

import br.edu.utfpr.anotacoes.NaoNulo;
import br.edu.utfpr.anotacoes.Positivo;
import br.edu.utfpr.anotacoes.Tamanho;
import br.edu.utfpr.dominio.ContaBancaria;
import br.edu.utfpr.dominio.ContaPessoaFisica;
import br.edu.utfpr.dominio.ContaPessoaJuridica;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Validador {

    private static final int IDADE_MINIMA = 18;
    private static final BigDecimal CAPITAL_SOCIAL_MINIMO = new BigDecimal("10000");

    public ResultadoValidacao validar(ContaBancaria conta) {
        final List<Violacao> violacoes = new ArrayList<>();

        if (conta == null) {
            return ResultadoValidacao.de(
                    List.of(
                            new Violacao("(objeto)", "o objeto a validar e nulo")
                    )
            );
        }
        // TODO ver requisito 1, implementar aqui
        violacoes.addAll(validarCampos(conta));
        violacoes.addAll(regrasDeNegocio(conta));

        return ResultadoValidacao.de(violacoes);
    }

    public List<Violacao> validarCampos(Object objeto) {

        final List<Violacao> violacoes = new ArrayList<>();

        // TODO ver requisito 2, implementar aqui
        for(Field field : objeto.getClass().getDeclaredFields()) {
            verificarNaoNulo(field, objeto, violacoes);
            verificarTamanho(field, objeto, violacoes);
            verificarPositivo(field, objeto, violacoes);
        }
        return violacoes;
    }

    private void verificarNaoNulo(Field campo, Object valor, List<Violacao> acc) {
        // TODO implementar regra de negocio de nao nulo
        if(!campo.isAnnotationPresent(NaoNulo.class)) return;

        campo.setAccessible(true);

        try {
            final Object valorString = campo.get(valor);
            if(valorString == null) {
                acc.add(new Violacao(
                        campo.getName(),
                        campo.getAnnotation(NaoNulo.class).mensagem()
                ));
            }
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private void verificarTamanho(Field campo, Object valor, List<Violacao> acc) {
        // TODO implementar regra de negocio de tamanho
        if(!campo.isAnnotationPresent(Tamanho.class)) return;

        campo.setAccessible(true);

        try {
            final Tamanho tamanho = campo.getAnnotation(Tamanho.class);
            final String valorString = campo.get(valor).toString();
            if(valorString.length() != tamanho.max()) {
                acc.add(new Violacao(
                        campo.getName(),
                        campo.getAnnotation(Tamanho.class).mensagem()
                ));
            }
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception.getMessage());
        }


    }

    private void verificarPositivo(Field campo, Object valor, List<Violacao> acc) {
        // TODO implementar regra de negocio de positivo
        try {
            if(!campo.isAnnotationPresent(Positivo.class)) return;

            final Positivo positivo = campo.getAnnotation(Positivo.class);
            if (positivo == null) return;

            campo.setAccessible(true);
            Object objeto = campo.get(valor);

            if(objeto == null) return;

            if(
                    switch (valor) {
                        case Integer integer -> integer > 0;
                        case Long longg -> longg > 0;
                        case Double doublee -> doublee > 0;
                        case BigDecimal bigDecimal -> bigDecimal.compareTo(BigDecimal.valueOf(0)) > 0;
                        default -> false;
                    }
            ) {
                acc.add(new Violacao(campo.getName(), positivo.mensagem()));
            }

        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private List<Violacao> regrasDeNegocio(ContaBancaria conta) {
        // TODO implementar as regras de negocio por tipo

        return switch (conta) {
            case ContaPessoaFisica contaPessoaFisica -> validarMaioridade(contaPessoaFisica);
            case ContaPessoaJuridica contaPessoaJuridica -> validarCapitalSocial(contaPessoaJuridica.getCapitalSocial());
        };
    }

    private List<Violacao> validarMaioridade(ContaPessoaFisica pf) {
        // TODO implementar regra de negocio de maioridade
        if (pf.getDataNascimento() == null) {
            return List.of();
        }
        final int age = Period.between(pf.getDataNascimento(), LocalDate.now()).getYears();
        if(age < IDADE_MINIMA)
            return List.of(new Violacao(Violacao.DATA_NASCIMENTO_CAMPO, Violacao.DATA_NASCIMENTO_MENSAGEM));
        return List.of();
    }

    private List<Violacao> validarCapitalSocial(BigDecimal capital) {
        // TODO implementar regra de negocio de capital social
        if (capital == null) {
            return List.of();
        }
        if(capital.compareTo(CAPITAL_SOCIAL_MINIMO) < 0) {
            return List.of(new Violacao(Violacao.CAPITAL_SOCIAL_CAMPO, Violacao.CAPITAL_SOCIAL_MENSAGEM));
        }
        return List.of();
    }
}
