package br.edu.utfpr.dominio;

import java.util.Map;

public record Metricas(
        long totalDeRequisicoes,
        long totalDeErros,
        double taxaDeErro,
        long requisicoesLentas,
        Map<Integer, Long> requisicoesPorHora) {
}