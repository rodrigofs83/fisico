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
    private Date dataPg;
    private double valorPg;
    private String formaPg;
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
     * @return the dataPpg
     */
    public Date getDataPg() {
        return dataPg;
    }

    /**
     * @param data_pg the data_pg to set
     */
    public void setDataPg(Date dataPg) {
        this.dataPg = dataPg;
    }

    /**
     * @return the valorPg
     */
    public double getValorPg() {
        return valorPg;
    }

    /**
     * @param valorPg the valor_pg to set
     */
    public void setValorPg(double valor) {
        this.valorPg = valor;
    }

    /**
     * @return the forma_pg
     */
    public String getFormaPg() {
        return formaPg;
    }

    /**
     * @param forma_pg the forma_pg to set
     */
    public void setFormaPg(String formaPg) {
        this.formaPg = formaPg;
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
