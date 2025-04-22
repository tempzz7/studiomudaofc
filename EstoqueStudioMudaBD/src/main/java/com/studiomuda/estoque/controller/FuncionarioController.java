package com.studiomuda.estoque.controller;

import com.studiomuda.estoque.dao.FuncionarioDAO;
import com.studiomuda.estoque.model.Funcionario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @GetMapping
    public String listarFuncionarios(Model model) {
        try {
            model.addAttribute("funcionarios", funcionarioDAO.listar());
            return "funcionarios/lista";
        } catch (SQLException e) {
            model.addAttribute("mensagemErro", "Erro ao listar funcionários: " + e.getMessage());
            return "erro";
        }
    }

    @GetMapping("/novo")
    public String formNovoFuncionario(Model model) {
        Funcionario funcionario = new Funcionario();
        funcionario.setAtivo(true);
        // Inicializar com a data atual
        funcionario.setData_nasc(Date.valueOf(LocalDate.now()));
        model.addAttribute("funcionario", funcionario);
        return "funcionarios/form";
    }

    @PostMapping("/salvar")
    public String salvarFuncionario(@ModelAttribute Funcionario funcionario, @RequestParam(value = "dataNascimento", required = false) String dataNascimento) {
        try {
            // Converter a string de data para Date se não for nula
            if (dataNascimento != null && !dataNascimento.isEmpty()) {
                funcionario.setData_nasc(Date.valueOf(dataNascimento));
            }
            
            if (funcionario.getId() == 0) {
                funcionarioDAO.inserir(funcionario);
            } else {
                funcionarioDAO.atualizar(funcionario);
            }
            return "redirect:/funcionarios";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }

    @GetMapping("/editar/{id}")
    public String editarFuncionario(@PathVariable("id") int id, Model model) {
        try {
            Funcionario funcionario = funcionarioDAO.buscarPorId(id);
            if (funcionario != null) {
                model.addAttribute("funcionario", funcionario);
                return "funcionarios/form";
            } else {
                return "redirect:/funcionarios";
            }
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirFuncionario(@PathVariable("id") int id) {
        try {
            System.out.println("Tentando inativar funcionário ID: " + id);
            funcionarioDAO.deletar(id);
            System.out.println("Funcionário inativado com sucesso!");
            return "redirect:/funcionarios";
        } catch (SQLException e) {
            System.out.println("ERRO ao inativar funcionário: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
}
