package dao;

import conexao.Conexao;
import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (nome, cpf_cnpj, telefone, email, cep, rua, numero, bairro, cidade, estado, tipo, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getCpfCnpj());
            stmt.setString(3, c.getTelefone());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getCep());
            stmt.setString(6, c.getRua());
            stmt.setString(7, c.getNumero());
            stmt.setString(8, c.getBairro());
            stmt.setString(9, c.getCidade());
            stmt.setString(10, c.getEstado());
            stmt.setString(11, c.getTipo());
            stmt.setBoolean(12, c.isAtivo());

            stmt.executeUpdate();
        }
    }

    public Cliente buscarPorCpfCnpj(String cpfCnpj) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE cpf_cnpj = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfCnpj);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf_cnpj"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cep"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("tipo"),
                        rs.getBoolean("ativo")
                );
            }
        }
        return null;
    }

    public void atualizar(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET telefone = ?, email = ?, cep = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, tipo = ?, ativo = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getTelefone());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getCep());
            stmt.setString(4, c.getRua());
            stmt.setString(5, c.getNumero());
            stmt.setString(6, c.getBairro());
            stmt.setString(7, c.getCidade());
            stmt.setString(8, c.getEstado());
            stmt.setString(9, c.getTipo());
            stmt.setBoolean(10, c.isAtivo());
            stmt.setInt(11, c.getId());

            stmt.executeUpdate();
        }
    }

    public void deletar(String cpfCnpj) throws SQLException {
        String sql = "UPDATE cliente SET ativo = false WHERE cpf_cnpj = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfCnpj);
            stmt.executeUpdate();
        }
    }

    public List<Cliente> listarAtivos() throws SQLException {
        return listarPorStatus(true);
    }

    public List<Cliente> listarInativos() throws SQLException {
        return listarPorStatus(false);
    }

    private List<Cliente> listarPorStatus(boolean ativo) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE ativo = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, ativo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf_cnpj"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cep"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("tipo"),
                        rs.getBoolean("ativo")
                );
                lista.add(c);
            }
        }

        return lista;
    }
}
