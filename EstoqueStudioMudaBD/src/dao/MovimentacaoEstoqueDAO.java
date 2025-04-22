package dao;

import conexao.Conexao;
import model.MovimentacaoEstoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoEstoqueDAO {

    public void registrar(MovimentacaoEstoque mov) throws SQLException {
        String sqlMov = "INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) VALUES (?, ?, ?, ?, ?)";
        String sqlEstoque = "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtMov = conn.prepareStatement(sqlMov);
             PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)) {

            conn.setAutoCommit(false);

            stmtMov.setInt(1, mov.getIdProduto());
            stmtMov.setString(2, mov.getTipo());
            stmtMov.setInt(3, mov.getQuantidade());
            stmtMov.setString(4, mov.getMotivo());
            stmtMov.setDate(5, mov.getData());
            stmtMov.executeUpdate();

            int fator = mov.getTipo().equalsIgnoreCase("saida") ? -1 : 1;
            stmtEstoque.setInt(1, fator * mov.getQuantidade());
            stmtEstoque.setInt(2, mov.getIdProduto());
            stmtEstoque.executeUpdate();

            conn.commit();
        }
    }

    public List<MovimentacaoEstoque> listar() throws SQLException {
        List<MovimentacaoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao_estoque ORDER BY data DESC";

        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MovimentacaoEstoque m = new MovimentacaoEstoque(
                        rs.getInt("id"),
                        rs.getInt("id_produto"),
                        rs.getString("tipo"),
                        rs.getInt("quantidade"),
                        rs.getString("motivo"),
                        rs.getDate("data")
                );
                lista.add(m);
            }
        }
        return lista;
    }

    public MovimentacaoEstoque buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM movimentacao_estoque WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MovimentacaoEstoque(
                        rs.getInt("id"),
                        rs.getInt("id_produto"),
                        rs.getString("tipo"),
                        rs.getInt("quantidade"),
                        rs.getString("motivo"),
                        rs.getDate("data")
                );
            }
        }
        return null;
    }

    public void atualizar(MovimentacaoEstoque mov) throws SQLException {
        String sql = "UPDATE movimentacao_estoque SET motivo = ?, data = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mov.getMotivo());
            stmt.setDate(2, mov.getData());
            stmt.setInt(3, mov.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String busca = "SELECT tipo, quantidade, id_produto FROM movimentacao_estoque WHERE id = ?";
        String removerMov = "DELETE FROM movimentacao_estoque WHERE id = ?";
        String reverterEstoque = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtBusca = conn.prepareStatement(busca);
             PreparedStatement stmtDeleta = conn.prepareStatement(removerMov);
             PreparedStatement stmtEstoque = conn.prepareStatement(reverterEstoque)) {

            stmtBusca.setInt(1, id);
            ResultSet rs = stmtBusca.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                int quantidade = rs.getInt("quantidade");
                int idProduto = rs.getInt("id_produto");

                int fator = tipo.equalsIgnoreCase("saida") ? 1 : -1;

                stmtEstoque.setInt(1, fator * quantidade);
                stmtEstoque.setInt(2, idProduto);
                stmtEstoque.executeUpdate();

                stmtDeleta.setInt(1, id);
                stmtDeleta.executeUpdate();
            }
        }
    }
}
