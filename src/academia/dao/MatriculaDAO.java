/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package academia.dao;

import academia.interfaces.GenericRepository;
import academia.model.Aluno;
import java.util.Date;
import academia.model.Matricula;
import academia.model.Modalidade;
import academia.sqlite.ConexaoSQLiteJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author POSITIVO
 */
public class MatriculaDAO implements GenericRepository<Matricula> {

    private Connection conn;

    public MatriculaDAO(Connection conexao) {
        this.conn = conexao;
    }

    @Override
    public Boolean insert(Matricula matricula) throws SQLException {
        String sql = "INSERT INTO tb_Matricula (DATAVIGENCIA,DATAVENCIMENTO,DATAFIM,VALOR,STATUS,ALUNO_id) VALUES (?, ?, ?, ?, ?,?)";
        boolean rst = false;
        if (matricula.getId() != null) {
            // Se o aluno já tiver um ID, atualiza-o
            System.out.println("metodo put");
            rst = put(matricula);
            return rst;
        } else {
            System.out.println(matricula.getId());

            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                stmt.setDate(1, new java.sql.Date(matricula.getDataDeVigencia().getTime()));
                stmt.setDate(2, new java.sql.Date(matricula.getDataVecimento().getTime()));
                stmt.setDate(3, new java.sql.Date(matricula.getDataFim().getTime()));
                stmt.setDouble(4, matricula.getValor());
                stmt.setInt(5, matricula.getStatus() ? 1 : 0);
                stmt.setInt(6, matricula.getAluno().getId());
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            matricula.setId(generatedKeys.getInt(1));
                            insertModalidades(matricula);
                            System.out.println("Aluno  matriculado com sucesso.");
                            rst = true;
                        }
                      
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao matricula o aluno." + e);
                rst = false;
            }

            return rst;
        }
    }

    private void insertModalidades(Matricula matricula) throws SQLException {
        String sql = "INSERT INTO tb_Matriculas_tb_Modalidades (matricula_id, modalidade_id) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Modalidade modalidade : matricula.getModalidades()) {
                stmt.setInt(1, matricula.getId());
                stmt.setInt(2, modalidade.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Erro ao associar modalidades à matrícula: " + e);
        }
    }

    @Override
    public Boolean put(Matricula matricula) throws SQLException {
        String sql = "UPDATE tb_Matricula SET dataDeVigencia = ?, dataVencimento = ?, dataFim = ?, valor = ?, status = ? ,ALUNO_id = ? WHERE id = ?";
        Optional<Matricula> matricula_bd = this.getById(matricula.getId());
        matricula_bd.get().setDataDeVigencia(matricula.getDataDeVigencia());
        matricula_bd.get().setDataVecimento(matricula.getDataVecimento());
        matricula_bd.get().setDataFim(matricula.getDataFim());
        matricula_bd.get().setValor(matricula.getValor());
        matricula_bd.get().setStatus(matricula.getStatus());
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(matricula_bd.get().getDataDeVigencia().getTime()));
            stmt.setDate(2, new java.sql.Date(matricula_bd.get().getDataVecimento().getTime()));
            stmt.setDate(3, new java.sql.Date(matricula_bd.get().getDataFim().getTime()));
            stmt.setDouble(4, matricula_bd.get().getValor());
            stmt.setInt(5, matricula_bd.get().getStatus() ? 1 : 0);
            stmt.setInt(6, matricula_bd.get().getAluno().getId());
            stmt.setInt(7, matricula_bd.get().getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                deleteModalidades(matricula_bd.get().getId());
                insertModalidades(matricula_bd.get());
                return true;
            }
 
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar matrícula: " + e.getMessage());
            return false;
        }
        return null;
    }
    private void deleteModalidades(int matriculaId) throws SQLException {
        String sql = "DELETE FROM tb_Matriculas_tb_Modalidades WHERE matricula_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matriculaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir modalidades da matrícula: " + e);
        }
    }

    @Override
    public Boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM tb_Matricula WHERE ID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            stmt.executeUpdate();

            System.out.println("matricula excluído com sucesso.");
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o ,matricula.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List getAll() throws SQLException {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT * FROM tb_Matricula";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("ID"));
                matricula.setDataDeVigencia(rs.getDate("DATAVIGENCIA"));
                matricula.setDataVecimento(rs.getDate("DATAVENCIMENTO"));
                matricula.setDataFim(rs.getDate("DATAFIM"));
                matricula.setValor(rs.getDouble("VALOR"));
                matricula.setStatus(rs.getBoolean("STATUS"));
                try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                // Inserir o aluno no banco usando o AlunoDAO
                AlunoDAO alunoDAO = new AlunoDAO();
               
                matricula.setAluno(alunoDAO.getById(rs.getInt("ID")).get());
                }
                matricula.setModalidades(getModalidadesByMatriculaId(rs.getInt("ID")));
                matriculas.add(matricula);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter todos os alunos.");
            e.printStackTrace();
        }

        return matriculas;
    }
     private List<Modalidade> getModalidadesByMatriculaId(int matriculaId) throws SQLException {
        List<Modalidade> modalidades = new ArrayList<>();
        String sql = "SELECT m.* FROM tb_Modalidade m JOIN tb_Matriculas_tb_Modalidades mm ON m.id = mm.modalidade_id WHERE mm.matricula_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matriculaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Modalidade modalidade = new Modalidade();
                modalidade.setId(rs.getInt("ID"));
                modalidade.setNome(rs.getString("NOME"));
                modalidade.setValor(rs.getDouble("VALOR"));
                modalidades.add(modalidade);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter modalidades por ID da matrícula: " + e);
        }

        return modalidades;
    }

    @Override
    public Optional<Matricula> getById(Integer id) throws SQLException {
        Optional<Matricula> m = Optional.empty();
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            String sql = "SELECT * FROM tb_Matricula WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("id"));
                matricula.setDataDeVigencia(rs.getDate("dataDeVigencia"));
                matricula.setDataVecimento(rs.getDate("dataVecimento"));
                matricula.setDataFim(rs.getDate("dataFim"));
                matricula.setValor(rs.getDouble("valor"));
                matricula.setStatus(rs.getBoolean("status"));
                AlunoDAO alunoDAO = new AlunoDAO();
                matricula.setAluno(alunoDAO.getById(rs.getInt("ID")).get());
                matricula.setModalidades(getModalidadesByMatriculaId(rs.getInt("ID")));
                m = Optional.of(matricula);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter aluno pelo ID.");
            e.printStackTrace();
        }
        return m;
    }
}
