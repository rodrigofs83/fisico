/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.model;

import main.java.dao.AlunoDAO;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author POSITIVO
 */
public class Aluno {

    private Integer id;
    private String nome;
    private String fone;
    private Date dataNasc;
    private String cpf;
    private String email;
    private String endereco;
    private Boolean status;

    public Aluno() {
    }

    public Aluno(String nome, String fone, Date dataNasc, String cpf, String email, String endereco, Boolean status) {
        this.nome = nome;
        this.fone = fone;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
        this.status = status;

    }

    public Aluno(Integer id, String nome, String fone, Date dataNasc, String cpf, String email, String endereco, Boolean status) {
        this.id = id;
        this.nome = nome;
        this.fone = fone;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
        this.status = status;

    }
    ;
    private static AlunoDAO alunoDao = new AlunoDAO();

    // ----------------DAO----------------------------------
    public void save() throws SQLException {
            if (getId() != null && alunoDao.find(getId()).get() != null) {
                alunoDao.update(this);
            } else {
                alunoDao.create(this);
            }
            
     
    }

    public void delete() throws SQLException {
        if (alunoDao.find(getId()).get() != null) {
            alunoDao.delete(this);
        }
    }

    public static List<Aluno> getAll() throws SQLException {
        return alunoDao.getAll();
    }

    public static Aluno find(int pk) throws SQLException {
        return alunoDao.find(pk).get();
    }
    public static boolean verificaDuplicidadesFone(Aluno aluno) throws SQLException{
        boolean rst = false ;
       if(alunoDao.duplicatephone(aluno)){
           rst = true;
       }
       return rst; 
    }
      public static boolean verificaDuplicidadesCpf(Aluno aluno) throws SQLException{
        boolean rst = false ;
       if(alunoDao.duplicateCpf(aluno)){
           rst = true;
       }
       return rst;
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
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the fone
     */
    public String getFone() {
        return fone;
    }

    /**
     * @param fone the fone to set
     */
    public void setFone(String fone) {
        this.fone = fone;
    }

    /**
     * @return the dataNasc
     */
    public Date getDataNasc() {
        return dataNasc;
    }

    /**
     * @param dataNasc the dataNasc to set
     */
    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Aluno{"
                + "id = " + this.id
                +"nome "+this.nome  
                +"fone "+this.fone +
                "data "+this.dataNasc+ 
                "email "+this.email+ 
                "endereço "+this.endereco+ 
                "status "+this.status+"}";        
    }

}
