/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.dao;

import main.java.interfaces.GenericRepository;
import main.java.model.Pagamento;
import main.java.sqlite.ConexaoSQLiteJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static main.java.dao.MatriculaDAO.alunoMatricula;
import static main.java.dao.MatriculaDAO.getModalidadesByMatriculaId;
import main.java.model.Aluno;
import main.java.model.Matricula;

/**
 *
 * @author POSITIVO
 */
public class PagamentoDAO implements GenericRepository<Pagamento> {

    public PagamentoDAO() {

    }

    @Override
    public Boolean create(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO tb_Pagamento(DATAPAGAMENTO,VALOR,FORMA,ID_MATRICULA) VALUES (?, ?, ?, ?)";
        boolean rst = false;
        if (pagamento.getId() != null) {
            // Se o aluno já tiver um ID, atualiza-o
            System.out.println("metodo put");
            rst = update(pagamento);
            return rst;
        } else {
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDate(1, new java.sql.Date(pagamento.getDataPg().getTime()));
                stmt.setDouble(2, pagamento.getValorPg());
                stmt.setString(3, pagamento.getFormaPg());
                stmt.setInt(4, pagamento.getMatricula().getId());
                stmt.executeUpdate();
                System.out.println("mensalidade paga  com sucesso.");
                rst = true;
            } catch (SQLException e) {
                System.err.println("Erro ao paga ." + e);
                rst = false;
            }

            return rst;
        }
    }

    @Override
    public Boolean update(Pagamento pagamento) throws SQLException {
        String sql = "UPDATE tb_Pagamento SET DATAPAGAMENTO = ?, VALOR = ?, FORMA = ?, ID_MATRICULA = ? WHERE id = ?";
        Optional<Pagamento> pagamento_bd = this.find(pagamento.getId());
        pagamento_bd.get().setDataPg(pagamento.getDataPg());
        pagamento_bd.get().setValorPg(pagamento.getValorPg());
        pagamento_bd.get().setFormaPg(pagamento.getFormaPg());
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(pagamento.getDataPg().getTime()));
            stmt.setDouble(2, pagamento.getValorPg());
            stmt.setString(3, pagamento.getFormaPg());
            stmt.setInt(4, pagamento.getMatricula().getId());
            stmt.setInt(5, pagamento.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pagamento: " + e.getMessage());
            return false;
        }

    }

    @Override
    public Boolean delete(Pagamento p) throws SQLException {
        String sql = "DELETE FROM tb_Pagamento WHERE ID = ?";

        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p.getId());

            stmt.executeUpdate();

            System.out.println("pagamento excluído com sucesso.");
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o ,pagamento.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pagamento> getAll() throws SQLException {
        List<Pagamento> pagamentos = new ArrayList<>();
        String sql = "SELECT p.*,m.ID AS matricula_id," +
        "m.DATAVIGENCIA AS matricula_dataVigencia," +
        "m.DATAVENCIMENTO AS matricula_dataVencimento," +
        "m.DATAINICIO AS matricula_dataInicio," +
        "m.VALOR AS matricula_valor," +
        "m.STATUS AS matricula_status," +
        "a.ID AS aluno_id," +
        "a.NOME AS aluno_nome," +
        "a.FONE AS aluno_fone," +
        "a.DATANASC AS aluno_dataNasc," +
        "a.CPF AS aluno_cpf," +
        "a.EMAIL AS aluno_email, " +
        "a.ENDERECO AS aluno_endereco," +
        "a.STATUS AS aluno_status "
                + "FROM tb_Pagamento p "
                + "JOIN tb_Matricula m ON p.ID_MATRICULA = m.ID "
                + "LEFT JOIN tb_Aluno a ON m.ID_ALUNO = a.ID"; // Use LEFT JOIN in case Aluno is null

        try (Connection conn = ConexaoSQLiteJDBC.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pagamento pagamento = new Pagamento();
                pagamento.setId(rs.getInt("ID"));
                pagamento.setDataPg(rs.getDate("DATAPAGAMENTO"));
                pagamento.setValorPg(rs.getDouble("VALOR"));
                pagamento.setFormaPg(rs.getString("FORMA"));

            Matricula matricula = new Matricula();
            matricula.setId(rs.getInt("matricula_id"));
            matricula.setDataDeVigencia(rs.getDate("matricula_dataVigencia"));
            matricula.setDataVecimento(rs.getDate("matricula_dataVencimento"));
            matricula.setDataInicio(rs.getDate("matricula_dataInicio"));
            matricula.setValor(rs.getDouble("matricula_valor"));
            matricula.setStatus(rs.getBoolean("matricula_status"));

            int alunoId = rs.getInt("aluno_id");
            if (alunoId != 0) {
                Aluno aluno = new Aluno();
                aluno.setId(alunoId);
                aluno.setNome(rs.getString("aluno_nome"));
                aluno.setFone(rs.getString("aluno_fone"));
                aluno.setDataNasc(rs.getDate("aluno_dataNasc"));
                aluno.setCpf(rs.getString("aluno_cpf"));
                aluno.setEmail(rs.getString("aluno_email"));
                aluno.setEndereco(rs.getString("aluno_endereco"));
                aluno.setStatus(rs.getBoolean("aluno_status"));
                matricula.setAluno(aluno);
            }

                pagamento.setMatricula(matricula);
                pagamentos.add(pagamento);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter todos os pagamentos.");
            e.printStackTrace();
        }
        return pagamentos;
    }

//    @Override
//    public List getAll() throws SQLException {
//        List<Pagamento> pagamentos = new ArrayList<>();
//        String sql = "SELECT * FROM tb_Pagamento";
//        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//
//                Pagamento pagamento = new Pagamento();
//                pagamento.setId(rs.getInt("ID"));
//                pagamento.setDataPg(rs.getDate("DATAPAGAMENTO"));
//                pagamento.setValorPg(rs.getDouble("VALOR"));
//                pagamento.setFormaPg(rs.getString("FORMA"));
//                //DATAVIGENCIA ,DATAVENCIMENTO I,DATAFIM,VALOR REAL,STATUS INTEGER
//                pagamentos.add(pagamento);
//
//            }
//            for (Pagamento p : pagamentos) {
//                p.setMatricula(pagamentoMatricula(conn, p.getId()).get());
//            }
//
//        } catch (SQLException e) {
//            System.err.println("Erro ao obter todos os alunos.");
//            e.printStackTrace();
//        }
//
//        return pagamentos;
//    }
    @Override
    public Optional find(Integer id) throws SQLException {
        Optional<Pagamento> pg = Optional.empty();
        Pagamento pagamento = new Pagamento();
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            String sql = "SELECT * FROM tb_Pagamento WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                pagamento.setId(rs.getInt("id"));
                pagamento.setDataPg(rs.getDate("data_pg"));
                pagamento.setValorPg(rs.getDouble("valor_pg"));
                pagamento.setFormaPg(rs.getString("forma_pg"));
            }
            pagamento.setMatricula(pagamentoMatricula(conn, id).get());
            pg = Optional.of(pagamento);
        } catch (SQLException e) {
            System.err.println("Erro ao obter aluno pelo ID.");
            e.printStackTrace();
        }
        return pg;
    }

    public static Optional<Matricula> pagamentoMatricula(Connection conn, int id) throws SQLException {
        Optional<Matricula> m = Optional.empty();

        String sql = "SELECT m.ID,m.DATAVIGENCIA,m.DATAVENCIMENTO,m.DATAINICIO,m.VALOR,m.STATUS,m.ID_ALUNO "
                + "FROM tb_Matricula m "
                + "WHERE EXISTS (SELECT 1 FROM tb_Pagamento p WHERE p.ID_MATRICULA = m.ID AND p.ID = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Matricula matricula = new Matricula();
                    matricula.setId(rs.getInt("id"));
                    matricula.setDataDeVigencia(rs.getDate("DATAVIGENCIA"));
                    matricula.setDataVecimento(rs.getDate("DATAVENCIMENTO"));
                    matricula.setDataInicio(rs.getDate("DATAINICIO"));
                    matricula.setValor(rs.getDouble("VALOR"));
                    matricula.setStatus(rs.getBoolean("STATUS"));
                    m = Optional.of(matricula);
                } else {
                    System.out.println("Matricula não encontrado para ID: " + id);
                }

            } catch (SQLException e) {
                System.err.println("Erro ao obter matricula pelo  ID.");
                e.printStackTrace();
            }
            return m;
        }
    }

}
