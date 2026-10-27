package br.edu.utfpr.jakartaee.ejb;


import br.edu.utfpr.jakartaee.model.Jogador;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class RankingEJB {

    private final Map<String, Jogador> jogadores = new HashMap<>();

    @Lock(LockType.WRITE)
    public void registrarJogador(String nome) {
        final String nomeTrim = nome.trim();
        jogadores.computeIfAbsent(
                nomeTrim.toLowerCase(),
                chave -> new Jogador(nomeTrim)
        );
    }

    @Lock(LockType.WRITE)
    public void adicionarPonto(String nome) {
        Jogador jogador = jogadores.get(nome.trim().toLowerCase());

        if (jogador != null) {
            jogador.adicionarPonto();
        }
    }

    @Lock(LockType.READ)
    public List<Jogador> listarRanking() {
        List<Jogador> ranking = new ArrayList<>(jogadores.values());

        ranking.sort(
                Comparator.comparingInt(Jogador::getPontuacao)
                        .reversed()
                        .thenComparing(Jogador::getNome,
                                String.CASE_INSENSITIVE_ORDER)
        );

        return ranking;
    }
}

