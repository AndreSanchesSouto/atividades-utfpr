package br.edu.utfpr;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class CotadorDeMoedas {

    public Path verificarEntrada() {
        // TODO
        return Path.of("entrada", "moedas.txt");
    }

    public Path verificarSaida() {
        // TODO
        try {
            Path diretorio = Path.of("saida");
            Files.createDirectories(diretorio);
        return diretorio.resolve("cotacoes.csv");
        } catch (IOException ex) {
            System.err.println("Falaha na criação do diretório: " + ex.getMessage());
            System.exit(-1);
            return null;
        }
    }

    public List<String> lerArquivoDeMoedas(Path entrada) {
        // TODO
        if(!Files.exists(entrada)) {
            return List.of();
        }
        try {
            try (Stream<String> linhas = Files.lines(entrada)) {
                return linhas
                        .map(linha -> linha.trim().toUpperCase())
                        .filter(linha -> !linha.isBlank())
                        .filter(linha -> linha.length() == 3)
                        .toList();
            }
        } catch (IOException ex) {
            System.err.println("Falha na leitura do arquivo: " + ex.getMessage());
            System.exit(-1);
            return null;
        }
    }

    public void cotarERegistrar(Path saida, List<String> moedas, ClienteCambio cliente) {

        // TODO
        if(moedas.isEmpty()) {
            return;
        }
        final List<CompletableFuture<Optional<Cotacao>>> requisicoes = moedas
                .stream()
                .map(cliente::consultar)
                .toList();
        CompletableFuture.allOf(
                requisicoes.toArray(new CompletableFuture[0])
        ).join();
        IO.println("Requisicoes");
        IO.println(requisicoes);
        final List<Cotacao> cotacoes = requisicoes
                .stream()
                .map(CompletableFuture::join)
                .flatMap(Optional::stream)
                .toList();

        gravarEmCSV(saida, cotacoes);
    }

    private void gravarEmCSV(Path saida, List<Cotacao> cotacoes) {

        // TODO
        try(BufferedWriter writer = Files.newBufferedWriter(saida)) {
            writer.write("moeda,valor,coletadoEm");
            writer.newLine();
            IO.println(cotacoes);
            for(Cotacao cotacao:cotacoes) {
                writer.write(cotacao.paraCsv());
                writer.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Erro ao gravar CSV: " + ex.getMessage());
            System.exit(-1);
        }

    }
}
