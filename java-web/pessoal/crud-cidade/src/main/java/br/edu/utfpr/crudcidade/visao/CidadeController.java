package br.edu.utfpr.crudcidade.visao;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;


@Controller
public class CidadeController {
    @GetMapping("/")
    public String listar(Model memoria) {
        final Set<Cidade> cidades = Set.of(
                new Cidade("Cianorte", "PR"),
                new Cidade("Maringá", "PR"),
                new Cidade("Tapejara", "PR")

        );

        memoria.addAttribute("listaCidades", cidades);

        return "/crud";
    }
}
