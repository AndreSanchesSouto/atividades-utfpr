package br.edu.utfpr;

import br.edu.utfpr.dominio.ConstantesDeLog;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VÍDEO 6 — D: Inversão de Dependência (DIP)
 * Requer Java 25 LTS
 */
public class Video06InversaoDeDependencia {

    // a ABSTRAÇÃO (o contrato)
    interface LeitorDeLinhas {
        List<String> ler();
    }

    // implementações concretas (os detalhes)

    static final class LeitorDeArquivo implements LeitorDeLinhas {

        private final Path caminho;

        LeitorDeArquivo(Path caminho) {
            this.caminho = caminho;
        }

        @Override
        public List<String> ler() {
            try {
                return Files.readAllLines(caminho);
            } catch (IOException ex) {
                IO.println("    (arquivo nao encontrado: " + caminho + ")");
                return List.of();
            }
        }
    }

    static final class LeitorEmMemoria implements LeitorDeLinhas {

        private final List<String> linhas;

        LeitorEmMemoria(List<String> linhas) {
            this.linhas = List.copyOf(linhas);
        }

        @Override
        public List<String> ler() {
            return linhas;
        }
    }

    // VIOLAÇÃO

    /**
     * A classe de alto nível instancia a de baixo nível diretamente. Consequências: impossível trocar a fonte,
     * impossível testar sem disco. É literalmente o que o monolito da webconf 2 fazia
     */
    static final class ContadorViolandoDip {

        // @Autowired - nao usar! sempre prefira injecao por construtor!
        // acoplamento rígido
        private final LeitorDeArquivo leitor = new LeitorDeArquivo(Path.of("logs", "acesso.log"));

        long contarLinhas() {
            return leitor.ler().size();
        }
    }

    // SOLUÇÃO: injeção por construtor

    /**
     * Solução: injeção por construtor, mais trivial e comum
     */
    static final class ContadorBom {

        private final LeitorDeLinhas leitor;

        public ContadorBom(LeitorDeLinhas leitor) {
            this.leitor = leitor;
        }

        long contarLinhas() {
            return leitor.ler().size();
        }
    }

    /**
     * Depende da INTERFACE e recebe a implementação de fora, quem decide qual usar é o chamador, não o serviço
     */
    static final class ContadorDeLinhas {

        private final LeitorDeLinhas leitor;

        @Injetar
        ContadorDeLinhas(LeitorDeLinhas leitor) {
            this.leitor = leitor;
        }

        long contarLinhas() {
            return leitor.ler().size();
        }
    }

    // annotation própria (Bloco 1)
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.CONSTRUCTOR)
    @interface Injetar { // @Autowired - depreciada!
    }

    // MINI-CONTAINER (annotations + reflection do Bloco 1)

    /**
     * Registra qual implementação atende cada abstração e constrói objetos resolvendo as dependências do construtor
     * anotado com @Injetar, é uma miniatura do núcleo de um framework de DI
     */
    static final class ContainerSimples {

        private final Map<Class<?>, Object> implementacoesRegistradas = new HashMap<>();

        <T> void registrar(Class<T> abstracao, T implementacao) {
            implementacoesRegistradas.put(abstracao, implementacao);
        }

        <T> T criar(Class<T> tipo) {
            try {
                // 1) acha o construtor anotado com @Injetar (ou o primeiro)
                Constructor<?> construtorEscolhido = null;
                for (final Constructor<?> construtor : tipo.getDeclaredConstructors()) {
                    if (construtor.isAnnotationPresent(Injetar.class)) {
                        construtorEscolhido = construtor;
                        break;
                    }
                }
                if (construtorEscolhido == null) {
                    construtorEscolhido = tipo.getDeclaredConstructors()[0];
                }
                construtorEscolhido.setAccessible(true);

                // 2) resolve cada parâmetro pelo tipo registrado
                final Class<?>[] tiposDosParametros = construtorEscolhido.getParameterTypes();
                final Object[] argumentos = new Object[tiposDosParametros.length];

                for (int indice = 0; indice < tiposDosParametros.length; indice++) {

                    final Object dependencia = implementacoesRegistradas.get(tiposDosParametros[indice]);

                    if (dependencia == null) {
                        throw new IllegalStateException("nenhuma implementacao registrada para " + tiposDosParametros[indice].getSimpleName());
                    }
                    argumentos[indice] = dependencia;
                }

                // 3) instancia
                return tipo.cast(construtorEscolhido.newInstance(argumentos));
            } catch (final ReflectiveOperationException excecao) {
                throw new IllegalStateException("falha ao criar " + tipo.getName(), excecao);
            }
        }
    }

    void main() {

        IO.println("== Violacao do DIP ==");
        IO.println("  linhas: " + new ContadorViolandoDip().contarLinhas());
        IO.println("  ^ sempre do arquivo fixo; nao da para trocar nem testar isolado");

        IO.println("\n== Solucao: injecao manual por construtor ==");

        final ContadorDeLinhas comMemoria = new ContadorDeLinhas(new LeitorEmMemoria(ConstantesDeLog.amostraDeLinhasCruas()));
        final ContadorDeLinhas comArquivo = new ContadorDeLinhas(new LeitorDeArquivo(Path.of("logs", "acesso.log")));

        IO.println("  em memoria -> " + comMemoria.contarLinhas() + " linhas (teste rapido!)");
        IO.println("  em arquivo -> " + comArquivo.contarLinhas() + " linhas");
        IO.println("  MESMA classe, fontes diferentes. Foi so inverter a dependencia.");

        IO.println("\n== Mini-container (annotations + reflection, Bloco 1) ==");
        final ContainerSimples container = new ContainerSimples();
        container.registrar(LeitorDeLinhas.class, new LeitorEmMemoria(ConstantesDeLog.amostraDeLinhasCruas()));

        final ContadorDeLinhas injetado = container.criar(ContadorDeLinhas.class);
        IO.println("  criado pelo container -> " + injetado.contarLinhas() + " linhas");
    }
}