package com.studiomuda.estoque.controller;

import com.studiomuda.estoque.dao.MovimentacaoEstoqueDAO;
import com.studiomuda.estoque.dao.ProdutoDAO;
import com.studiomuda.estoque.model.MovimentacaoEstoque;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    private final MovimentacaoEstoqueDAO movimentacaoDAO = new MovimentacaoEstoqueDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @GetMapping
    public String listarMovimentacoes(Model model) {
        try {
            List<MovimentacaoEstoque> movimentacoes = movimentacaoDAO.listar();
            model.addAttribute("movimentacoes", movimentacoes);
            return "estoque/lista";
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }

    @GetMapping("/nova")
    public String formNovaMovimentacao(Model model) {
        try {
            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setData(Date.valueOf(LocalDate.now()));
            movimentacao.setTipo("entrada"); // Valor padru00e3o
            
            model.addAttribute("movimentacao", movimentacao);
            model.addAttribute("produtos", produtoDAO.listar());
            return "estoque/form";
        } catch (SQLException e) {
            model.addAttribute("mensagemErro", "Erro ao preparar formulu00e1rio: " + e.getMessage());
            return "erro";
        }
    }

    @PostMapping("/salvar")
    public String salvarMovimentacao(@ModelAttribute MovimentacaoEstoque movimentacao,
                                    @RequestParam(value = "dataStr", required = false) String dataStr) {
        try {
            // Converter string de data para Date
            if (dataStr != null && !dataStr.isEmpty()) {
                movimentacao.setData(Date.valueOf(dataStr));
            } else {
                // Usar a data atual se não for fornecida
                movimentacao.setData(Date.valueOf(LocalDate.now()));
            }
            
            movimentacaoDAO.registrar(movimentacao);
            return "redirect:/estoque";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirMovimentacao(@PathVariable("id") int id) {
        try {
            System.out.println("Tentando excluir movimentação ID: " + id);
            movimentacaoDAO.deletar(id);
            System.out.println("Movimentação excluída com sucesso!");
            return "redirect:/estoque";
        } catch (SQLException e) {
            System.out.println("ERRO ao excluir movimentação: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
}
