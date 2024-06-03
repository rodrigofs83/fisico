/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package academia.sqlite;

import java.sql.Connection;
import java.sql.Statement;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author POSITIVO
 */
public class ConexaoSQLiteJDBCTest {

    public ConexaoSQLiteJDBCTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of conectar method, of class ConexaoSQLiteJDBC.
     */
    @Test
    public void testConectar() throws Exception {
        System.out.println("conectar");
        Connection conn = ConexaoSQLiteJDBC.getConexao();
        assertNotNull("A conexão deve ser estabelecida", conn);
        fail("The test case is a prototype.");
    }

    /**
     * Test of desconectar method, of class ConexaoSQLiteJDBC.
     */
    @Test
    public void testDesconectar() {
        System.out.println("desconectar");
        ConexaoSQLiteJDBC.desconectar();
        Connection conn = ConexaoSQLiteJDBC.getConexao();
        assertNotNull("A conexão deve ser estabelecida", conn);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getConexao method, of class ConexaoSQLiteJDBC.
     */
    @Test
    public void testGetConexao() {
        System.out.println("getConexao");
        Connection expResult = null;
        Connection result = ConexaoSQLiteJDBC.getConexao();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTables method, of class ConexaoSQLiteJDBC.
     */
    @Test
    public void testCreateTables() throws Exception {
        System.out.println("createTables");
        ConexaoSQLiteJDBC.createTables();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAdmin method, of class ConexaoSQLiteJDBC.
     */
    @Test
    public void testAddAdmin() throws Exception {
        System.out.println("addAdmin");
        Statement statement = null;
        ConexaoSQLiteJDBC.addAdmin(statement);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    private void assertNotNull(String a_conexão_deve_ser_estabelecida, Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
