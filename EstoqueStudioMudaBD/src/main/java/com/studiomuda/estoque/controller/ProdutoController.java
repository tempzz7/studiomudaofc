package com.studiomuda.estoque.controller;

import com.studiomuda.estoque.dao.ProdutoDAO;
import com.studiomuda.estoque.model.Produto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @GetMapping
    public String listarProdutos(Model model) {
        try {
            model.addAttribute("produtos", produtoDAO.listar());
            return "produtos/lista";
        } catch (SQLException e) {
            model.addAttribute("mensagemErro", "Erro ao listar produtos: " + e.getMessage());
            return "erro";
        }
    }

    @GetMapping("/novo")
    public String formNovoProduto(Model model) {
        model.addAttribute("produto", new Produto());
        return "produtos/form";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@ModelAttribute Produto produto) {
        try {
            if (produto.getId() == 0) {
                produtoDAO.inserir(produto);
            } else {
                produtoDAO.atualizar(produto);
            }
            return "redirect:/produtos";
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable("id") int id, Model model) {
        try {
            Produto produto = produtoDAO.buscarPorId(id);
            if (produto != null) {
                model.addAttribute("produto", produto);
                return "produtos/form";
            } else {
                return "redirect:/produtos";
            }
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable("id") int id) {
        try {
            produtoDAO.deletar(id);
            return "redirect:/produtos";
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
}
