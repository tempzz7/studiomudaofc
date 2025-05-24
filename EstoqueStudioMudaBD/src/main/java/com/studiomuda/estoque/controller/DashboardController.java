package com.studiomuda.estoque.controller;

import com.studiomuda.estoque.dao.ClienteDAO;
import com.studiomuda.estoque.dao.ItemPedidoDAO;
import com.studiomuda.estoque.dao.PedidoDAO;
import com.studiomuda.estoque.model.Pedido;
import com.studiomuda.estoque.model.Cliente;
import com.studiomuda.estoque.model.ItemPedido;
import com.studiomuda.estoque.dto.DashboardDTO;
import com.studiomuda.estoque.conexao.Conexao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    // Página principal
    @GetMapping
    public String dashboard(Model model) {
        return "dashboard";
    }

    // Endpoint para pedidos recentes
    @GetMapping("/api/recentes-pedidos")
    @ResponseBody
    public List<DashboardDTO.PedidoResumo> getRecentesPedidos() {
        List<DashboardDTO.PedidoResumo> lista = new ArrayList<>();
        try {
            List<Pedido> pedidos = pedidoDAO.listar();
            // Ordena por data de requisição decrescente (mais recente primeiro)
            pedidos.sort((a, b) -> b.getDataRequisicao().compareTo(a.getDataRequisicao()));
            int limite = Math.min(10, pedidos.size());
            for (int i = 0; i < limite; i++) {
                Pedido pedido = pedidos.get(i);
                DashboardDTO.PedidoResumo dto = new DashboardDTO.PedidoResumo();
                dto.id = pedido.getId();
                dto.clienteNome = pedido.getClienteNome();
                dto.dataRequisicao = pedido.getDataRequisicao();
                dto.dataEntrega = pedido.getDataEntrega();
                // Calcular valor total
                double valorTotal = 0.0;
                List<ItemPedido> itens = itemPedidoDAO.listarPorPedido(pedido.getId());
                for (ItemPedido item : itens) {
                    valorTotal += item.getProdutoValor() * item.getQuantidade();
                }
                // Aplica desconto se houver
                valorTotal -= pedido.getValorDesconto();
                dto.valorTotal = Math.max(0, valorTotal);
                // Status real do pedido
                dto.status = (pedido.getStatus() != null && !pedido.getStatus().isBlank()) ? pedido.getStatus() : "Desconhecido";
                lista.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Endpoint para clientes ativos
    @GetMapping("/api/clientes-ativos")
    @ResponseBody
    public List<DashboardDTO.ClienteResumo> getClientesAtivos() {
        List<DashboardDTO.ClienteResumo> lista = new ArrayList<>();
        try {
            List<Cliente> clientes = clienteDAO.listar();
            List<Pedido> pedidos = pedidoDAO.listar();
            for (Cliente cliente : clientes) {
                if (!cliente.isAtivo()) continue;
                DashboardDTO.ClienteResumo dto = new DashboardDTO.ClienteResumo();
                dto.id = cliente.getId();
                dto.nome = cliente.getNome();
                // Filtra pedidos deste cliente
                int count = 0;
                java.sql.Date ultimaCompra = null;
                for (Pedido pedido : pedidos) {
                    if (pedido.getClienteId() == cliente.getId()) {
                        count++;
                        if (ultimaCompra == null || pedido.getDataRequisicao().after(ultimaCompra)) {
                            ultimaCompra = pedido.getDataRequisicao();
                        }
                    }
                }
                dto.pedidos = count;
                dto.ultimaCompra = ultimaCompra;
                lista.add(dto);
            }
            // Ordena por última compra (mais recente primeiro)
            lista.sort((a, b) -> {
                if (b.ultimaCompra == null && a.ultimaCompra == null) return 0;
                if (b.ultimaCompra == null) return -1;
                if (a.ultimaCompra == null) return 1;
                return b.ultimaCompra.compareTo(a.ultimaCompra);
            });
            // Limita a 10 clientes
            if (lista.size() > 10) {
                lista = lista.subList(0, 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Endpoint para buscar categorias
    @GetMapping("/categorias")
    @ResponseBody
    public List<String> getCategorias() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT DISTINCT tipo FROM produto WHERE tipo IS NOT NULL";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categorias.add(rs.getString("tipo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    // Endpoint dinâmico com filtro por categoria
    // Endpoint para top clientes
    @GetMapping("/api/top-clientes")
    @ResponseBody
    public List<Map<String, Object>> getTopClientes() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT c.nome, COUNT(p.id) as totalPedidos " +
                "FROM cliente c " +
                "JOIN pedido p ON c.id = p.cliente_id " +
                "GROUP BY c.nome " +
                "ORDER BY totalPedidos DESC " +
                "LIMIT 10";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("nome", rs.getString("nome"));
                map.put("totalPedidos", rs.getInt("totalPedidos"));
                lista.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Endpoint para top produtos
    @GetMapping("/api/top-produtos")
    @ResponseBody
    public List<Map<String, Object>> getTopProdutos() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT p.nome as produtoNome, SUM(ip.quantidade) as quantidadeVendida " +
                "FROM item_pedido ip " +
                "JOIN produto p ON ip.id_produto = p.id " +
                "GROUP BY p.nome " +
                "ORDER BY quantidadeVendida DESC " +
                "LIMIT 10";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("produtoNome", rs.getString("produtoNome"));
                map.put("quantidadeVendida", rs.getInt("quantidadeVendida"));
                lista.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Endpoint para top cidades
    @GetMapping("/api/top-cidades")
    @ResponseBody
    public List<Map<String, Object>> getTopCidades() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT c.cidade, COUNT(p.id) as vendas " +
                "FROM cliente c " +
                "JOIN pedido p ON c.id = p.cliente_id " +
                "GROUP BY c.cidade " +
                "ORDER BY vendas DESC " +
                "LIMIT 10";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("cidade", rs.getString("cidade"));
                map.put("vendas", rs.getInt("vendas"));
                lista.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Endpoint para top auxiliares
    @GetMapping("/api/top-auxiliares")
    @ResponseBody
    public List<Map<String, Object>> getTopAuxiliares() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT f.nome as auxiliar, COUNT(p.id) as vendas " +
                "FROM funcionario f " +
                "JOIN pedido p ON f.id = p.funcionario_id " +
                "WHERE f.cargo = 'Auxiliar' " +
                "GROUP BY f.nome " +
                "ORDER BY vendas DESC " +
                "LIMIT 10";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("auxiliar", rs.getString("auxiliar"));
                map.put("vendas", rs.getInt("vendas"));
                lista.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Endpoint para top faixa etária
    @GetMapping("/api/top-faixa-etaria")
    @ResponseBody
    public List<Map<String, Object>> getTopFaixaEtaria() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT " +
                "CASE " +
                "  WHEN TIMESTAMPDIFF(YEAR, c.dataNascimento, CURDATE()) BETWEEN 18 AND 25 THEN '18-25' " +
                "  WHEN TIMESTAMPDIFF(YEAR, c.dataNascimento, CURDATE()) BETWEEN 26 AND 35 THEN '26-35' " +
                "  WHEN TIMESTAMPDIFF(YEAR, c.dataNascimento, CURDATE()) BETWEEN 36 AND 45 THEN '36-45' " +
                "  WHEN TIMESTAMPDIFF(YEAR, c.dataNascimento, CURDATE()) BETWEEN 46 AND 55 THEN '46-55' " +
                "  ELSE '55+' " +
                "END AS faixa, " +
                "COUNT(p.id) as vendas " +
                "FROM cliente c " +
                "JOIN pedido p ON c.id = p.cliente_id " +
                "WHERE c.dataNascimento IS NOT NULL " +
                "GROUP BY faixa " +
                "ORDER BY vendas DESC";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("faixa", rs.getString("faixa"));
                map.put("vendas", rs.getInt("vendas"));
                lista.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}