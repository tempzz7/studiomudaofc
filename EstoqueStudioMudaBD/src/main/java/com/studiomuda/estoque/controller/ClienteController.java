package com.studiomuda.estoque.controller;

import com.studiomuda.estoque.dao.ClienteDAO;
import com.studiomuda.estoque.model.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @GetMapping
    public String listarClientes(Model model) {
        try {
            model.addAttribute("clientes", clienteDAO.listar());
            return "clientes/lista";
        } catch (SQLException e) {
            model.addAttribute("mensagemErro", "Erro ao listar clientes: " + e.getMessage());
            return "erro";
        }
    }
    
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<?> listarClientesApi() {
        try {
            List<Cliente> clientes = clienteDAO.listar();
            return ResponseEntity.ok(clientes);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao listar clientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/novo")
    public String formNovoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente) {
        try {
            if (cliente.getId() == 0) {
                clienteDAO.inserir(cliente);
            } else {
                clienteDAO.atualizar(cliente);
            }
            return "redirect:/clientes";
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
    
    @PostMapping("/api/salvar")
    @ResponseBody
    public ResponseEntity<?> salvarClienteApi(@RequestBody Cliente cliente) {
        try {
            if (cliente.getId() == 0) {
                clienteDAO.inserir(cliente);
            } else {
                clienteDAO.atualizar(cliente);
            }
            return ResponseEntity.ok(cliente);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao salvar cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable("id") int id, Model model) {
        try {
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente != null) {
                model.addAttribute("cliente", cliente);
                return "clientes/form";
            } else {
                return "redirect:/clientes";
            }
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarClienteApi(@PathVariable("id") int id) {
        try {
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente != null) {
                return ResponseEntity.ok(cliente);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao buscar cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable("id") int id) {
        try {
            clienteDAO.deletar(id);
            return "redirect:/clientes";
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
    
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> excluirClienteApi(@PathVariable("id") int id) {
        try {
            clienteDAO.deletar(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Cliente excluído com sucesso");
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao excluir cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
