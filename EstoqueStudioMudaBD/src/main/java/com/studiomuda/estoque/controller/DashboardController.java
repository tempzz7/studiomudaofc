package com.studiomuda.estoque.controller;

import com.studiomuda.estoque.dao.ClienteDAO;
import com.studiomuda.estoque.dao.ItemPedidoDAO;
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

    // Página principal
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("topProdutos", getTopProdutosMaisVendidos(null));
        model.addAttribute("topClientes", getTopClientes());
        model.addAttribute("categorias", getCategorias());
        return "dashboard";
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
    @GetMapping("/top-produtos")
    @ResponseBody
    public List<Map<String, Object>> getTopProdutosComFiltro(@RequestParam(required = false) String categoria) {
        return getTopProdutosMaisVendidos(categoria);
    }

    // Endpoint para top clientes
    @GetMapping("/top-clientes")
    @ResponseBody
    public List<Map<String, Object>> getTopClientesEndpoint() {
        return getTopClientes();
    }

    // Endpoint para top cidades
    @GetMapping("/top-cidades")
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

    // Endpoint para top faixa etária
    @GetMapping("/top-faixa-etaria")
    @ResponseBody
    public List<Map<String, Object>> getTopFaixaEtaria() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT " +
                "CASE " +
                "  WHEN c.idade BETWEEN 18 AND 25 THEN '18-25' " +
                "  WHEN c.idade BETWEEN 26 AND 35 THEN '26-35' " +
                "  WHEN c.idade BETWEEN 36 AND 45 THEN '36-45' " +
                "  WHEN c.idade BETWEEN 46 AND 55 THEN '46-55' " +
                "  ELSE '55+' " +
                "END AS faixa, " +
                "COUNT(p.id) as vendas " +
                "FROM cliente c " +
                "JOIN pedido p ON c.id = p.cliente_id " +
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

    // Endpoint para top auxiliares    @GetMapping("/top-auxiliares")
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

    // Lógica para top produtos, com ou sem filtro
    private List<Map<String, Object>> getTopProdutosMaisVendidos(String categoria) {
        List<Map<String, Object>> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.nome as produtoNome, SUM(ip.quantidade) as quantidadeVendida " +
                "FROM item_pedido ip " +
                "JOIN produto p ON ip.id_produto = p.id "
        );

        if (categoria != null && !categoria.isEmpty()) {
            sql.append("WHERE p.tipo = ? ");
        }

        sql.append("GROUP BY p.nome ORDER BY quantidadeVendida DESC LIMIT 10");

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (categoria != null && !categoria.isEmpty()) {
                stmt.setString(1, categoria);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("produtoNome", rs.getString("produtoNome"));
                    map.put("quantidadeVendida", rs.getInt("quantidadeVendida"));
                    lista.add(map);
                }
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
}