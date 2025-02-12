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
            System.out.println(pagamento.getId());

            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDate(1, new java.sql.Date(pagamento.getData_pg().getTime()));
                stmt.setDouble(2, pagamento.getValor_pg());
                stmt.setString(3, pagamento.getForma_pg());
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
        String sql = "UPDATE tb_Pagamento SET  = ?, DATAPAGAMENTO = ?, VALOR = ?, FORMA = ?, ID_MATRICULA = ? WHERE id = ?";
        Optional<Pagamento> pagamento_bd = this.find(pagamento.getId());
        pagamento_bd.get().setData_pg(pagamento.getData_pg());
        pagamento_bd.get().setValor_pg(pagamento.getValor_pg());
        pagamento_bd.get().setForma_pg(pagamento.getForma_pg());
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(pagamento.getData_pg().getTime()));
            stmt.setDouble(2, pagamento.getValor_pg());
            stmt.setString(3, pagamento.getForma_pg());
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
    public List getAll() throws SQLException {
        List<Pagamento> pagamentos = new ArrayList<>();
        String sql = "SELECT * FROM tb_Pagamento";
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                Date dataPagamento = rs.getDate("DATAPAGAMENTO");
                Double valor = rs.getDouble("VALOR");
                String status = rs.getString("FORMA");
                //DATAVIGENCIA ,DATAVENCIMENTO I,DATAFIM,VALOR REAL,STATUS INTEGER
                Pagamento pagamento = new Pagamento();
                pagamentos.add(pagamento);

            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter todos os alunos.");
            e.printStackTrace();
        }

        return pagamentos;
    }

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
                pagamento.setData_pg(rs.getDate("data_pg"));
                pagamento.setValor_pg(rs.getDouble("valor_pg"));
                pagamento.setForma_pg(rs.getString("forma_pg"));
            }
            pagamento.setMatricula(pagamentoMatricula(conn,id).get());
            pg = Optional.of(pagamento);
        } catch (SQLException e) {
            System.err.println("Erro ao obter aluno pelo ID.");
            e.printStackTrace();
        }
        return pg;
    }

    public static Optional<Matricula> pagamentoMatricula(Connection conn, int id) throws SQLException {
        Optional<Matricula> m = Optional.empty();
        Matricula matricula = new Matricula();
        String sql = "select m.ID,m.DATAVIGENCIA,m.DATAVENCIMENTO,m.DATAINICIO,m.VALOR,m.STATUS,m.ID_ALUNO"
                + "FROM tb_Matricula m"
                + "WHERE EXISTS (SELECT 1 FROM tb_Pagamento  p where p.ID_MATRICULA = m.ID and  p.ID = 1)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
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
            matricula.setAluno(alunoMatricula(conn, id).get());
            matricula.setModalidades(getModalidadesByMatriculaId(conn, id));
            m = Optional.of(matricula);
        } catch (SQLException e) {
            System.err.println("Erro ao obter matricula pelo  ID.");
            e.printStackTrace();
        }
        return m;
    }

}
