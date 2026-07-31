package br.edu.utfpr;

import br.edu.utfpr.dominio.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * VÍDEO 2 — S: Responsabilidade Única (SRP)
 * Requer Java 25 LTS
 */
public class Video02ResponsabilidadeUnica {

    // COLABORADOR 1: obter as linhas, muda quando a FONTE muda (arquivo, fila, banco, HTTP).
    interface LeitorDeLinhas {
        List<String> ler();
    }

    // COLABORADOR 2: converter linha em registro, muda quando o FORMATO da linha muda.
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
            } catch (final RuntimeException excecao) {
                return new LinhaInvalida(linha, excecao.getMessage());
            }
        }
    }

    // COLABORADOR 3: o CACHE, agora com dono e com limite, muda quando a POLÍTICA DE RETENÇÃO muda.
    // Na aula ao vivo essa política não existia porque não existia esta classe
    static final class CacheDeRegistros {

        private final int capacidadeMaxima;
        private final Map<String, RegistroAcesso> conteudo;

        CacheDeRegistros(int capacidadeMaxima) {
            this.capacidadeMaxima = capacidadeMaxima;

            // LinkedHashMap com removeEldestEntry: descarta o mais antigo quando passa da capacidade
            // O limite fica EXPLÍCITO no código
            this.conteudo = new LinkedHashMap<>(16, 0.75f, false) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, RegistroAcesso> maisAntigo) {
                    return size() > capacidadeMaxima;
                }
            };
        }

        void guardar(RegistroAcesso registro) {
            conteudo.put(registro.identificador(), registro);
        }

        Optional<RegistroAcesso> buscar(String identificador) {
            return Optional.ofNullable(conteudo.get(identificador));
        }

        int quantidade() {
            return conteudo.size();
        }

        int capacidade() {
            return capacidadeMaxima;
        }
    }

    // COLABORADOR 4: calcular métricas
    // Muda quando as REGRAS de cálculo mudam, não sabe de onde vêm os dados nem para onde vai o resultado — por isso é
    // testável sozinho
    static final class AgregadorDeMetricas {

        Metricas agregar(List<RegistroAcesso> registros) {
            final long total = registros.size();

            final long erros = registros.stream()
                    .filter(RegistroAcesso::ehErroDeServidor)
                    .count();

            final long lentas = registros.stream()
                    .filter(registro -> registro.ehLenta(
                            ConstantesDeLog.LIMITE_PADRAO_DE_LENTIDAO_MS))
                    .count();

            final double taxaDeErro = total == 0 ? 0.0 : (erros * 100.0) / total;

            final Map<Integer, Long> porHora = registros.stream()
                    .collect(Collectors.groupingBy(
                            registro -> registro.instante().getHour(),
                            TreeMap::new,
                            Collectors.counting()));

            return new Metricas(total, erros, taxaDeErro, lentas, porHora);
        }
    }

    // COLABORADOR 5: apresentar o resultado, muda quando o FORMATO de saída muda
    // (No vídeo 3 isso vira o ponto de extensão do OCP)
    interface FormatadorDeRelatorio {
        String formatar(Metricas metricas);
    }

    static final class FormatadorTexto implements FormatadorDeRelatorio {
        @Override
        public String formatar(Metricas metricas) {

            final StringBuilder saida = new StringBuilder("=== RELATORIO ===\n");

            saida.append("Total: ").append(metricas.totalDeRequisicoes()).append('\n');
            saida.append("Erros: ").append(metricas.totalDeErros()).append('\n');
            saida.append("Taxa de erro: %.2f%%%n".formatted(metricas.taxaDeErro()));
            saida.append("Lentas: ").append(metricas.requisicoesLentas()).append('\n');

            metricas.requisicoesPorHora()
                    .forEach((hora, quantidade) -> saida.append("  %02dh -> %d%n".formatted(hora, quantidade)));

            return saida.toString();
        }
    }

    // ORQUESTRADOR: só coordena, não faz o trabalho
    static final class GeradorDeRelatorio {

        private final LeitorDeLinhas leitor;
        private final ParserDeLinha parser;
        private final CacheDeRegistros cache;
        private final AgregadorDeMetricas agregador;
        private final FormatadorDeRelatorio formatador;

        GeradorDeRelatorio(LeitorDeLinhas leitor,
                           ParserDeLinha parser,
                           CacheDeRegistros cache,
                           AgregadorDeMetricas agregador,
                           FormatadorDeRelatorio formatador) {
            this.leitor = leitor;
            this.parser = parser;
            this.cache = cache;
            this.agregador = agregador;
            this.formatador = formatador;
        }

        String gerar() {
            final List<RegistroAcesso> registros = leitor.ler().stream()
                    .map(parser::parsear)
                    .flatMap(resultado -> switch (resultado) {
                        case LinhaValida valida -> Stream.of(valida.registro());
                        case LinhaInvalida _ -> Stream.<RegistroAcesso>empty();
                    })
                    .toList();

            registros.forEach(cache::guardar);

            return formatador.formatar(agregador.agregar(registros));
        }
    }

    void main() {
        // Um leitor EM MEMÓRIA: prova que agora dá para rodar sem tocar em disco
        final LeitorDeLinhas leitorEmMemoria = ConstantesDeLog::amostraDeLinhasCruas;

        final CacheDeRegistros cache = new CacheDeRegistros(3); // limite pequeno de propósito

        final GeradorDeRelatorio gerador = new GeradorDeRelatorio(
                leitorEmMemoria,
                new ParserDeLinha(),
                cache,
                new AgregadorDeMetricas(),
                new FormatadorTexto());

        IO.println(gerador.gerar());

        IO.println("\n--- e o cache? ---");
        IO.println("Capacidade: " + cache.capacidade() + " | retidos agora: " + cache.quantidade() + " (5 registros entraram, so os 3 mais recentes ficaram)");
    }
}