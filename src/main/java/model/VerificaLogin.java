


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.model;

import main.java.sqlite.ConexaoSQLiteJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author POSITIVO
 * 
 */

public class VerificaLogin {
    
 
    public VerificaLogin(){
         
        
    }
     public boolean verificarLogin(String usuario, String senha) throws Exception {
        boolean loginSucesso = false;
      
        try(Connection conn = ConexaoSQLiteJDBC.getConexao() ) {
            
            
            String sql = "SELECT * FROM tb_User WHERE nome = ? AND senha = ?";
            PreparedStatement smt;
            smt = conn.prepareStatement(sql);
            smt.setString(1, usuario);  
            smt.setString(2, senha);
            ResultSet rs = smt.executeQuery();
            loginSucesso = rs.next(); // Se houver resultados, o login é bem-sucedido
            
        } catch (SQLException e) {
  
             
            System.err.println("Erro ao verificar o login.");
            e.printStackTrace();
             loginSucesso = false;
        }

        return loginSucesso;
    }
}
