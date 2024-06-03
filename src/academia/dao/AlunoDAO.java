/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package academia.dao;

/**
 *
 * @author POSITIVO
 */
import academia.interfaces.GenericRepository;

import academia.model.Aluno;

import academia.sqlite.ConexaoSQLiteJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.Connection;
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
    public List getAll() throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM tb_Aluno";

        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                 Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("ID"));
                aluno.setNome( rs.getString("NOME"));
                aluno.setFone(rs.getString("FONE"));
                aluno.setDataNasc(rs.getDate("DATANASC"));
                aluno.setCpf(rs.getString("CPF"));
                aluno.setEmail(rs.getString("EMAIL"));
                aluno.setEndereco(rs.getString("ENDERECO"));
                aluno.setStatus(rs.getBoolean("STATUS"));
                alunos.add(aluno);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter todos os alunos.");
            e.printStackTrace();
        }

        return alunos;

    }

    @Override
    public Optional<Aluno> getById(Integer id) throws SQLException {
        Optional<Aluno> a = Optional.empty();
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            String sql = "SELECT * FROM tb_Aluno WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("ID"));
                aluno.setNome( rs.getString("NOME"));
                aluno.setFone(rs.getString("FONE"));
                aluno.setDataNasc(rs.getDate("DATANASC"));
                aluno.setCpf(rs.getString("CPF"));
                aluno.setEmail(rs.getString("EMAIL"));
                aluno.setEndereco(rs.getString("ENDERECO"));
                aluno.setStatus(rs.getBoolean("STATUS"));
                a = Optional.of(aluno);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter aluno pelo ID.");
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public Boolean put(Aluno aluno) throws SQLException {
        String sql = "UPDATE tb_Aluno SET NOME = ?, FONE = ?, DATANASC = ?, CPF = ?, EMAIL = ?, ENDERECO = ?, STATUS = ? WHERE ID = ?";
        Optional<Aluno> aluno_bd = this.getById(aluno.getId());
        aluno_bd.get().setNome(aluno.getNome());
        aluno_bd.get().setFone(aluno.getFone());
        aluno_bd.get().setDataNasc(aluno.getDataNasc());
        aluno_bd.get().setCpf(aluno.getCpf());
        aluno_bd.get().setEmail(aluno.getEmail());
        aluno_bd.get().setEndereco(aluno.getEndereco());
        boolean result = false;
        if (aluno_bd.isPresent()) {
            
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, aluno_bd.get().getNome());
                stmt.setString(2, aluno_bd.get().getFone());
                stmt.setDate(3, new java.sql.Date(aluno_bd.get().getDataNasc().getTime()));
                stmt.setString(4,aluno_bd.get().getCpf());
                stmt.setString(5,aluno_bd.get().getEmail());
                stmt.setString(6,aluno_bd.get().getEndereco());
                stmt.setInt(7,aluno_bd.get().getStatus() ? 1 : 0);
                stmt.setInt(8, aluno_bd.get().getId());
                
                stmt.executeUpdate();
                  int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Aluno atualizado com sucesso."+aluno);
                 result = true;
            } else {
                System.out.println("Nenhum aluno foi atualizado.");
                return result = false ;
            }
            } catch (SQLException e) {
                System.err.println("Erro ao atualizar o aluno.");
                e.printStackTrace();
            }
        }else{
             result =  false ;
        }
        return result;
    }
    

    @Override
    public Boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM tb_Aluno WHERE ID = ?";
    
        try (Connection conn = ConexaoSQLiteJDBC.getConexao() ) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.executeUpdate();

            System.out.println("Aluno excluído com sucesso.");
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o aluno.");
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Boolean insert(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO tb_Aluno (NOME, FONE, DATANASC, CPF, EMAIL, ENDERECO,STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean rst = false;
        if (aluno.getId() != null) {
            // Se o aluno já tiver um ID, atualiza-o
            System.out.println("metodo put");
            rst =  put(aluno);
            return rst;
        }else {
            System.out.println(aluno.getId());

      try (Connection conn = ConexaoSQLiteJDBC.getConexao() ) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getFone());
            stmt.setDate(3, new java.sql.Date(aluno.getDataNasc().getTime()));
            stmt.setString(4,aluno.getCpf());
            stmt.setString(5,aluno.getEmail());
            stmt.setString(6,aluno.getEndereco());
            stmt.setInt(7,aluno.getStatus() ? 1 : 0);
            stmt.executeUpdate();
            System.out.println("Aluno adicionado com sucesso.");
            rst = true;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar o aluno." + e);
            rst = false;
        }

        return rst;
    }
    }

   
}
