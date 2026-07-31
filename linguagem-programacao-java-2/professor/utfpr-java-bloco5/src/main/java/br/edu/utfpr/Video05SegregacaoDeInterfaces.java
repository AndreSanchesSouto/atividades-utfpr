package br.edu.utfpr;

import br.edu.utfpr.dominio.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * VÍDEO 5 — I: Segregação de Interfaces (ISP)
 * Requer Java 25 LTS
 */
public class Video05SegregacaoDeInterfaces {

    // VIOLAÇÃO: interface gorda

    /**
     * Tentativa ingênua de organizar o monolito: uma interface com tudo. Quem só quer LER é obrigado a implementar
     * escrita, validação, cache, exportação e notificação.
     */
    interface ProcessadorCompleto {
        List<String> lerLinhas();

        ResultadoParse parsear(String linha);

        boolean validar(RegistroAcesso registro);

        void guardarNoCache(RegistroAcesso registro);

        Optional<RegistroAcesso> buscarNoCache(String identificador);

        Metricas agregar(List<RegistroAcesso> registros);

        String exportarCsv(Metricas metricas);

        void notificarPlantao(String mensagem);
    }

    /**
     * O dublê (mock) de teste vira um monstro: 8 métodos para exercitar 1. Pior: os métodos "não usados" costumam ficar
     * vazios ou lançando exceção — o que ainda viola o LSP (vídeo 4).
     */
    static final class DubleDolorido implements ProcessadorCompleto {

        @Override
        public List<String> lerLinhas() {
            return ConstantesDeLog.amostraDeLinhasCruas(); // o único que interessa
        }

        // Tudo abaixo existe só para compilar. É ruído puro.
        @Override
        public ResultadoParse parsear(String linha) {
            return new LinhaInvalida(linha, "nao implementado no duble");
        }

        @Override
        public boolean validar(RegistroAcesso registro) {
            return true;
        }

        @Override
        public void guardarNoCache(RegistroAcesso registro) {
        }

        @Override
        public Optional<RegistroAcesso> buscarNoCache(String identificador) {
            return Optional.empty();
        }

        @Override
        public Metricas agregar(List<RegistroAcesso> registros) {
            throw new UnsupportedOperationException("nao implementado"); // fere o LSP
        }

        @Override
        public String exportarCsv(Metricas metricas) {
            return "";
        }

        @Override
        public void notificarPlantao(String mensagem) {
        }
    }

    // SOLUÇÃO: interfaces magras
    // Cada cliente depende apenas do que realmente usa. São as mesmas fronteiras que encontramos no vídeo 2 — não por 
    // coincidência: ISP e SRP olham o mesmo problema, um pela interface e outro pela implementação
    interface LeitorDeLinhas {
        List<String> ler();
    }

    interface ValidadorDeRegistro {
        boolean validar(RegistroAcesso registro);
    }

    interface NotificadorDePlantao {
        void notificar(String mensagem);
    }

    void main() {
        IO.println("== Interface gorda ==");
        final ProcessadorCompleto dublePesado = new DubleDolorido();

        IO.println("  para testar apenas 'lerLinhas', implementamos 8 metodos");
        IO.println("  linhas lidas: " + dublePesado.lerLinhas().size());
        IO.println("  e 'agregar' lanca excecao -> tambem fere o LSP");

        IO.println("\n== Interfaces magras ==");

        // Cada dublê agora é uma lambda de UMA linha.
        final LeitorDeLinhas leitor = ConstantesDeLog::amostraDeLinhasCruas;
        final ValidadorDeRegistro validador = registro -> registro.status() > 0;
        final NotificadorDePlantao notificador = mensagem -> IO.println("    [plantao] " + mensagem);

        IO.println("  leitor (lambda): " + leitor.ler().size() + " linhas");
        IO.println("  validador (lambda): " + validador.validar(ConstantesDeLog.amostraDeRegistros().getFirst()));
        notificador.notificar("taxa de erro acima do limite");

        // O JDK como referência de ISP
        // Predicate, Function, Supplier, Consumer, Comparator: UM metodo cada. É por isso que podem ser lambdas, e é
        // por isso que compõem tão bem (Bloco 4). Se Comparator tivesse 8 métodos, não existiria lambda.
        IO.println("\n== ISP no proprio JDK ==");

        final Predicate<RegistroAcesso> ehErro = RegistroAcesso::ehErroDeServidor;
        final Predicate<RegistroAcesso> ehLenta = registro -> registro.ehLenta(ConstantesDeLog.LIMITE_PADRAO_DE_LENTIDAO_MS);
        final Function<RegistroAcesso, String> extrairRota = RegistroAcesso::rota;

        // Composição só é possível porque as interfaces são mínimas:
        final List<String> rotasProblematicas = ConstantesDeLog.amostraDeRegistros().stream()
                .filter(ehErro.or(ehLenta))
                .map(extrairRota)
                .distinct()
                .toList();

        IO.println("  rotas com erro OU lentas: " + rotasProblematicas);
        IO.println("  Predicate tem UM metodo -> vira lambda e compoe com or()");
    }
}