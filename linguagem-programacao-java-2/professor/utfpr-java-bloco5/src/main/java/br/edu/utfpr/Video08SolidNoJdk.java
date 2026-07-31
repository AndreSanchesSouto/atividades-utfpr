package br.edu.utfpr;

import br.edu.utfpr.dominio.ConstantesDeLog;
import br.edu.utfpr.dominio.RegistroAcesso;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;

/**
 * VÍDEO 8 — SOLID no próprio JDK (estudo de caso) e fechamento do curso
 * Requer Java 25 LTS
 */
public class Video08SolidNoJdk {

    void main() {
        final List<RegistroAcesso> registros = ConstantesDeLog.amostraDeRegistros();

        // ISP: interfaces funcionais com UM metodo, por exemplo comparator tem um único metodo abstrato.
        // Se tivesse oito, não existiriam lambdas nem composição com thenComparing
        IO.println("== ISP: Comparator ==");
        final Comparator<RegistroAcesso> ordenacao = Comparator
                .comparingLong(RegistroAcesso::tempoRespostaMs).reversed()
                .thenComparing(RegistroAcesso::identificador);

        registros.stream().sorted(ordenacao)
                .limit(3)
                .forEach(registro -> IO.println("  %s %s %dms".formatted(registro.identificador(), registro.rota(), registro.tempoRespostaMs())));

        // OCP: Collector é ponto de extensão, a Stream API está FECHADA para modificação (você não edita o JDK) e
        // ABERTA para extensão (você escreve o seu Collector) — visto no bloco 4
        IO.println("\n== OCP: Collector customizado ==");

        final Collector<RegistroAcesso, StringBuilder, String> listarRotas = Collector.of(
                StringBuilder::new,
                (acumulador, registro) -> {
                    if (!acumulador.isEmpty()) {
                        acumulador.append(" | ");
                    }
                    acumulador.append(registro.rota());
                },
                StringBuilder::append,
                StringBuilder::toString);

        IO.println("  " + registros.stream().collect(listarRotas));

        // OCP também no "meio" do pipeline (Gatherers), antes do JEP 485 não havia como estender as operações
        // intermediárias. Gatherers abriram esse ponto sem modificar nada do que já existia
        IO.println("\n== OCP: Gatherers (Bloco 4) ==");
        final List<List<RegistroAcesso>> lotes = registros.stream()
                .gather(Gatherers.windowFixed(2))
                .toList();

        IO.println("  " + registros.size() + " registros -> " + lotes.size() + " lotes de 2");

        // DIP: a Stream API depende de abstrações, collect() não conhece HashMap nem ArrayList: depende de Collector.
        // Quem decide a estrutura concreta é você, de fora
        IO.println("\n== DIP: collect depende da abstracao Collector ==");
        IO.println("  por status: " + registros.stream()
                .collect(Collectors.groupingBy(RegistroAcesso::status, Collectors.counting())));

        // LSP: contratos honestos e imutabilidade, List.of() é imutável POR CONTRATO DOCUMENTADO. A lição do vídeo 4:
        // se o subtipo restringe, o contrato precisa dizer isso.
        IO.println("\n== LSP: contrato explicito de imutabilidade ==");
        try {
            List.of("a").add("b");
        } catch (final UnsupportedOperationException excecao) {
            IO.println("  List.of e imutavel por contrato - documentado, nao surpresa");
            IO.println("  (diferente do nosso cache que descartava em silencio)");
        }

        /*
         * SRP: java.time separa conceitos:
         * LocalDate (data), LocalTime (hora), Duration (intervalo), ZoneId (fuso): um proposito cada.
         * A antiga java.util.Date acumulava tudo -- e por isso foi substituida. O proprio JDK aplicou o SRP.
         */

        /*
         * Fechamento do curso:
         * Bloco 1: as ferramentas, generics, annotations, reflection, records, sealed, pattern matching
         * Bloco 2: conversar com o mundo, NIO.2, HttpClient
         * Bloco 3: lidar com escala, virtual threads, concorrencia estruturada, scoped values
         * Bloco 4: o ferramental do dia a dia collections, collectors, gatherers, date/time
         * Bloco 5: o critério para organizar tudo isso
         *
         * O processador de logs da aula ao vivo era funcional e usava o que ha de mais moderno no JDK. Faltava design
         * e foi so isso que mudou entre o video 1 e o video 7
         */
    }
}