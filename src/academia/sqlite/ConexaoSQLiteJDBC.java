/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package academia.sqlite;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author POSITIVO
 */
public class ConexaoSQLiteJDBC {
  private static Connection connection;
    private static final String nomeBancoDados = "jdbc:sqlite:fisico.db";
    private static final Logger logger = Logger.getLogger(ConexaoSQLiteJDBC.class.getName());

    // Método para conectar ao banco de dados
    public static void conectar() throws ClassNotFoundException {
        try {
            // Carregar o driver JDBC do SQLite
            Class.forName("org.sqlite.JDBC");

            // Estabelecer a conexão com o banco de dados
            connection = DriverManager.getConnection(nomeBancoDados);
            connection.createStatement().setQueryTimeout(30);

            // Ativar suporte a chaves estrangeiras
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }

            System.out.println("Conexão com o banco de dados " + nomeBancoDados + " estabelecida com sucesso!");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao conectar ao banco de dados", e);
        }
    }

    // Método para desconectar do banco de dados
    public static void desconectar() {
        if (connection == null) {
            return;
        }
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao fechar conexão");
            logger.log(Level.SEVERE, null, ex);
        }
    }

    // Método para obter a conexão com o banco de dados
    public static Connection getConexao() {
        try {
            if (connection == null || connection.isClosed()) {
                conectar();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Erro ao abrir conexão");
            logger.log(Level.SEVERE, null, ex);
             connection = null; // Certifique-se de que a conexão é nula em caso de erro
        }
        return connection;
    }

    // Método para criar a tabela tb_aluno
   
    public static  void createTables() throws SQLException{
        

        try {
          // Carregar o driver JDBC do SQLite
        String tb_Aluno ="CREATE TABLE IF NOT EXISTS tb_Aluno("+
                "ID INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                "NOME VARCHAR,"+
                "FONE VARCHAR,"+
                "DATANASC INTEGER,"+
                "CPF VARCHAR,"+
                "EMAIL VARCHAR,"+
                "ENDERECO VARCHAR,"+
                "STATUS INTEGER)";
        String tb_Matricula ="CREATE TABLE IF NOT EXISTS tb_Matricula("+
                "ID INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                "DATAVIGENCIA INTEGER,"+
                "DATAVENCIMENTO INTEGER,"+
                "DATAFIM INTEGER,"+
                "VALOR REAL,"+
                "STATUS INTEGER,"+
                "ID_ALUNO INTEGER,"+
                "FOREIGN KEY(ID_ALUNO) REFERENCES tb_Aluno(ID))";
        String tb_Pagamento ="CREATE TABLE IF NOT EXISTS tb_Pagamento("+
                "ID INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                "DATAPAGAMENTO INTEGER,"+
                "VALOR REAL,"+
                "FORMA VARCHAR,"+
                "ID_MATRICULA INTEGER,"+
               "FOREIGN KEY(ID_MATRICULA) REFERENCES tb_Matricula(ID))";
        String tb_Modalidade ="CREATE TABLE IF NOT EXISTS tb_Modalidade("+
                "ID INTEGER PRIMARY KEY  AUTOINCREMENT,"+
                "NOME VARCHAR,"+
                "VALOR REAL)";
       
        String tb_Matriculas_tb_Modalidades = "CREATE TABLE IF NOT EXISTS tb_Matriculas_tb_Modalidades ("
                    + "matricula_id INTEGER NOT NULL, "
                    + "modalidade_id INTEGER NOT NULL, "
                    + "PRIMARY KEY (matricula_id, modalidade_id), "
                    + "FOREIGN KEY (matricula_id) REFERENCES tb_Matricula(ID) ON DELETE CASCADE, "
                    + "FOREIGN KEY (modalidade_id) REFERENCES tb_Modalidade(ID) ON DELETE CASCADE)"; // Fechar parênteses aqui
        Connection conn = getConexao();
        Statement statement = conn.createStatement();
        statement.execute(tb_Aluno);
        statement.execute(tb_Matricula);
        statement.execute(tb_Pagamento);
        statement.execute(tb_Modalidade);
        statement.execute(tb_Matriculas_tb_Modalidades );
        addAdmin(statement);
        
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        } String tb_User ="CREATE TABLE IF NOT EXISTS tb_User(ID INTEGER PRIMARY KEY AUTOINCREMENT ,NOME VACHAR,SENHA VACHAR)";
         String sql = "INSERT INTO tb_User(NOME,SENHA) VALUES ('admin','admin')";
        
        
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




















