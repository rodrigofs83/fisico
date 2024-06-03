/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package academia.dao;

import academia.interfaces.GenericRepository;
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
public class ModalidadeDAO implements GenericRepository<Modalidade> {
    private Connection conn;

    public ModalidadeDAO(Connection conexao) {
        this.conn = conexao;
    }
    @Override
    public Boolean insert(Modalidade modalidade) throws SQLException {
         String sql = "INSERT INTO tb_Modalidade(NOME,VALOR) VALUES (?,?)";
         boolean rst = false;
        if(modalidade.getId() != null){
            // Se o aluno já tiver um ID, atualiza-o
            System.out.println("metodo put");
            rst =  put(modalidade);
            return rst;
        }else {
            System.out.println(modalidade.getId());

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          
            stmt.setString(1,modalidade.getNome());
            stmt.setDouble(2,modalidade.getValor());
            stmt.executeUpdate();
            System.out.println("modalidade  inseria sucesso.");
            rst = true;
        } catch (SQLException e) {
            System.err.println("Erro ao adiciona modalidade ." + e);
            rst = false;
        }

        return rst;
    }
    }

    @Override
    public Boolean put(Modalidade modalidade) throws SQLException {
        String sql = "UPDATE tb_Modalidade SET  NOME = ?, VALOR = ? WHERE id = ?";
        Optional<Modalidade> modalidade_bd = this.getById(modalidade.getId());
        modalidade_bd.get().setNome(modalidade.getNome());
        modalidade_bd.get().setValor(modalidade.getValor());
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, modalidade.getNome());
            stmt.setDouble(2,modalidade.getValor());
            stmt.setInt(3, modalidade.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar modalidade: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(Integer id) throws SQLException {
         String sql = "DELETE FROM tb_Modalidade WHERE ID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            stmt.executeUpdate();

            System.out.println("modalidade excluído com sucesso.");
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o ,modalidade.");
            e.printStackTrace();
        }
        return null;
    }
    

    @Override
    public List getAll() throws SQLException {
             List<Modalidade> modalidades = new ArrayList<>();
        String sql = "SELECT * FROM tb_Modalidade";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String nome  = rs.getString("NOME");
                Double valor = rs.getDouble("VALOR");
                
                Modalidade modalidade = new Modalidade(id,nome,valor);
                modalidades.add(modalidade);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter todas as modalidades.");
            e.printStackTrace();
        }
        return modalidades;
    }


    @Override
    public Optional getById(Integer id) throws SQLException {
            Optional<Modalidade> moda = Optional.empty();
    try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            String sql = "SELECT * FROM tb_Modalidade WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Modalidade modalidade = new Modalidade();
                modalidade.setId(rs.getInt("id"));
                modalidade.setNome(rs.getString("nome"));
                modalidade.setValor(rs.getDouble("valor"));
                moda = Optional.of(modalidade);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter modalidade pelo ID.");
            e.printStackTrace();
        }
        return moda;
    }

    
    
}
