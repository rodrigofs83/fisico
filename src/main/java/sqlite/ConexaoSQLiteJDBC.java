/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
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
    public static void conectar()  throws SQLException, ClassNotFoundException {
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

}
