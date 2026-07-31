package br.edu.utfpr;

import br.edu.utfpr.dominio.ConstantesDeLog;
import br.edu.utfpr.dominio.RegistroAcesso;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VÍDEO 1 — A dor: coesão, acoplamento e por que aquele código não se sustenta
 * Requer Java 25 LTS
 */
public class Video01CoesaoEAcoplamento {

    static final class ProcessadorMonolitico {

        /*
         * O MONOLITO, como estava na Aula Virtual 2 (reduzido para caber na tela).
         *
         * Razões para esta classe mudar:
         * 1. o formato da linha de log mudou;
         * 2. a regra do que é "erro" mudou;
         * 3. o cálculo das métricas mudou;
         * 4. o formato do relatório mudou;
         * 5. o destino da saída mudou (tela -> arquivo -> e-mail);
         * 6. a política de cache mudou.
         *
         * Seis razões = BAIXA COESÃO. Ela não tem UM assunto; tem seis.
         */

        // O CACHE QUE VAZOU NA AULA AO VIVO
        // Está aqui, no meio de tudo, sem dono e sem limite. Guarde esta linha: ela volta no vídeo 2.
        private static final Map<String, RegistroAcesso> CACHE = new HashMap<>();

        void executar(List<String> linhasDeLog) {
            long total = 0;
            long erros = 0;

            for (final String linha : linhasDeLog) {
                // ACOPLAMENTO 1: o parse está soldado ao laço de processamento
                final String[] partes = linha.trim().split(" ");

                if (partes.length != 7) { // magic number
                    continue;
                }

                final RegistroAcesso registro = new RegistroAcesso(
                        partes[0],
                        LocalDateTime.parse(partes[1]),
                        partes[2],
                        partes[3],
                        partes[4],
                        Integer.parseInt(partes[5]),
                        Long.parseLong(partes[6]));

                // ACOPLAMENTO 2: a política de cache está soldada ao processamento
                CACHE.put(registro.identificador(), registro);

                total++;
                if (registro.status() >= 500) { // magic number, regra embutida
                    erros++;
                }
            }

            // ACOPLAMENTO 3: cálculo, formatação e saída, tudo junto.
            IO.println("Total: " + total);
            IO.println("Erros: " + erros);
            IO.println("Cache: " + CACHE.size() + " registros retidos");
        }
    }

    void main() {

        /*
         * As perguntas que expoem o problema:
         *
         * 1. Como testar só o calculo da taxa de erro, sem construir linhas de log de mentira?
         *    Nao da. O calculo esta preso ao parse
         * 2. Como exportar o mesmo relatorio em CSV, sem tocar na logica de calculo?
         *    Nao da. A formatacao esta dentro do metodo
         * 3. Como trocar o arquivo por uma fila de mensagens?
         *    Reescrever a classe inteira
         * 4. E a pergunta que ficou da aula ao vivo: por que ninguem percebeu o cache vazando?
         *    Porque o cache nao tem DONO, ele mora no meio de outras cinco responsabilidades. Ninguem olha para ele
         *    porque nao existe um lugar 'de guardar coisas'
         *
         * O vocabulario:
         *
         * COESAO: o quanto os elementos de um modulo pertencem juntos. Teste pratico: conte as RAZOES PARA MUDAR.
         *         Aqui sao seis.
         *
         * ACOPLAMENTO: o quanto um modulo depende dos DETALHES de outro. Baixo acoplamento = depender de abstracao,
         *              nao de detalhe.
         *
         * SOLID: sao heuristicas para aumentar coesao e reduzir o acoplamento
         */

        IO.println("=== O monolito rodando ===");
        new ProcessadorMonolitico().executar(ConstantesDeLog.amostraDeLinhasCruas());
    }
}