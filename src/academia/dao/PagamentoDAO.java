/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package academia.dao;

import academia.interfaces.GenericRepository;

import academia.model.Pagamento;
import academia.sqlite.ConexaoSQLiteJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author POSITIVO
 */
public class PagamentoDAO implements GenericRepository<Pagamento>{
 private Connection conn;

    public PagamentoDAO(Connection conexao) {
        this.conn = conexao;
    }

    @Override
    public Boolean insert(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO tb_Pagamento(DATAPAGAMENTO,VALOR,FORMA) VALUES (?, ?, ?)";
         boolean rst = false;
        if(pagamento.getId() != null){
            // Se o aluno já tiver um ID, atualiza-o
            System.out.println("metodo put");
            rst =  put(pagamento);
            return rst;
        }else {
            System.out.println(pagamento.getId());

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          
            stmt.setDate(1, new java.sql.Date(pagamento.getData_pg().getTime()));
            stmt.setDouble(2,pagamento.getValor_pg());
            stmt.setString(3, pagamento.getForma_pg());
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
    public Boolean put(Pagamento pagamento) throws SQLException {
        String sql = "UPDATE tb_Pagamento SET  = ?, DATAPAGAMENTO = ?, VALOR = ?, FORMA = ? WHERE id = ?";
        Optional<Pagamento> pagamento_bd = this.getById(pagamento.getId());
        pagamento_bd.get().setData_pg(pagamento.getData_pg());
        pagamento_bd.get().setValor_pg(pagamento.getValor_pg());
        pagamento_bd.get().setForma_pg(pagamento.getForma_pg());
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(pagamento.getData_pg().getTime()));
            stmt.setDouble(2,pagamento.getValor_pg());
            stmt.setString(3, pagamento.getForma_pg());
            stmt.setInt(4, pagamento.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pagamento: " + e.getMessage());
            return false;
        }
        
    }

    @Override
    public Boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM tb_Pagamento WHERE ID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

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
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                Date dataPagamento = rs.getDate("DATAPAGAMENTO");
                Double valor = rs.getDouble("VALOR");
                String status  = rs.getString("FORMA");
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
    public Optional getById(Integer id) throws SQLException {
         Optional<Pagamento> pg = Optional.empty();
    try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            String sql = "SELECT * FROM tb_Pagamento WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pagamento pagamento = new Pagamento();
                pagamento.setId(rs.getInt("id"));
       
                pagamento.setData_pg(rs.getDate("data_pg"));
                pagamento.setValor_pg(rs.getDouble("valor_pg"));
                pagamento.setForma_pg(rs.getString("forma_pg"));
                
                pg = Optional.of(pagamento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter aluno pelo ID.");
            e.printStackTrace();
        }
        return pg;
    }

   
    
}
