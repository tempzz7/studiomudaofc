package dao;

import conexao.Conexao;
import model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public void inserir(Funcionario f) throws SQLException {
        String sql = "INSERT INTO funcionario (nome, cpf, cargo, data_nasc, telefone, " +
                "cep, rua, numero, bairro, cidade, estado, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getCargo());
            stmt.setString(4, f.getData_nasc());
            stmt.setString(5, f.getTelefone());
            stmt.setString(6, f.getCep());
            stmt.setString(7, f.getRua());
            stmt.setString(8, f.getNumero());
            stmt.setString(9, f.getBairro());
            stmt.setString(10, f.getCidade());
            stmt.setString(11, f.getEstado());
            stmt.setBoolean(12, f.isAtivo());

            stmt.executeUpdate();
        }
    }

    public List<Funcionario> listar() throws SQLException {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Funcionario f = new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("cargo"),
                        rs.getString("data_nasc"),
                        rs.getString("telefone"),
                        rs.getString("cep"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getBoolean("ativo")
                );
                lista.add(f);
            }
        }
        return lista;
    }

    public Funcionario buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM funcionario WHERE cpf = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("cargo"),
                        rs.getString("data_nasc"),
                        rs.getString("telefone"),
                        rs.getString("cep"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getBoolean("ativo")
                );
            }
        }
        return null;
    }

    public void atualizar(Funcionario f) throws SQLException {
        String sql = "UPDATE funcionario SET telefone = ?, cep = ?, rua = ?, numero = ?, bairro = ?, " +
                "cidade = ?, estado = ?, cargo = ?, ativo = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getTelefone());
            stmt.setString(2, f.getCep());
            stmt.setString(3, f.getRua());
            stmt.setString(4, f.getNumero());
            stmt.setString(5, f.getBairro());
            stmt.setString(6, f.getCidade());
            stmt.setString(7, f.getEstado());
            stmt.setString(8, f.getCargo());
            stmt.setBoolean(9, f.isAtivo());
            stmt.setInt(10, f.getId());

            stmt.executeUpdate();
        }
    }

    public void deletar(String cpf) throws SQLException {
        String sql = "UPDATE funcionario SET ativo = false WHERE cpf = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }
}
