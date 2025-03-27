/*

 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.dao;

/**
 *
 * @author POSITIVO
 */
import main.java.interfaces.GenericRepository;
import main.java.model.Aluno;
import main.java.sqlite.ConexaoSQLiteJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static javafx.application.Application.launch;

public class AlunoDAO implements GenericRepository<Aluno> {

    public AlunoDAO() {

    }

    // Método para adicionar um novo aluno ao banco de dados
    // Método para obter um aluno pelo ID
    // Método para atualizar um aluno no banco de dados
    // Método para excluir um aluno do banco de dados
    // Método para obter todos os alunos do banco de dados
    @Override
    public List<Aluno> getAll() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM tb_Aluno";

        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        Aluno aluno = new Aluno();
                        aluno.setId(rs.getInt("ID"));
                        aluno.setNome(rs.getString("NOME"));
                        aluno.setFone(rs.getString("FONE"));
                        aluno.setDataNasc(rs.getDate("DATANASC"));
                        aluno.setCpf(rs.getString("CPF"));
                        aluno.setEmail(rs.getString("EMAIL"));
                        aluno.setEndereco(rs.getString("ENDERECO"));
                        aluno.setStatus(rs.getBoolean("STATUS"));
                        alunos.add(aluno);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter todos os alunos.");
            e.printStackTrace();
        }

        return alunos;

    }

    @Override
    public Optional<Aluno> find(Integer id) throws SQLException {
        Optional<Aluno> a = Optional.empty();
        String sql = "SELECT * FROM tb_Aluno WHERE ID = ?";
        try (Connection conn = ConexaoSQLiteJDBC.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rstt = stmt.executeQuery();
            if (rstt.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rstt.getInt("ID"));
                aluno.setNome(rstt.getString("NOME"));
                aluno.setFone(rstt.getString("FONE"));
                aluno.setDataNasc(rstt.getDate("DATANASC"));
                aluno.setCpf(rstt.getString("CPF"));
                aluno.setEmail(rstt.getString("EMAIL"));
                aluno.setEndereco(rstt.getString("ENDERECO"));
                aluno.setStatus(rstt.getBoolean("STATUS"));
                a = Optional.of(aluno);
                System.out.println("Aluno encontrado: " + aluno);
            } else {
                System.out.println("Aluno não encontrado para ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter aluno pelo ID. " + e);
            e.printStackTrace();
        }

        return a;
    }

    @Override
    public Boolean update(Aluno aluno) throws SQLException {
        String sql = "UPDATE tb_Aluno SET NOME = ?, FONE = ?, DATANASC = ?, CPF = ?, EMAIL = ?, ENDERECO = ?, STATUS = ? WHERE ID = ?";
        Optional<Aluno> aluno_bd = this.find(aluno.getId());
        aluno_bd.get().setNome(aluno.getNome());
        aluno_bd.get().setFone(aluno.getFone());
        aluno_bd.get().setDataNasc(aluno.getDataNasc());
        aluno_bd.get().setCpf(aluno.getCpf());
        aluno_bd.get().setEmail(aluno.getEmail());
        aluno_bd.get().setEndereco(aluno.getEndereco());
        aluno_bd.get().setStatus(aluno.getStatus());
        boolean result = false;
        if (aluno_bd.isPresent()) {

            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, aluno_bd.get().getNome());
                    stmt.setString(2, aluno_bd.get().getFone());
                    stmt.setDate(3, new java.sql.Date(aluno_bd.get().getDataNasc().getTime()));
                    if (aluno_bd.get().getCpf() == null) {
                        stmt.setNull(4, Types.VARCHAR);
                    } else {
                        if (aluno_bd.get().getCpf().isEmpty()) {
                            stmt.setNull(4, Types.VARCHAR);
                        } else {
                            stmt.setString(4, aluno_bd.get().getCpf());
                        }
                    }
                    if (aluno_bd.get().getEmail() == null) {

                        stmt.setNull(5, Types.VARCHAR);
                    } else {
                        if (aluno_bd.get().getEmail().isEmpty()) {
                            stmt.setNull(5, Types.VARCHAR);
                        } else {
                            stmt.setString(5, aluno_bd.get().getEmail());
                        }
                    }
                    if (aluno_bd.get().getEndereco() == null) {

                        stmt.setNull(6, Types.VARCHAR);
                    } else {
                        if (aluno_bd.get().getEndereco().isEmpty()) {
                            stmt.setNull(6, Types.VARCHAR);
                        } else {
                            stmt.setString(6, aluno_bd.get().getEndereco());
                        }

                    }
                    stmt.setInt(7, aluno_bd.get().getStatus() ? 1 : 0);
                    stmt.setInt(8, aluno_bd.get().getId());
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Aluno atualizado com sucesso." + aluno);
                        result = true;
                    } else {
                        System.out.println("Nenhum aluno foi atualizado.");
                        return result = false;
                    }

                }
            } catch (SQLException e) {
                System.err.println("Erro ao atualizar o aluno.");
                e.printStackTrace();
            }
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public Boolean delete(Aluno a) throws SQLException {
        String sql = "DELETE FROM tb_Aluno WHERE ID = ?";

        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, a.getId());

                stmt.executeUpdate();

                System.out.println("Aluno excluído com sucesso.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o aluno.");
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Boolean create(Aluno aluno) throws SQLException {
        Integer generateId = null;
        String sql = "INSERT INTO tb_Aluno (NOME, FONE, DATANASC, CPF, EMAIL, ENDERECO,STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean rst = false;
        if (aluno.getId() != null) {
            // Se o aluno já tiver um ID, atualiza-o
            System.out.println("metodo put");
            rst = update(aluno);
            return rst;
        } else {
            System.out.println(aluno.getId());

            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, aluno.getNome());
                    stmt.setString(2, aluno.getFone());
                    stmt.setDate(3, new java.sql.Date(aluno.getDataNasc().getTime()));

                    if (aluno.getCpf().isEmpty()) {
                        stmt.setNull(4, Types.VARCHAR);
                    } else {
                        stmt.setString(4, aluno.getCpf());

                    }
                    if (aluno.getEmail().isEmpty()) {
                        stmt.setNull(5, Types.VARCHAR);
                    } else {
                        stmt.setString(5, aluno.getEmail());

                    }
                    if (aluno.getEmail().isEmpty()) {
                        stmt.setNull(6, Types.VARCHAR);
                    } else {
                        stmt.setString(6, aluno.getEndereco());

                    }

                    stmt.setInt(7, aluno.getStatus() ? 1 : 0);
                    stmt.executeUpdate();
                    try (ResultSet rsId = conn.createStatement().executeQuery("SELECT LAST_INSERT_ROWID()")) {
                        if (rsId.next()) {
                            int idInserido = rsId.getInt(1);
                            System.out.println("ID inserido recuperado: " + idInserido);
                        } else {
                            throw new SQLException("Falha ao recuperar o ID inserido.");
                        }
                    }
                    System.out.println("Aluno adicionado com sucesso.");
                    rst = true;
                }
            } catch (SQLException e) {
                rst = false;

                System.err.println("Erro '" + e.getMessage() + "' já existe.");
            }

            return rst;
        }
    }

    public Boolean duplicatephone(Aluno a) throws SQLException {
        boolean rst = false;
        // Check for duplicate phone number first
        if (a.getId() != null) {
            String sql = "SELECT * FROM tb_Aluno WHERE FONE = ? AND ID != ?";
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, a.getFone());
                    stmt.setInt(2, a.getId());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        // Phone number already exists, throw an exception
                        //throw new SQLException("Error: fone number already exists.");
                        rst = true;
                    }
                }
            } catch (SQLException e) {
                rst = false;
                throw new SQLException(e.getMessage());
                //System.err.println("Erro: Aluno com CPF '" +e.getMessage()+ "' já existe.");  
            }
        } else {
            String sql = "SELECT * FROM tb_Aluno WHERE FONE = ? ";
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, a.getFone());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        // Phone number already exists, throw an exception
                        //throw new SQLException("Error: fone number already exists.");
                        rst = true;
                    }
                }
            } catch (SQLException e) {
                rst = false;
                throw new SQLException(e.getMessage());
                //System.err.println("Erro: Aluno com CPF '" +e.getMessage()+ "' já existe.");  
            }
        }
        return rst;
    }

    public Boolean duplicateCpf(Aluno a) throws SQLException {
        boolean rst = false;
        // Check for duplicate phone number first
        if (a.getId() != null) {
            String sql = "SELECT * FROM tb_Aluno WHERE CPF = ? AND ID != ?";
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, a.getCpf());
                    stmt.setInt(2, a.getId());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        // Phone number already exists, throw an exception
                        //throw new SQLException("Error: fone number already exists.");
                        rst = true;
                    }
                }
            } catch (SQLException e) {
                rst = false;
                throw new SQLException(e.getMessage());
                //System.err.println("Erro: Aluno com CPF '" +e.getMessage()+ "' já existe.");  
            }
        } else {
            String sql = "SELECT * FROM tb_Aluno WHERE CPF = ? ";
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, a.getCpf());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        // Phone number already exists, throw an exception
                        //throw new SQLException("Error: fone number already exists.");
                        rst = true;
                    }
                }
            } catch (SQLException e) {
                rst = false;
                throw new SQLException(e.getMessage());
                //System.err.println("Erro: Aluno com CPF '" +e.getMessage()+ "' já existe.");  
            }
        }
        return rst;
    }

    public static int contarTotalAlunos() {
        String sql = "SELECT COUNT(*) AS total_alunos FROM tb_Aluno";
        int total = 0;

        try (Connection conn = ConexaoSQLiteJDBC.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total_alunos");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao contar alunos: " + e.getMessage());
        }

        return total;
    }

}
