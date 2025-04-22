package com.studiomuda.estoque.dao;

import com.studiomuda.estoque.conexao.Conexao;
import com.studiomuda.estoque.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void inserir(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (data_requisicao, data_entrega, cliente_id) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, pedido.getDataRequisicao());
            stmt.setDate(2, pedido.getDataEntrega());
            stmt.setInt(3, pedido.getClienteId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pedido.setId(rs.getInt(1));
            }
        }
    }

    public List<Pedido> listar() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nome as cliente_nome FROM pedido p " +
                     "LEFT JOIN cliente c ON p.cliente_id = c.id";

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido p = new Pedido(
                        rs.getInt("id"),
                        rs.getDate("data_requisicao"),
                        rs.getDate("data_entrega"),
                        rs.getInt("cliente_id")
                );
                p.setClienteNome(rs.getString("cliente_nome"));
                lista.add(p);
            }
        }
        return lista;
    }

    public Pedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT p.*, c.nome as cliente_nome FROM pedido p " +
                     "LEFT JOIN cliente c ON p.cliente_id = c.id " +
                     "WHERE p.id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pedido p = new Pedido(
                        rs.getInt("id"),
                        rs.getDate("data_requisicao"),
                        rs.getDate("data_entrega"),
                        rs.getInt("cliente_id")
                );
                p.setClienteNome(rs.getString("cliente_nome"));
                return p;
            }
        }
        return null;
    }

    public void atualizar(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedido SET data_requisicao = ?, data_entrega = ?, cliente_id = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, pedido.getDataRequisicao());
            stmt.setDate(2, pedido.getDataEntrega());
            stmt.setInt(3, pedido.getClienteId());
            stmt.setInt(4, pedido.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int obterUltimoId() throws SQLException {
        String sql = "SELECT MAX(id) AS ultimo_id FROM pedido";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("ultimo_id");
            }
        }
        return 0;
    }
}
