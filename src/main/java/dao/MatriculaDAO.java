/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.dao;

import main.java.interfaces.GenericRepository;
import main.java.model.Aluno;
import main.java.model.Matricula;
import main.java.model.Modalidade;
import main.java.sqlite.ConexaoSQLiteJDBC;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author POSITIVO
 */
public class MatriculaDAO implements GenericRepository<Matricula> {

    public MatriculaDAO() {
    }

    @Override
    public Boolean create(Matricula matricula) throws SQLException {
        System.out.println("metodo create bd");
        String sql = "INSERT INTO tb_Matricula (DATAVIGENCIA, DATAVENCIMENTO, DATAINICIO, VALOR, STATUS, ID_ALUNO) VALUES (?, ?, ?, ?, ?, ?)";
        boolean result = false;
        if (matricula.getId() != null) {
            return update(matricula);
        } else {
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                System.out.println("conectar ao banco: ");
                // Inserir na tabela tb_Matricula
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    System.out.println("pepara para inseri na tabela tbmatricula ");
                    stmt.setDate(1, new java.sql.Date(matricula.getDataDeVigencia().getTime()));
                    stmt.setDate(2, new java.sql.Date(matricula.getDataVecimento().getTime()));
                    stmt.setDate(3, new java.sql.Date(matricula.getDataInicio().getTime()));
                    stmt.setDouble(4, matricula.getValor());
                    stmt.setInt(5, matricula.getStatus() ? 1 : 0);
                    stmt.setInt(6, matricula.getAluno().getId());
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Falha ao inserir matricula, nenhuma linha afetada.");
                    } else {
                        try (PreparedStatement pegaId = conn.prepareStatement("SELECT LAST_INSERT_ROWID() AS id")) {
                            System.out.println("pepara para pega id da tabela tb_matricula ");
                            ResultSet resultId = pegaId.executeQuery();
                            if (resultId.next()) {
                                int idInserido = resultId.getInt("id");
                                matricula.setId(idInserido);
                                System.out.println(matricula.getId());

                            } else {
                                throw new SQLException("Falha recupera id.");
                            }
                        } catch (SQLException e2) {
                            System.err.println("Erro pepara par pega id  : " + e2.getMessage());
                            e2.printStackTrace();
                        }
                    }
                } catch (SQLException e1) {
                    System.err.println("Erro ao pepara para inseri na tabela tbmatricula: " + e1.getMessage());
                    e1.printStackTrace();
                }
                updateModalidades(matricula);
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    private void updateModalidades(Matricula matricula) throws SQLException {

        for (Modalidade modalidade : matricula.getModalidades()) {
            addModalidade(matricula, modalidade);
        }
    }

    private void addModalidade(Matricula matricula, Modalidade modalidade) throws SQLException {
        String sql = "INSERT INTO tb_Matriculas_tb_Modalidades (matricula_id, modalidade_id) VALUES (?, ?)";
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, matricula.getId());
                stmt.setInt(2, modalidade.getId());
                stmt.executeUpdate();
            } catch (SQLException e1) {
                System.err.println("Erro ao prepara modalidades à matrícula: " + e1.getMessage());
                // Check if connection is valid before rollback
            }
        } catch (SQLException e) {
            System.err.println("Erro ao associar modalidades à matrícula: " + e.getMessage());
            // Check if connection is valid before rollback

        }
    }

    @Override
    public Boolean update(Matricula matricula) throws SQLException {
        System.out.println("metodo updte bd matricula");
        String sql = "UPDATE tb_Matricula SET DATAVIGENCIA = ?,DATAVENCIMENTO = ?, DATAINICIO = ?, VALOR = ?, STATUS = ? ,ID_ALUNO = ? WHERE id = ?";
        Optional<Matricula> matricula_bd = this.find(matricula.getId());
        // Verifica se a matrícula foi encontrada
        if (!matricula_bd.isPresent()) {
            System.err.println("Erro: Matrícula ID " + matricula.getId() + " não encontrada no banco.");
            return false;
        }
        matricula_bd.get().setDataDeVigencia(matricula.getDataDeVigencia());
        matricula_bd.get().setDataVecimento(matricula.getDataVecimento());
        matricula_bd.get().setDataInicio(matricula.getDataInicio());
        matricula_bd.get().setValor(matricula.getValor());
        matricula_bd.get().setStatus(matricula.getStatus());
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, new java.sql.Date(matricula_bd.get().getDataDeVigencia().getTime()));
                stmt.setDate(2, new java.sql.Date(matricula_bd.get().getDataVecimento().getTime()));
                stmt.setDate(3, new java.sql.Date(matricula_bd.get().getDataInicio().getTime()));
                stmt.setDouble(4, matricula_bd.get().getValor());
                stmt.setInt(5, matricula_bd.get().getStatus() ? 1 : 0);
                if (matricula_bd.get().getAluno() != null) {
                    stmt.setInt(6, matricula_bd.get().getAluno().getId());
                } else {
                    stmt.setNull(6, java.sql.Types.INTEGER);
                    System.err.println("Aviso: Matrícula ID " + matricula.getId() + " não tem aluno associado.");
                }
                stmt.setInt(7, matricula_bd.get().getId());
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    deleteModalidades(conn, matricula_bd.get());
                    matricula_bd.get().getModalidades().clear();
                    matricula_bd.get().getModalidades().addAll(matricula.getModalidades());
                    updateModalidades(matricula_bd.get());
                    return true;
                } else {
                    System.err.println("Nenhuma linha foi atualizada para a matrícula ID: " + matricula.getId());
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar matrícula: " + e.getMessage());
            return false;
        }
        return null;
    }

    private void deleteModalidades(Connection conn, Matricula matricula) throws SQLException {
        String sql = "DELETE FROM tb_Matriculas_tb_Modalidades WHERE matricula_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, matricula.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deleta modalidades por ID da matrícula: " + e.getMessage());
        }
    }

    @Override
    public Boolean delete(Matricula matricula) throws SQLException {
        String sql = "DELETE FROM tb_Matricula WHERE ID = ?";
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, matricula.getId());
                deleteModalidades(conn, matricula);
                stmt.executeUpdate();
                System.out.println("matricula excluído com sucesso.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir o ,matricula.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    /*public List<Matricula> getAll() throws SQLException {
        List<Matricula> matriculas = new ArrayList<>();
        //List<Modalidade> m= new ArrayList<>();
        // String sql = "SELECT * FROM tb_Matricula";
        String sql = "SELECT * FROM tb_Matricula";
        try (Connection conn = ConexaoSQLiteJDBC.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql);) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("Executando consulta para obter todas as matrículas");
            while (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("ID"));
                matricula.setDataDeVigencia(rs.getDate("DATAVIGENCIA"));
                matricula.setDataVecimento(rs.getDate("DATAVENCIMENTO"));
                matricula.setDataInicio(rs.getDate("DATAINICIO"));
                matricula.setValor(rs.getDouble("VALOR"));
                matricula.setStatus(rs.getBoolean("STATUS"));
                matriculas.add(matricula);
            }
            for (Matricula m : matriculas) {
                m.setAluno(alunoMatricula(conn, m.getId()).get());
                m.setModalidades(getModalidadesByMatriculaId(conn, m.getId()));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter todas as matrículas: " + e.getMessage());
            e.printStackTrace();
        }
        return matriculas;
    }*/

    public List<Matricula> getAll() throws SQLException {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT m.*, a.ID AS aluno_id, a.NOME AS aluno_nome, a.FONE AS aluno_fone, a.DATANASC AS aluno_dataNasc, a.CPF AS aluno_cpf, a.EMAIL AS aluno_email, a.ENDERECO AS aluno_endereco, a.STATUS AS aluno_status "
                + "FROM tb_Matricula m "
                + "LEFT JOIN tb_Aluno a ON m.ID_ALUNO = a.ID";
        try (Connection conn = ConexaoSQLiteJDBC.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            System.out.println("Executando consulta para obter todas as matrículas");
            while (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("ID"));
                matricula.setDataDeVigencia(rs.getDate("DATAVIGENCIA"));
                matricula.setDataVecimento(rs.getDate("DATAVENCIMENTO"));
                matricula.setDataInicio(rs.getDate("DATAINICIO"));
                matricula.setValor(rs.getDouble("VALOR"));
                matricula.setStatus(rs.getBoolean("STATUS"));

                // Populate Aluno object directly
                if (rs.getInt("aluno_id") != 0) { // Check if Aluno exists
                    Aluno aluno = new Aluno();
                    aluno.setId(rs.getInt("aluno_id"));
                    aluno.setNome(rs.getString("aluno_nome"));
                    aluno.setFone(rs.getString("aluno_fone"));
                    aluno.setDataNasc(rs.getDate("aluno_dataNasc"));
                    aluno.setCpf(rs.getString("aluno_cpf"));
                    aluno.setEmail(rs.getString("aluno_email"));
                    aluno.setEndereco(rs.getString("aluno_endereco"));
                    aluno.setStatus(rs.getBoolean("aluno_status"));
                    matricula.setAluno(aluno);
                }
                matricula.setModalidades(getModalidadesByMatriculaId(conn, matricula.getId()));
                matriculas.add(matricula);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter todas as matrículas: " + e.getMessage());
            e.printStackTrace();
        }
        return matriculas;
    }

    public static Optional<Aluno> alunoMatricula(Connection conn, int id) throws SQLException {
        Optional<Aluno> a = Optional.empty();
        String sql = "SELECT a.ID, a.NOME, a.FONE, a.DATANASC, a.CPF, a.EMAIL, a.ENDERECO, a.STATUS"
                + " FROM tb_Aluno a"
                + " WHERE EXISTS (SELECT 1 FROM tb_Matricula  m where m.ID_ALUNO = a.ID and  m.ID = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("ID"));
                aluno.setNome(rs.getString("NOME"));
                aluno.setFone(rs.getString("FONE"));
                aluno.setDataNasc(rs.getDate("DATANASC"));
                aluno.setCpf(rs.getString("CPF"));
                aluno.setEmail(rs.getString("EMAIL"));
                aluno.setEndereco(rs.getString("ENDERECO"));
                aluno.setStatus(rs.getBoolean("STATUS"));
                a = Optional.of(aluno);
                System.out.println("Aluno encontrado: " + aluno);
            } else {
                System.out.println("Aluno não encontrado para ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter aluno por ID da matrícula: " + e.getMessage());
        }
        return a;
    }

    public static List<Modalidade> getModalidadesByMatriculaId(Connection conn, int matriculaId) throws SQLException {
        List<Modalidade> modalidades = new ArrayList<>();
        String sql = "SELECT m.ID,m.NOME,m.VALOR "
                + " FROM tb_Modalidade m"
                + " JOIN tb_Matriculas_tb_Modalidades mm ON m.id = mm.modalidade_id"
                + " WHERE mm.matricula_id = ?";

        try (PreparedStatement stmts = conn.prepareStatement(sql)) {
            stmts.setInt(1, matriculaId);
            try (ResultSet rst = stmts.executeQuery()) {
                while (rst.next()) {
                    Modalidade modalidade = new Modalidade();
                    modalidade.setId(rst.getInt("ID"));
                    modalidade.setNome(rst.getString("NOME"));
                    modalidade.setValor(rst.getDouble("VALOR"));
                    modalidades.add(modalidade);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter modalidades por ID da matrícula: " + e.getMessage());
        }
        return modalidades;
    }

    @Override
    public Optional<Matricula> find(Integer id) throws SQLException {
        Optional<Matricula> m = Optional.empty();
        Matricula matricula = new Matricula();
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            String sql = "SELECT * FROM tb_Matricula WHERE ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        matricula.setId(rs.getInt("id"));
                        matricula.setDataDeVigencia(rs.getDate("DATAVIGENCIA"));
                        matricula.setDataVecimento(rs.getDate("DATAVENCIMENTO"));
                        matricula.setDataInicio(rs.getDate("DATAINICIO"));
                        matricula.setValor(rs.getDouble("VALOR"));
                        matricula.setStatus(rs.getBoolean("STATUS"));

                    } else {
                        return Optional.empty(); // Retorna vazio se a matrícula não existir
                    }
                }
            }
            //matricula.setAluno(alunoMatricula(conn, id).get());
            // Obtendo o aluno da matrícula
            Optional<Aluno> alunoOpt = alunoMatricula(conn, id);
            if (alunoOpt.isPresent()) {
                matricula.setAluno(alunoOpt.get());
            } else {
                System.err.println("Aviso: Nenhum aluno encontrado para a matrícula ID: " + id);
                matricula.setAluno(null);
            }
            matricula.setModalidades(getModalidadesByMatriculaId(conn, id));
            m = Optional.of(matricula);
        } catch (SQLException e) {
            System.err.println("Erro ao obter aluno pelo ID.");
            e.printStackTrace();
        }
        return m;
    }

    public Optional<Matricula> selectMatriculaAlunoId(Integer id) throws SQLException {
        Optional<Matricula> m = Optional.empty();
        Matricula matricula = new Matricula();
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            String sql = "SELECT * FROM tb_Matricula WHERE ID_ALUNO = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        matricula.setId(rs.getInt("id"));
                        matricula.setDataDeVigencia(rs.getDate("DATAVIGENCIA"));
                        matricula.setDataVecimento(rs.getDate("DATAVENCIMENTO"));
                        matricula.setDataInicio(rs.getDate("DATAINICIO"));
                        matricula.setValor(rs.getDouble("VALOR"));
                        matricula.setStatus(rs.getBoolean("STATUS"));
                    }
                }
            }
            matricula.setAluno(alunoMatricula(conn, id).get());
            matricula.setModalidades(getModalidadesByMatriculaId(conn, id));
            m = Optional.of(matricula);
        } catch (SQLException e) {
            System.err.println("Erro ao obter matricula  pelo ID do aluno.");
            e.printStackTrace();
        }
        return m;
    }

    public static List<Matricula> getMatriculasByAlunoNome(String nomeAluno) throws SQLException {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT m.* "
                + "FROM tb_Matricula m "
                + "JOIN tb_Aluno a ON m.ID_ALUNO = a.ID "
                + "WHERE a.NOME LIKE ?"; // Use LIKE for partial matches

        try (Connection conn = ConexaoSQLiteJDBC.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeAluno + "%"); // Add wildcard for partial matching
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setId(rs.getInt("ID"));
                matricula.setDataDeVigencia(rs.getDate("DATAVIGENCIA"));
                matricula.setDataVecimento(rs.getDate("DATAVENCIMENTO"));
                matricula.setDataInicio(rs.getDate("DATAINICIO"));
                matricula.setValor(rs.getDouble("VALOR"));
                matricula.setStatus(rs.getBoolean("STATUS"));
                matriculas.add(matricula);
            }

            for (Matricula m : matriculas) {
                m.setAluno(alunoMatricula(conn, m.getId()).orElse(null)); // Handle potential null
                m.setModalidades(getModalidadesByMatriculaId(conn, m.getId()));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter matrículas pelo nome do aluno: " + e.getMessage());
            e.printStackTrace();
        }
        return matriculas;
    }

}
