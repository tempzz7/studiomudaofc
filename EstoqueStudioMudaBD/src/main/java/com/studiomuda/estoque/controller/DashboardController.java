package com.studiomuda.estoque.controller;

import com.studiomuda.estoque.dao.ClienteDAO;
import com.studiomuda.estoque.dao.ItemPedidoDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("topProdutos", getTopProdutosMaisVendidos());
        model.addAttribute("topClientes", getTopClientes());
        return "dashboard";
    }

    private List<Map<String, Object>> getTopProdutosMaisVendidos() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT p.nome as produtoNome, SUM(ip.quantidade) as quantidadeVendida " +
                "FROM item_pedido ip " +
                "JOIN produto p ON ip.id_produto = p.id " +
                "GROUP BY p.nome " +
                "ORDER BY quantidadeVendida DESC " +
                "LIMIT 10";
        try (Connection conn = com.studiomuda.estoque.conexao.Conexao.getConnection();
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

    private List<Map<String, Object>> getTopClientes() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT c.nome, COUNT(p.id) as totalPedidos " +
                "FROM cliente c " +
                "JOIN pedido p ON c.id = p.cliente_id " +
                "GROUP BY c.nome " +
                "ORDER BY totalPedidos DESC " +
                "LIMIT 10";
        try (Connection conn = com.studiomuda.estoque.conexao.Conexao.getConnection();
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
}
