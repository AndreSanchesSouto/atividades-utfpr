package br.edu.utfpr.crudcidade.visao;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Controller
public class CidadeController {

    private Set<Cidade> cidades;

    public CidadeController() {
        this.cidades = new HashSet<Cidade>();
    }

    @GetMapping("/")
    public String listar(Model memoria) {
        memoria.addAttribute("listaCidades", cidades);
        return "/crud";
    }

    @PostMapping("/criar")
    public String criar(Cidade cidade) {
        cidades.add(cidade);
        return "redirect:/";
    }

    @GetMapping("/excluir")
    public String excluir(
            @RequestParam String nome,
            @RequestParam String estado
    ) {
        cidades.removeIf(cidade ->
                cidade.getNome().equals(nome)
                && cidade.getEstado().equals(estado)
        );

        return "redirect:/";
    }

    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
            @RequestParam String nome,
            @RequestParam String estado,
            Model memoria
    ) {
        memoria.addAttribute("listaCidades", this.cidades);

        this.cidades.stream()
                .filter( cidade ->
                        cidade.getEstado().equals(estado)
                        && cidade.getNome().equals(nome)
                )
                .findAny()
                .ifPresent(cidade ->
                    memoria.addAttribute("cidadeAtual", cidade)
                );

        return "/crud";
    }

    @PostMapping("/alterar")
    public String alterar(
            @RequestParam String nomeAtual,
            @RequestParam String estadoAtual,
            Cidade cidade
    ) {
        cidades.removeIf(cidadeAtual ->
                cidadeAtual.getNome().equals(nomeAtual)
                && cidadeAtual.getEstado().equals(estadoAtual)
        );

        this.criar(cidade);

        return "redirect:/";
    }
}
