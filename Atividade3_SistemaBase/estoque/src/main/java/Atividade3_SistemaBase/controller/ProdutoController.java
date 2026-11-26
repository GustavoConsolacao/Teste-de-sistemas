package Atividade3_SistemaBase.controller;import Atividade3_SistemaBase.model.Produto;
import Atividade3_SistemaBase.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProdutoController {
    @Autowired
    private ProdutoRepository repository;
    @GetMapping("/")
    public String listarProdutos(@RequestParam(required = false) String busca, Model model) {
        List<Produto> lista;

        if (busca != null && !busca.trim().isEmpty()) {
            lista = repository.findByNomeContainingIgnoreCase(busca);
        } else {
            lista = repository.findAll();
        }

        double totalGeral = lista.stream()
                .mapToDouble(Produto::calcularValorTotalEstoque)
                .sum();

        model.addAttribute("produtos", lista);
        model.addAttribute("novoProduto", new Produto());
        model.addAttribute("totalGeral", totalGeral);
        model.addAttribute("busca", busca);
        return "index";
    }
    @PostMapping("/salvar")
    public String salvarProduto(@ModelAttribute Produto produto) {
        repository.save(produto);
        return "redirect:/";
    }

    @GetMapping("/adicionar/{id}")
    public String adicionarUnidade(@PathVariable Long id) {
        Produto p = repository.findById(id).orElse(null);
        if (p != null) {
            p.adicionarEstoque(1);
            repository.save(p);
        }
        return "redirect:/";
    }
    @GetMapping("/remover/{id}")
    public String removerUnidade(@PathVariable Long id) {
        Produto p = repository.findById(id).orElse(null);
        if (p != null) {
            p.removerEstoque(1);
            repository.save(p);
        }
        return "redirect:/";
    }
    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/";
    }
}