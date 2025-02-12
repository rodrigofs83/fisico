/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.model;

/**
 *
 * @author POSITIVO
 */

import main.java.dao.PagamentoDAO;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;



/**
 *
 * @author POSITIVO
 */

public class Pagamento {
    private Integer id;
    private Date data_pg;
    private double valor_pg;
    private String forma_pg;
    private Matricula matricula;
    
    public Pagamento(){}
    
    
    private static PagamentoDAO pagamentoDao = new PagamentoDAO();
    // ----------------DAO----------------------------------
    public void save() throws SQLException{
        if(getId() !=null && pagamentoDao.find(getId()).get()!= null){
           pagamentoDao.update(this);
        }else{
            pagamentoDao.create(this);
        }
        
    }
    public void delete() throws SQLException{
        if(pagamentoDao.find(getId()).get()!= null){
           pagamentoDao.delete(this);
        }
    }
    public static List<Pagamento> getAll() throws SQLException{
        return pagamentoDao.getAll();
    }
    public static Pagamento find(int pk) throws SQLException{
        return (Pagamento) pagamentoDao.find(pk).get();
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the data_pg
     */
    public Date getData_pg() {
        return data_pg;
    }

    /**
     * @param data_pg the data_pg to set
     */
    public void setData_pg(Date data_pg) {
        this.data_pg = data_pg;
    }

    /**
     * @return the valor_pg
     */
    public double getValor_pg() {
        return valor_pg;
    }

    /**
     * @param valor_pg the valor_pg to set
     */
    public void setValor_pg(double valor_pg) {
        this.valor_pg = valor_pg;
    }

    /**
     * @return the forma_pg
     */
    public String getForma_pg() {
        return forma_pg;
    }

    /**
     * @param forma_pg the forma_pg to set
     */
    public void setForma_pg(String forma_pg) {
        this.forma_pg = forma_pg;
    }

    /**
     * @return the matricula
     */
    public Matricula getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }
}
