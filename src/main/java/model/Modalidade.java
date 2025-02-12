/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.model;

/**
 *
 * @author POSITIVO
 */
import main.java.dao.ModalidadeDAO;
import java.sql.SQLException;
import java.util.List;

public class Modalidade {

    private Integer id;
    private String nome;
    private double valor = 0.0;

    public Modalidade() {
    }

    public Modalidade(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public Modalidade(Integer id, String nome, double valor) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Modalidade{"
                + "id" + this.id
                + "nome"
                + this.nome
                + "valor"
                + this.valor + "}";
    }
    private static ModalidadeDAO modalidadeDao = new ModalidadeDAO();

    // ----------------DAO----------------------------------
    public void save() throws SQLException {
        if (getId() != null && modalidadeDao.find(getId()).get() != null) {
            modalidadeDao.update(this);
        } else {
            modalidadeDao.create(this);
        }

    }

    public void delete() throws SQLException {
        if (modalidadeDao.find(getId()).get() != null) {
            modalidadeDao.delete(this);
        }
    }

    public static List<Modalidade> getAll() throws SQLException {
        return modalidadeDao.getAll();
    }

    public static Modalidade find(int pk) throws SQLException {
        return (Modalidade) modalidadeDao.find(pk).get();
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
     * @return the valor
     */
    public double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

}
