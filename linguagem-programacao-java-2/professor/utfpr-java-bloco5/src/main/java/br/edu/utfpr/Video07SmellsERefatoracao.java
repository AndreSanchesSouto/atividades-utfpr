package br.edu.utfpr;

import br.edu.utfpr.dominio.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * VÍDEO 7 — Code smells e refatoração guiada: o antes e o depois completos
 * Requer Java 25 LTS
 */
public class Video07SmellsERefatoracao {

    // ANTES — o monolito da webconf 2, com os smells etiquetados

    /**
     * Smells presentes:
     * - LONG METHOD: um metodo faz leitura, parse, cache, cálculo e formatação.
     * - LONG PARAMETER LIST: seis parâmetros.
     * - FLAG ARGUMENT: 'somenteErros' e 'formatoCsv' mudam o comportamento.
     * - MAGIC NUMBERS: 7, 500, 1000 sem nome.
     * - DUPLICATED CODE: a comparação de status repetida.
     * - GLOBAL MUTABLE STATE: o cache estático (que vazou na aula ao vivo).
     */
    static final class VersaoComSmells {

        private static final Map<String, RegistroAcesso> CACHE = new HashMap<>();

        static String relatorio(List<String> linhas, boolean somenteErros, boolean formatoCsv,
                                int horaInicial, int horaFinal, long limiteDeLentidao) {

            long total = 0;
            long erros = 0;
            long lentas = 0;

            final Map<Integer, Long> porHora = new TreeMap<>();

            for (final String linha : linhas) {
                final String[] partes = linha.trim().split(" ");

                if (partes.length != 7) { // magic number
                    continue;
                }

                final int status;
                final long tempo;
                final LocalDateTime instante;

                try {
                    instante = LocalDateTime.parse(partes[1]);
                    status = Integer.parseInt(partes[5]);
                    tempo = Long.parseLong(partes[6]);
                } catch (RuntimeException ex) {
                    continue;
                }

                if (instante.getHour() < horaInicial || instante.getHour() > horaFinal) {
                    continue;
                }
                if (somenteErros && status < 500) { // magic number + flag argument
                    continue;
                }

                // estado global mutável
                CACHE.put(partes[0], new RegistroAcesso(partes[0], instante, partes[2], partes[3], partes[4], status, tempo));

                total++;
                if (status >= 500) { // magic number repetido
                    erros++;
                }
                if (tempo > limiteDeLentidao) {
                    lentas++;
                }
                porHora.merge(instante.getHour(), 1L, Long::sum);
            }

            if (formatoCsv) { // flag argument decidindo formato
                return "total,erros,lentas%n%d,%d,%d".formatted(total, erros, lentas);
            }
            return "Total: %d%nErros: %d%nLentas: %d".formatted(total, erros, lentas);
        }
    }

    // DEPOIS — as refatorações nomeadas, uma a uma

    // REFATORAÇÃO 1 — Introduce Parameter Object: a lista de seis parâmetros vira um record com nomes claros.
    record FiltroDeAnalise(int horaInicial, int horaFinal, long limiteDeLentidaoMs, boolean somenteErros) {

        static FiltroDeAnalise diaCompleto() {
            return new FiltroDeAnalise(0, 23, ConstantesDeLog.LIMITE_PADRAO_DE_LENTIDAO_MS, false);
        }

        boolean aceita(RegistroAcesso registro) {
            final int hora = registro.instante().getHour();

            if (hora < horaInicial || hora > horaFinal) {
                return false;
            }

            // REFATORAÇÃO 2 — Replace Magic Number with Named Constant: os números viraram constantes em ConstantesDeLog,
            // e as regras ehErroDeServidor()/ehLenta() moram no próprio RegistroAcesso
            return !somenteErros || registro.ehErroDeServidor();
        }
    }

    // REFATORAÇÃO 3 — Extract Class (parse virou classe própria)
    static final class ParserDeLinha {

        ResultadoParse parsear(String linha) {
            final String[] partes = linha.trim().split(" ");

            if (partes.length != ConstantesDeLog.QUANTIDADE_DE_CAMPOS_DA_LINHA) {
                return new LinhaInvalida(linha, "numero de campos inesperado");
            }

            try {
                return new LinhaValida(new RegistroAcesso(
                        partes[0],
                        LocalDateTime.parse(partes[1]),
                        partes[2],
                        partes[3],
                        partes[4],
                        Integer.parseInt(partes[5]),
                        Long.parseLong(partes[6])));
            } catch (RuntimeException ex) {
                return new LinhaInvalida(linha, ex.getMessage());
            }
        }
    }

    // REFATORAÇÃO 4 — Extract Method + Extract Class: o cálculo é uma função pura, testável sem I/O e sem estado global
    static final class AgregadorDeMetricas {

        Metricas agregar(List<RegistroAcesso> registros, FiltroDeAnalise filtro) {
            final List<RegistroAcesso> filtrados = registros.stream()
                    .filter(filtro::aceita)
                    .toList();

            final long erros = filtrados.stream()
                    .filter(RegistroAcesso::ehErroDeServidor)
                    .count();

            return new Metricas(
                    filtrados.size(),
                    erros,
                    filtrados.isEmpty() ? 0.0 : (erros * 100.0) / filtrados.size(),
                    filtrados.stream()
                            .filter(registro -> registro.ehLenta(filtro.limiteDeLentidaoMs()))
                            .count(),
                    filtrados.stream().collect(Collectors.groupingBy(
                            registro -> registro.instante().getHour(),
                            TreeMap::new,
                            Collectors.counting())));
        }
    }

    // REFATORAÇÃO 5 — Replace Flag Argument with Polymorphism: o boolean 'formatoCsv' virou uma implementação própria
    // (o OCP do vídeo 3) e flexível
    interface Formatador {
        String formatar(Metricas metricas);
    }

    static final class FormatadorTexto implements Formatador {
        @Override
        public String formatar(Metricas metricas) {
            return "Total: %d%nErros: %d%nLentas: %d".formatted(
                    metricas.totalDeRequisicoes(),
                    metricas.totalDeErros(),
                    metricas.requisicoesLentas());
        }
    }

    static final class FormatadorCsv implements Formatador {
        @Override
        public String formatar(Metricas metricas) {
            return "total,erros,lentas%n%d,%d,%d".formatted(
                    metricas.totalDeRequisicoes(),
                    metricas.totalDeErros(),
                    metricas.requisicoesLentas());
        }
    }

    // REFATORAÇÃO 6 — o orquestrador recebe suas dependências (DIP do vídeo 6).
    static final class GeradorDeRelatorio {

        private final ParserDeLinha parser;
        private final AgregadorDeMetricas agregador;
        private final Formatador formatador;

        GeradorDeRelatorio(ParserDeLinha parser, AgregadorDeMetricas agregador, Formatador formatador) {
            this.parser = parser;
            this.agregador = agregador;
            this.formatador = formatador;
        }

        String gerar(List<String> linhas, FiltroDeAnalise filtro) {

            final List<RegistroAcesso> registros = linhas.stream()
                    .map(parser::parsear)
                    .flatMap(resultado ->
                            switch (resultado) {
                                case LinhaValida valida -> Stream.of(valida.registro());
                                case LinhaInvalida _ -> Stream.<RegistroAcesso>empty();
                            })
                    .toList();

            return formatador.formatar(agregador.agregar(registros, filtro));
        }
    }

    void main() {
        final List<String> linhas = ConstantesDeLog.amostraDeLinhasCruas();

        IO.println("=== ANTES (monolito da Aula Virtual 2) ===");
        IO.println(VersaoComSmells.relatorio(linhas, false, false, 0, 23, 1_000));

        IO.println("\n=== DEPOIS (refatorado nos videos 2 a 6) ===");

        final GeradorDeRelatorio gerador = new GeradorDeRelatorio(
                new ParserDeLinha(),
                new AgregadorDeMetricas(),
                new FormatadorTexto()
        );

        IO.println(gerador.gerar(linhas, FiltroDeAnalise.diaCompleto()));

        IO.println("\n--- mesma saida, outro formato (sem tocar no calculo) ---");

        final GeradorDeRelatorio geradorCsv = new GeradorDeRelatorio(
                new ParserDeLinha(),
                new AgregadorDeMetricas(),
                new FormatadorCsv());

        IO.println(geradorCsv.gerar(linhas, FiltroDeAnalise.diaCompleto()));

        /*
         * E o que respondemos das perguntas do video 1:
         *
         * 1. Testar so o calculo?
         *    AgregadorDeMetricas, sem I/O
         * 2. Exportar em CSV?
         *    Trocar o Formatador injetado
         * 3. Trocar a fonte?
         *    Trocar o LeitorDeLinhas (video 6)
         * 4. O cache vazando?
         *    Ganhou dono (video 2) e contrato honesto com limite (video 4)
         *
         * Nenhuma dessas respostas exigiu sintaxe nova. Todas foram sobre ONDE colocar o codigo, e isso é design
         */
    }
}