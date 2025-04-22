package com.studiomuda.estoque.controller;

import com.studiomuda.estoque.dao.ClienteDAO;
import com.studiomuda.estoque.dao.ItemPedidoDAO;
import com.studiomuda.estoque.dao.PedidoDAO;
import com.studiomuda.estoque.dao.ProdutoDAO;
import com.studiomuda.estoque.model.ItemPedido;
import com.studiomuda.estoque.model.Pedido;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @GetMapping
    public String listarPedidos(Model model) {
        try {
            model.addAttribute("pedidos", pedidoDAO.listar());
            return "pedidos/lista";
        } catch (SQLException e) {
            model.addAttribute("mensagemErro", "Erro ao listar pedidos: " + e.getMessage());
            return "erro";
        }
    }
    
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<?> listarPedidosApi() {
        try {
            List<Pedido> pedidos = pedidoDAO.listar();
            return ResponseEntity.ok(pedidos);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao listar pedidos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/novo")
    public String formNovoPedido(Model model) {
        try {
            Pedido pedido = new Pedido();
            pedido.setDataRequisicao(Date.valueOf(LocalDate.now()));
            
            model.addAttribute("pedido", pedido);
            model.addAttribute("clientes", clienteDAO.listarAtivos());
            return "pedidos/form";
        } catch (SQLException e) {
            model.addAttribute("mensagemErro", "Erro ao preparar formulário: " + e.getMessage());
            return "erro";
        }
    }

    @PostMapping("/salvar")
    public String salvarPedido(@ModelAttribute Pedido pedido, 
                              @RequestParam(value = "dataRequisicaoStr", required = false) String dataRequisicaoStr,
                              @RequestParam(value = "dataEntregaStr", required = false) String dataEntregaStr) {
        try {
            // Converter strings de data para Date
            if (dataRequisicaoStr != null && !dataRequisicaoStr.isEmpty()) {
                pedido.setDataRequisicao(Date.valueOf(dataRequisicaoStr));
            }
            
            if (dataEntregaStr != null && !dataEntregaStr.isEmpty()) {
                pedido.setDataEntrega(Date.valueOf(dataEntregaStr));
            }
            
            if (pedido.getId() == 0) {
                pedidoDAO.inserir(pedido);
            } else {
                pedidoDAO.atualizar(pedido);
            }
            return "redirect:/pedidos/itens/" + pedido.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
    
    @PostMapping("/api/salvar")
    @ResponseBody
    public ResponseEntity<?> salvarPedidoApi(@RequestBody Pedido pedido) {
        try {
            if (pedido.getId() == 0) {
                pedidoDAO.inserir(pedido);
            } else {
                pedidoDAO.atualizar(pedido);
            }
            return ResponseEntity.ok(pedido);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao salvar pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/editar/{id}")
    public String editarPedido(@PathVariable("id") int id, Model model) {
        try {
            Pedido pedido = pedidoDAO.buscarPorId(id);
            if (pedido != null) {
                model.addAttribute("pedido", pedido);
                model.addAttribute("clientes", clienteDAO.listarAtivos());
                return "pedidos/form";
            } else {
                return "redirect:/pedidos";
            }
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarPedidoApi(@PathVariable("id") int id) {
        try {
            Pedido pedido = pedidoDAO.buscarPorId(id);
            if (pedido != null) {
                return ResponseEntity.ok(pedido);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao buscar pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluirPedido(@PathVariable("id") int id) {
        try {
            System.out.println("Tentando excluir pedido ID: " + id);
            // Primeiro excluir os itens do pedido
            System.out.println("Excluindo itens do pedido...");
            itemPedidoDAO.deletarPorPedido(id);
            // Depois excluir o pedido
            System.out.println("Excluindo o pedido...");
            pedidoDAO.deletar(id);
            System.out.println("Pedido excluído com sucesso!");
            return "redirect:/pedidos";
        } catch (SQLException e) {
            System.out.println("ERRO ao excluir pedido: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
    
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> excluirPedidoApi(@PathVariable("id") int id) {
        try {
            // Primeiro excluir os itens do pedido
            itemPedidoDAO.deletarPorPedido(id);
            // Depois excluir o pedido
            pedidoDAO.deletar(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Pedido excluído com sucesso");
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao excluir pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/itens/{pedidoId}")
    public String listarItensPedido(@PathVariable("pedidoId") int pedidoId, Model model) {
        try {
            Pedido pedido = pedidoDAO.buscarPorId(pedidoId);
            List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(pedidoId);
            double valorTotal = 0.0;
            
            // Calcular o valor total dos itens do pedido
            for (ItemPedido itemPedido : itens) {
                valorTotal += itemPedido.getSubtotal();
            }
            
            model.addAttribute("pedido", pedido);
            model.addAttribute("itens", itens);
            model.addAttribute("novoItem", new ItemPedido());
            model.addAttribute("produtos", produtoDAO.listar());
            model.addAttribute("valorTotal", valorTotal);
            
            return "pedidos/itens";
        } catch (SQLException e) {
            model.addAttribute("mensagemErro", "Erro ao listar itens do pedido: " + e.getMessage());
            return "erro";
        }
    }
    
    @GetMapping("/api/itens/{pedidoId}")
    @ResponseBody
    public ResponseEntity<?> listarItensPedidoApi(@PathVariable("pedidoId") int pedidoId) {
        try {
            List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(pedidoId);
            return ResponseEntity.ok(itens);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao listar itens do pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/itens/adicionar")
    public String adicionarItemPedido(@ModelAttribute ItemPedido item) {
        try {
            itemPedidoDAO.inserir(item);
            return "redirect:/pedidos/itens/" + item.getPedidoId();
        } catch (SQLException e) {
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
    
    @PostMapping("/api/itens/adicionar")
    @ResponseBody
    public ResponseEntity<?> adicionarItemPedidoApi(@RequestBody ItemPedido item) {
        try {
            itemPedidoDAO.inserir(item);
            return ResponseEntity.ok(item);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao adicionar item ao pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/itens/excluir/{id}")
    public String excluirItemPedido(@PathVariable("id") int id) {
        try {
            System.out.println("Tentando excluir item de pedido ID: " + id);
            // Buscar o item para obter o ID do pedido antes de excluir
            ItemPedido itemPedido = itemPedidoDAO.buscarPorId(id);
            int pedidoId = itemPedido.getPedidoId();
            System.out.println("Item pertence ao pedido ID: " + pedidoId);
            System.out.println("Excluindo item...");
            itemPedidoDAO.deletar(id);
            System.out.println("Item excluído com sucesso!");
            return "redirect:/pedidos/itens/" + pedidoId;
        } catch (SQLException e) {
            System.out.println("ERRO ao excluir item de pedido: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/erro?mensagem=" + e.getMessage();
        }
    }
    
    @DeleteMapping("/api/itens/{id}")
    @ResponseBody
    public ResponseEntity<?> excluirItemPedidoApi(@PathVariable("id") int id) {
        try {
            ItemPedido item = itemPedidoDAO.buscarPorId(id);
            itemPedidoDAO.deletar(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Item excluído com sucesso");
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao excluir item do pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
