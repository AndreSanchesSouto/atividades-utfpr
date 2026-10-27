package br.edu.utfpr.jakartaee.ejb;


import jakarta.ejb.Stateless;
import java.util.concurrent.ThreadLocalRandom;

@Stateless
public class JogoEJB {

    public int[] gerarPergunta() {
        int numero1 = ThreadLocalRandom.current().nextInt(1, 101);
        int numero2 = ThreadLocalRandom.current().nextInt(1, 101);

        return new int[]{numero1, numero2};
    }

    public boolean verificarResposta(
            int numero1,
            int numero2,
            int resposta) {

        return numero1 + numero2 == resposta;
    }
}
