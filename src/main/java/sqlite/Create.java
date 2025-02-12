/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.sqlite;


import static main.java.sqlite.ConexaoSQLiteJDBC.getConexao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author POSITIVO
 */
public class Create {
    // Método para criar a tabela tb_aluno
   
    public static  void createTables() throws SQLException{
        

        try {
          // Carregar o driver JDBC do SQLite
        String tb_Aluno ="CREATE TABLE IF NOT EXISTS tb_Aluno("+
                "ID INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                "NOME VARCHAR,"+
                "FONE VARCHAR UNIQUE,"+
                "DATANASC INTEGER,"+
                "CPF VARCHAR UNIQUE,"+
                "EMAIL VARCHAR UNIQUE,"+
                "ENDERECO VARCHAR,"+
                "STATUS INTEGER)";
        String tb_Matricula ="CREATE TABLE IF NOT EXISTS tb_Matricula("+
                "ID INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                "DATAVIGENCIA INTEGER,"+
                "DATAVENCIMENTO INTEGER,"+
                "DATAINICIO INTEGER,"+
                "VALOR REAL,"+
                "STATUS INTEGER,"+
                "ID_ALUNO INTEGER,"+
                "FOREIGN KEY(ID_ALUNO) REFERENCES tb_Aluno(ID) ON DELETE CASCADE)";
        String tb_Pagamento ="CREATE TABLE IF NOT EXISTS tb_Pagamento("+
                "ID INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                "DATAPAGAMENTO INTEGER,"+
                "VALOR REAL,"+
                "FORMA VARCHAR,"+
                "ID_MATRICULA INTEGER,"+
               "FOREIGN KEY(ID_MATRICULA) REFERENCES tb_Matricula(ID))";
        String tb_Modalidade ="CREATE TABLE IF NOT EXISTS tb_Modalidade("+
                "ID INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                "NOME VARCHAR UNIQUE,"+
                "VALOR REAL)";
       
        String tb_Matriculas_tb_Modalidades = "CREATE TABLE IF NOT EXISTS tb_Matriculas_tb_Modalidades ("
                    + "matricula_id INTEGER NOT NULL,"
                    + "modalidade_id INTEGER NOT NULL,"
                    + "PRIMARY KEY (matricula_id, modalidade_id),"
                    + "FOREIGN KEY (matricula_id) REFERENCES tb_Matricula(ID) ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "FOREIGN KEY (modalidade_id) REFERENCES tb_Modalidade(ID) ON DELETE CASCADE ON UPDATE CASCADE)"; // Fechar parênteses aqui
       
         String TriggerUpdateMatriculaValor = 
            "CREATE TRIGGER IF NOT EXISTS update_matricula_valor " +
            "AFTER UPDATE ON tb_Modalidade " +
            "FOR EACH ROW " +
            "BEGIN " +
            "  UPDATE tb_Matricula " +
            "  SET valor = (" +
            "    SELECT IFNULL(SUM(m.valor), 0) " +
            "    FROM tb_Modalidade m " +
            "    INNER JOIN tb_Matriculas_tb_Modalidades mtm ON m.id = mtm.modalidade_id" +
            "    WHERE mtm.matricula_id = tb_Matricula.ID " +
            "  ); "+
            "END;";
        String TriggerDeleteMatriculaValor = 
            "CREATE TRIGGER IF NOT EXISTS delete_matricula_valor " +
            "AFTER DELETE ON tb_Modalidade " +
            "FOR EACH ROW " +
            "BEGIN " +
            "  UPDATE tb_Matricula " +
            "  SET valor = (" +
            "    SELECT IFNULL(SUM(m.valor), 0) " +
            "    FROM tb_Modalidade m " +
            "    INNER JOIN tb_Matriculas_tb_Modalidades mtm ON m.id = mtm.modalidade_id" +
            "    WHERE mtm.matricula_id = tb_Matricula.ID " +
            "  ); "+

            "END;";
        Connection conn = getConexao();
        Statement statement = conn.createStatement();
        statement.execute(tb_Aluno);
        statement.execute(tb_Matricula);
        statement.execute(tb_Pagamento);
        statement.execute(tb_Modalidade);
        statement.execute(tb_Matriculas_tb_Modalidades );
        statement.executeUpdate(TriggerUpdateMatriculaValor);
        statement.executeUpdate(TriggerDeleteMatriculaValor);
        addAdmin(statement);
        

        
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
       
        
        
    }
       // Verificar se a tabela tb_User está vazia adicionar so um admin
    public  static void  addAdmin(Statement statement) throws SQLException{
        String tb_User ="CREATE TABLE IF NOT EXISTS tb_User(ID INTEGER PRIMARY KEY AUTOINCREMENT ,NOME VACHAR,SENHA VACHAR)";
        statement.execute(tb_User);
        String sql = "INSERT INTO tb_User(NOME,SENHA) VALUES ('admin','admin')";
        String countSql = "SELECT COUNT(*) FROM tb_User";
        ResultSet resultSet = statement.executeQuery(countSql);
        int count = resultSet.getInt(1);
        resultSet.close();

        // Se a tabela estiver vazia, inserir o dado
        if (count == 0) 
            statement.execute(sql);
       
            
        
        
    }
}
