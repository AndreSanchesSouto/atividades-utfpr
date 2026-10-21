package br.edu.utfpr.crudcidade.cidade;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class CidadeController {

    private final CidadeRepository repository;

    public CidadeController(final CidadeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String listar(Model memoria) {
        memoria.addAttribute(
                "listaCidades",
                this.converteCidade(this.repository.findAll()));
        return "/crud";
    }

    private List<Cidade> converteCidade(List<CidadeEntidade> cidades) {
        return cidades.stream()
                .map(cidade -> new Cidade(cidade.getNome(), cidade.getEstado()))
                .collect(Collectors.toList());
    }

    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao, Model memoria) {
        if (validacao.hasErrors()) {
            validacao.getFieldErrors()
                    .forEach(error ->
                            memoria.addAttribute(error.getField(), error.getDefaultMessage())
                    );
            memoria.addAttribute("nomeInformado", cidade.getNome());
            memoria.addAttribute("estadoInformado", cidade.getEstado());
            memoria.addAttribute("listaCidades", this.converteCidade(repository.findAll()));

            return "/crud";

        } else {
            this.repository.save(cidade.clonar());
        }
        return "redirect:/";
    }

    @GetMapping("/excluir")
    public String excluir(
            @RequestParam String nome,
            @RequestParam String estado
    ) {
        final Optional<CidadeEntidade> cidadeEncontrada = this.repository.findByNomeAndEstado(nome, estado);

        cidadeEncontrada.ifPresent(this.repository::delete);

        return "redirect:/";
    }

    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
            @RequestParam String nome,
            @RequestParam String estado,
            Model memoria
    ) {
        final Optional<CidadeEntidade> cidadeAtual =
                this.repository.findByNomeAndEstado(nome, estado);

        cidadeAtual.ifPresent(cidade ->
                memoria.addAttribute("cidadeAtual", cidade)
        );

        memoria.addAttribute(
                "listaCidades",
                this.converteCidade(this.repository.findAll())
        );

        return "/crud";
    }

    @PostMapping("/alterar")
    public String alterar(
            @RequestParam String nomeAtual,
            @RequestParam String estadoAtual,
            @Valid Cidade cidade,
            BindingResult validacao,
            Model memoria
    ) {
        if (validacao.hasErrors()) {
            memoria.addAttribute("cidadeAtual", cidade);
            memoria.addAttribute("nomeAtual", nomeAtual);
            memoria.addAttribute("estadoAtual", estadoAtual);
            memoria.addAttribute("listaCidades",
                    this.converteCidade(this.repository.findAll()));

            validacao.getFieldErrors().forEach(error ->
                    memoria.addAttribute(error.getField(), error.getDefaultMessage())
            );

            return "/crud";
        }

        final Optional<CidadeEntidade> cidadeAtual =
                this.repository.findByNomeAndEstado(nomeAtual, estadoAtual);

        if (cidadeAtual.isPresent()) {
            CidadeEntidade cidadeEncontrada = cidadeAtual.get();
            cidadeEncontrada.setNome(cidade.getNome());
            cidadeEncontrada.setEstado(cidade.getEstado());

            repository.saveAndFlush(cidadeEncontrada);
        }

        return "redirect:/";
    }
}
