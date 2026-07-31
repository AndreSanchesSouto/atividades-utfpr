package br.edu.utfpr;

import br.edu.utfpr.dominio.ConstantesDeLog;
import br.edu.utfpr.dominio.RegistroAcesso;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * VÍDEO 4 — L: Substituição de Liskov (LSP) - criado por Barbara Liskov em 1987
 * Requer Java 25 LTS
 */
public class Video04SubstituicaoDeLiskov {

    // VIOLAÇÃO

    /**
     * Cache básico: quem chama 'guardar' espera que o registro fique guardado.
     */
    static class CacheDeRegistros {

        protected final Map<String, RegistroAcesso> conteudo = new LinkedHashMap<>();

        void guardar(RegistroAcesso registro) {
            conteudo.put(registro.identificador(), registro);
        }

        int quantidade() {
            return conteudo.size();
        }
    }

    /**
     * VIOLA o LSP de forma SILENCIOSA — a pior forma, prometeu ser um CacheDeRegistros, mas 'guardar' às vezes não
     * guarda, sem erro e sem retorno. O cliente guarda 5 e encontra 2.
     */
    static class CacheLimitadoSilencioso extends CacheDeRegistros {

        private final int capacidadeMaxima;

        CacheLimitadoSilencioso(int capacidadeMaxima) {
            this.capacidadeMaxima = capacidadeMaxima;
        }

        @Override
        void guardar(RegistroAcesso registro) {
            if (conteudo.size() < capacidadeMaxima) {
                conteudo.put(registro.identificador(), registro);
            }
            // descarta em silêncio -> surpresa para quem chamou, ou, lançando exception
            // throw new IllegalStateException("Limite de registros atingido");
        }
    }

    /**
     * Também viola, de forma ESCANDALOSA: prometeu 'guardar' e explode se um metodo herdado não pode ser cumprido,
     * a herança está errada.
     */
    static class CacheSomenteLeitura extends CacheDeRegistros {
        @Override
        void guardar(RegistroAcesso registro) {
            throw new UnsupportedOperationException("cache somente leitura");
        }
    }

    /**
     * Código cliente que confia no contrato do supertipo.
     */
    static void ingerir(CacheDeRegistros cache, List<RegistroAcesso> registros) {
        registros.forEach(cache::guardar);
        IO.println("  guardou " + registros.size() + ", cache tem " + cache.quantidade());
    }

    // SOLUÇÃO
    // 1) Separar as capacidades em interfaces distintas (conversa com o ISP)
    interface FonteDeRegistros {
        Optional<RegistroAcesso> buscar(String identificador);

        int quantidade();
    }

    interface DestinoDeRegistros {
        /**
         * @return true se guardou; false se recusou. A recusa é VISÍVEL.
         */
        boolean tentarGuardar(RegistroAcesso registro);
    }

    // 2) O limite passa a ser parte EXPLÍCITA do contrato.
    static final class CacheComLimiteHonesto implements FonteDeRegistros, DestinoDeRegistros {

        private final Map<String, RegistroAcesso> conteudo = new LinkedHashMap<>();
        private final int capacidadeMaxima;

        CacheComLimiteHonesto(int capacidadeMaxima) {
            this.capacidadeMaxima = capacidadeMaxima;
        }

        @Override
        public boolean tentarGuardar(RegistroAcesso registro) {
            if (conteudo.size() >= capacidadeMaxima && !conteudo.containsKey(registro.identificador())) {
                return false; // recusa explícita, não silenciosa
            }
            conteudo.put(registro.identificador(), registro);
            return true;
        }

        @Override
        public Optional<RegistroAcesso> buscar(String identificador) {
            return Optional.ofNullable(conteudo.get(identificador));
        }

        @Override
        public int quantidade() {
            return conteudo.size();
        }
    }

    // 3) Quem só lê implementa SÓ a leitura: não existe metodo para quebrar.
    record RegistrosImutaveis(Map<String, RegistroAcesso> conteudo) implements FonteDeRegistros {

        RegistrosImutaveis {
            conteudo = Map.copyOf(conteudo); // imutabilidade real
        }

        @Override
        public Optional<RegistroAcesso> buscar(String identificador) {
            return Optional.ofNullable(conteudo.get(identificador));
        }

        @Override
        public int quantidade() {
            return conteudo.size();
        }
    }

    void main() {
        final List<RegistroAcesso> registros = ConstantesDeLog.amostraDeRegistros();

        IO.println("== Violacao do LSP ==");
        IO.println("cache basico:");
        ingerir(new CacheDeRegistros(), registros);

        IO.println("cache limitado silencioso (a pior violacao):");
        ingerir(new CacheLimitadoSilencioso(2), registros);
        IO.println("  ^ o cliente nao tem como saber que perdeu 3 registros");

        IO.println("cache somente leitura:");
        try {
            ingerir(new CacheSomenteLeitura(), registros);
        } catch (final UnsupportedOperationException excecao) {
            IO.println("  explodiu: " + excecao.getMessage());
        }

        IO.println("\n== Solucao: contrato honesto ==");
        final CacheComLimiteHonesto cacheHonesto = new CacheComLimiteHonesto(2);

        int guardados = 0;
        int recusados = 0;

        for (final RegistroAcesso registro : registros) {
            if (cacheHonesto.tentarGuardar(registro)) {
                guardados++;
            } else {
                recusados++;
            }
        }

        IO.println("  guardados: " + guardados + " | recusados: " + recusados);
        IO.println("  o cliente SABE exatamente o que aconteceu e pode reagir");

        final FonteDeRegistros somenteLeitura = new RegistrosImutaveis(Map.of(registros.get(0).identificador(), registros.get(0)));
        IO.println("\n  fonte imutavel com " + somenteLeitura.quantidade() + " registro(s): sem metodo de escrita para quebrar o contrato");

        IO.println("\n--- ligacao com a aula ao vivo ---");
        IO.println("No video 2 o cache ganhou um dono. Aqui ele ganhou um");
        IO.println("CONTRATO HONESTO: o limite deixou de ser um efeito colateral");
        IO.println("escondido e passou a ser parte visivel da assinatura.");
    }
}