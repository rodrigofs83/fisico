/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package main.java.model;
/**
 *
 * @author POSITIVO
 */


import main.java.dao.MatriculaDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author POSITIVO
 */

public class Matricula {
    private Integer id ;
    private Date dataDeVigencia;
    private Date dataVecimento;
    private Date dataInicio;
    private Double valor;
    private Boolean status;
    private Aluno aluno;
    private List<Modalidade> modalidades; // Relacionamento 1:n com Modalidade
    
     // Construtor
    public Matricula() {
      this.modalidades = new ArrayList<>();
    }
    
 
    public void addModalidade(Modalidade modalidade) {
        this.getModalidades().add(modalidade);
    }

    public void removeModalidade(Modalidade modalidade) {
        this.getModalidades().remove(modalidade);
    }
    public List<Modalidade> modalidadeAll(){
       return this.getModalidades();
    }
    private static MatriculaDAO matriculaDao = new MatriculaDAO();
    // ----------------DAO----------------------------------
    public void save() throws SQLException{
        if(getId() !=null && matriculaDao.find(getId()).get()!= null){
           matriculaDao.update(this);
        }else{
            matriculaDao.create(this);
        }
        
    }
    public void delete() throws SQLException{
        if(matriculaDao.find(getId()).get()!= null){
           matriculaDao.delete(this);
        }
    }
    public static List<Matricula> getAll() throws SQLException{
        return matriculaDao.getAll();
    }
    public static Matricula find(int pk) throws SQLException{
        return matriculaDao.find(pk).get();
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
     * @return the dataDeVigencia
     */
    public Date getDataDeVigencia() {
        return dataDeVigencia;
    }

    /**
     * @param dataDeVigencia the dataDeVigencia to set
     */
    public void setDataDeVigencia(Date dataDeVigencia) {
        this.dataDeVigencia = dataDeVigencia;
    }

    /**
     * @return the dataVecimento
     */
    public Date getDataVecimento() {
        return dataVecimento;
    }

    /**
     * @param dataVecimento the dataVecimento to set
     */
    public void setDataVecimento(Date dataVecimento) {
        this.dataVecimento = dataVecimento;
    }

    /**
     * @return the dataFim
     */
    public Date getDataInicio() {
        return dataInicio;
    }

    /**
     * @param dataFim the dataFim to set
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the valor
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
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

    /**
     * @return the aluno
     */
    public Aluno getAluno() {
        return aluno;
    }

    /**
     * @param aluno the aluno to set
     */
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    /**
     * @return the modalidades
     */
    public List<Modalidade> getModalidades() {
        return modalidades;
    }

    /**
     * @param modalidades the modalidades to set
     */
    public void setModalidades(List<Modalidade> modalidades) {
        this.modalidades = modalidades;
    }
   @Override
    public String toString() {
        return "Matricula{"
                +" id "+this.id
                +" Data vigencia "+this.dataDeVigencia
                +" data vencimento "+this.dataVecimento
                +" data fim "+this.dataInicio
                +" valor "+this.valor
                 +" status "+this.status
                +" aluno "+this.aluno.getNome()
                +" modalidades "+ this.modalidades.toString();  
    }
}
