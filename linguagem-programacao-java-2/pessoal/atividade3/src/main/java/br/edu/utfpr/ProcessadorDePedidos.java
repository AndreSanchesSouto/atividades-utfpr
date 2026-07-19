package br.edu.utfpr;

import br.edu.utfpr.dominio.*;
import br.edu.utfpr.utilidades.LeitorDePedidos;
import br.edu.utfpr.utilidades.ServicosExternos;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope;

public final class ProcessadorDePedidos {

    public ResultadoPedido processarPedido(Pedido pedido) {
        try (StructuredTaskScope escopo = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())) {
            final Subtask<Estoque> estoquePedido = escopo.fork(() ->
                    ServicosExternos.consultarEstoque(
                            pedido.produto(),
                            pedido.identificador()
                    )
            );
            final Subtask<Preco> precoPedido = escopo.fork(() ->
                    ServicosExternos.consultarPreco(
                            pedido.produto(),
                            pedido.identificador()
                    )
            );
            escopo.join();

            Estoque estoque = estoquePedido.get();
            if (estoque.quantidadeDisponivel() < pedido.quantidade()) {
                return new PedidoRejeitado(
                        pedido.identificador(),
                        "estoque"
                );
            }

            CotacaoFrete frete = cotarFrete(pedido.produto());
            Preco preco = precoPedido.get();
            BigDecimal valorTotal = preco.valorUnitario()
                    .multiply(BigDecimal.valueOf(pedido.quantidade()))
                    .add(frete.valor());

            return new PedidoAprovado(
                    pedido.identificador(),
                    valorTotal,
                    frete
            );

        } catch (Exception e) {
            return new PedidoRejeitado(
                    pedido.identificador(),
                    e.getMessage()
            );
        }
    }

    private CotacaoFrete cotarFrete(String produto) throws InterruptedException {
        try (
            StructuredTaskScope<CotacaoFrete, CotacaoFrete> escopo = StructuredTaskScope.open(
                    StructuredTaskScope.Joiner.anySuccessfulResultOrThrow()
            )
        ) {
            escopo.fork(() -> ServicosExternos.cotarFreteTransportadoraUm(produto));
            escopo.fork(() ->ServicosExternos.cotarFreteTransportadoraDois(produto));

            return escopo.join();
        }
    }

    public Relatorio processarArquivo(Path arquivoEntrada) throws Exception{
        try (final var escopo = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())){
            List<Pedido> pedidos = LeitorDePedidos.ler(arquivoEntrada);
            List<Subtask<ResultadoPedido>> resultadoPedidos = new ArrayList<>();

            for(Pedido pedido : pedidos) {
                resultadoPedidos.add(escopo.fork(() -> processarPedido(pedido)));
            }

            escopo.join();

            List<PedidoAprovado> aprovados = new ArrayList<>();
            List<PedidoRejeitado> rejeitados = new ArrayList<>();

            for (Subtask<ResultadoPedido> subtask : resultadoPedidos) {
                switch (subtask.get()) {
                    case PedidoAprovado aprovado -> aprovados.add(aprovado);
                    case PedidoRejeitado rejeitado -> rejeitados.add(rejeitado);
                }
            }

            return new Relatorio(aprovados, rejeitados);
        }

    }
}
