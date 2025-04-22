package dao;

import conexao.Conexao;
import model.ItemPedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    public void inserir(ItemPedido item) throws SQLException {
        String sql = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getPedidoId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidade());
            stmt.executeUpdate();
        }
    }

    public List<ItemPedido> listarPorPedido(int pedidoId) throws SQLException {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM item_pedido WHERE id_pedido = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItemPedido item = new ItemPedido(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_produto"),
                        rs.getInt("quantidade")
                );
                lista.add(item);
            }
        }

        return lista;
    }

    public ItemPedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM item_pedido WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ItemPedido(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_produto"),
                        rs.getInt("quantidade")
                );
            }
        }

        return null;
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM item_pedido WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
