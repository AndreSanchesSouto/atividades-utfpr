package br.edu.utfpr;

import br.edu.utfpr.model.FormaPagamento;
import br.edu.utfpr.model.RankingVendedor;
import br.edu.utfpr.model.ResumoVendas;
import br.edu.utfpr.model.Venda;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;

public class LeitorDeVendas {

    private final List<Venda> vendas;

    public LeitorDeVendas(String salesFile) {

        final var stream = getClass().getClassLoader().getResourceAsStream(salesFile);

        if (stream == null) {
            throw new IllegalStateException("Arquivo não encontrado");
        }

        final var builder = new CsvToBeanBuilder<VendaCsv>(new InputStreamReader(stream, StandardCharsets.UTF_8));

        vendas = builder
                .withType(VendaCsv.class)
                .withSeparator(';')
                .build()
                .parse()
                .stream()
                .map(VendaCsv::toVenda)
                .toList();
    }

    public List<Venda> vendasConcluidasAcimaDoValorNaRegiao(BigDecimal valorMinimo, String regiao) {
        return vendas
                .stream()
                .filter(Venda::isConcluida)
                .filter(venda -> venda.valor().compareTo(valorMinimo) > 0)
                .filter(venda -> venda.regiao().equalsIgnoreCase(regiao))
                .toList();
    }

    public Optional<Venda> vendaDeMaiorValorNaRegiao(String regiao) {
        return vendas.stream()
                .filter(venda -> venda.regiao().equalsIgnoreCase(regiao))
                .max(Comparator.comparing(Venda::valor));
    }

    public List<Venda> topNVendasPorValor(int n) {
        final PriorityQueue<Venda> queue = new PriorityQueue<>(Comparator.comparing(Venda::valor));
        for(Venda venda: vendas) {
            if (queue.size() < n) {
                queue.add(venda);
            } else if(
                    queue.peek() != null &&
                            venda.valor().compareTo(queue.peek().valor()) > 0
            ) {
                queue.poll();
                queue.add(venda);
            }
        }
        return queue.stream()
                .sorted(Comparator.comparing(Venda::valor).reversed())
                .toList();
    }

    public List<RankingVendedor> top3VendedoresPorFaturamento() {
        final Map<String, List<BigDecimal>> vendarPorVendedor = vendas.stream()
                .filter(Venda::isConcluida)
                .collect(Collectors.groupingBy(
                        Venda::vendedor,
                        Collectors.mapping(Venda::valor, Collectors.toList())
                ));

        return vendarPorVendedor.entrySet()
                .stream()
                .map(v -> new RankingVendedor(
                                v.getKey(),
                                v.getValue()
                                        .stream()
                                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        )
                )
                .sorted(Comparator.comparing(RankingVendedor::faturamento).reversed())
                .limit(3)
                .toList();
    }

    public Map<String, BigDecimal> valorTotalDeVendasConcluidasPorRegiao() {
        return vendas.stream()
                .filter(Venda::isConcluida)
                .collect(Collectors.groupingBy(
                        Venda::regiao,
                        Collectors.mapping(
                                Venda::valor,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }

    public ResumoVendas resumoDeVendasConcluidas() {
        return vendas.stream()
                .filter(Venda::isConcluida)
                .collect(Collector.of(
                        AcumuladorDeResumo::new,
                        (acumuladorDeResumo, venda) -> acumuladorDeResumo.somar(venda.valor()),
                        AcumuladorDeResumo::combinar,
                        AcumuladorDeResumo::finalizar
                        )
                );
    }

    public List<BigDecimal> faturamentoAcumuladoPorVendedorEMes(String vendedor, YearMonth mes) {
        return vendas.stream()
                .filter(Venda::isConcluida)
                .filter(venda -> YearMonth.from(venda.dataVenda()).equals(mes))
                .filter(venda -> venda.vendedor().equalsIgnoreCase(vendedor))
                .map(Venda::valor)
                .gather(Gatherers.scan(() -> BigDecimal.ZERO, BigDecimal::add))
                .toList();
    }

    public FormaPagamento formaPagamentoComMaisCancelamentos() {
        return vendas.stream()
                .filter(Venda::isCancelada)
                .collect(Collectors.groupingBy(
                        Venda::formaPagamento,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }

    public long diasEntrePrimeiraEUltimaVendaCancelada() {
        return vendas.stream()
                .filter(Venda::isCancelada)
                .map(Venda::dataVenda)
                .collect(Collectors.teeing(
                        Collectors.minBy(LocalDate::compareTo),
                        Collectors.maxBy(LocalDate::compareTo),
                        (dataInicial, dataFinal) -> ChronoUnit.DAYS.between(dataInicial.orElseThrow(), dataFinal.orElseThrow())
                ));
    }

    public Map<Integer, Map<FormaPagamento, Long>> quantidadeDeVendasConcluidasPorFormaDePagamentoAgrupadasPorAno() {
        return vendas.stream()
                .filter(Venda::isConcluida)
                .collect(Collectors.groupingBy(
                        venda -> venda.dataVenda().getYear(),
                        Collectors.groupingBy(
                                Venda::formaPagamento,
                                Collectors.counting()
                        )
                ));
    }

    private static final class AcumuladorDeResumo {

        private BigDecimal total = BigDecimal.ZERO;
        private long quantidade = 0;

        private void somar(BigDecimal valor) {
            total = total.add(valor);
            quantidade++;
        }

        private AcumuladorDeResumo combinar(AcumuladorDeResumo outro) {
            total = total.add(outro.total);
            quantidade += outro.quantidade;
            return this;
        }

        private ResumoVendas finalizar() {
            final var media = quantidade == 0
                    ? BigDecimal.ZERO
                    : total.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP);
            return new ResumoVendas(total, media, quantidade);
        }
    }
}