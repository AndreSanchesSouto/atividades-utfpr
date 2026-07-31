package br.edu.utfpr;

import br.edu.utfpr.dominio.ConstantesDeLog;
import br.edu.utfpr.dominio.Metricas;
import br.edu.utfpr.dominio.RegistroAcesso;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * VÍDEO 3 — O: Aberto/Fechado (OCP)
 * Requer Java 25 LTS
 */
public class Video03AbertoFechado {

    // ANTES (violação)
    // Cada formato novo obriga a EDITAR este metodo: risco de quebrar o que já funcionava, e o metodo cresce sem parar
    static String formatarViolandoOcp(Metricas metricas, String formato) {
        if (formato.equals("texto")) {
            return "Total: " + metricas.totalDeRequisicoes();
        } else if (formato.equals("csv")) {
            return "total\n" + metricas.totalDeRequisicoes();
        }
        // ...e cada novo formato = mais um 'else if' AQUI.
        throw new IllegalArgumentException("formato desconhecido: " + formato);
    }
    // Distrator comum: trocar if/else por switch NÃO resolve o OCP — cada formato é uma implementação

    interface FormatadorDeRelatorio {
        String nome();

        String formatar(Metricas metricas);
    }

    static final class FormatadorTexto implements FormatadorDeRelatorio {
        @Override
        public String nome() {
            return "texto";
        }

        @Override
        public String formatar(Metricas metricas) {
            return "Total: %d | Erros: %d | Taxa: %.2f%%".formatted(
                    metricas.totalDeRequisicoes(),
                    metricas.totalDeErros(),
                    metricas.taxaDeErro());
        }
    }

    static final class FormatadorCsv implements FormatadorDeRelatorio {
        @Override
        public String nome() {
            return "csv";
        }

        @Override
        public String formatar(Metricas metricas) {
            final StringBuilder saida = new StringBuilder("hora,requisicoes\n");
            metricas.requisicoesPorHora().forEach((hora, quantidade) ->
                    saida.append(hora).append(',').append(quantidade).append('\n'));
            return saida.toString();
        }
    }

    // NOVO formato acrescentado SEM modificar uma linha do que existe acima:
    static final class FormatadorJson implements FormatadorDeRelatorio {
        @Override
        public String nome() {
            return "json";
        }

        @Override
        public String formatar(Metricas metricas) {
            return "{\"total\":%d,\"erros\":%d,\"taxaErro\":%.2f,\"lentas\":%d}".formatted(
                    metricas.totalDeRequisicoes(),
                    metricas.totalDeErros(),
                    metricas.taxaDeErro(),
                    metricas.requisicoesLentas());
        }
    }

    // sealed: extensão CONTROLADA (Bloco 1)
    // Quando o conjunto de variações deve ser FECHADO e conhecido, sealed autoriza apenas certos tipos — e o switch
    // exaustivo denuncia, em tempo de COMPILAÇÃO, qualquer tipo novo que ninguém tratou
    sealed interface ClassificacaoDaResposta permits RespostaComSucesso, ErroDoCliente, ErroDoServidor {
    }

    record RespostaComSucesso(int status) implements ClassificacaoDaResposta {
    }

    record ErroDoCliente(int status) implements ClassificacaoDaResposta {
    }

    record ErroDoServidor(int status, boolean exigeAlerta) implements ClassificacaoDaResposta {
    }

    static ClassificacaoDaResposta classificar(RegistroAcesso registro) {
        final int status = registro.status();
        if (status >= ConstantesDeLog.STATUS_MINIMO_DE_ERRO_SERVIDOR) {
            return new ErroDoServidor(status, status == 503);
        }
        if (status >= 400) {
            return new ErroDoCliente(status);
        }
        return new RespostaComSucesso(status);
    }

    static String descrever(ClassificacaoDaResposta classificacao) {
        return switch (classificacao) { // sem 'default': o compilador garante
            case RespostaComSucesso sucesso -> "OK (" + sucesso.status() + ")";
            case ErroDoCliente erroDoCliente -> "erro do cliente (" + erroDoCliente.status() + ")";
            case ErroDoServidor erroDoServidor ->
                    "ERRO DE SERVIDOR (%d)%s".formatted(erroDoServidor.status(), erroDoServidor.exigeAlerta() ? " -- acionar plantao" : "");
        };
    }

    void main() {
        final List<RegistroAcesso> registros = ConstantesDeLog.amostraDeRegistros();

        // Métricas montadas aqui de forma direta; no vídeo 2 isso é do agregador.
        final Map<Integer, Long> porHora = new TreeMap<>();
        registros.forEach(registro -> porHora.merge(registro.instante().getHour(), 1L, Long::sum));

        final long erros = registros.stream()
                .filter(RegistroAcesso::ehErroDeServidor)
                .count();

        final Metricas metricas = new Metricas(
                registros.size(),
                erros,
                (erros * 100.0) / registros.size(),
                registros.stream()
                        .filter(registro -> registro.ehLenta(ConstantesDeLog.LIMITE_PADRAO_DE_LENTIDAO_MS))
                        .count(),
                porHora);

        // O código cliente NÃO muda quando um formato novo aparece: basta acrescentar a implementação à lista.
        final List<FormatadorDeRelatorio> formatadores = List.of(
                new FormatadorTexto(),
                new FormatadorCsv(),
                new FormatadorJson());

        for (final FormatadorDeRelatorio formatador : formatadores) {
            IO.println("--- " + formatador.nome() + " ---");
            IO.println(formatador.formatar(metricas));
        }

        IO.println("--- sealed: extensao controlada ---");
        registros.forEach(registro -> IO.println("  " + registro.identificador() + ": " + descrever(classificar(registro))));
    }
}