/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package main.java.model;

/**
 *
 * @author POSITIVO
 */
public enum FormaPag {
    PIX("pix"),
    CARTAO("cartao"),
    DINHEIRO("dinheiro");
    private final String tipo;
    FormaPag(String tipo){
     this.tipo = tipo;
    }
    public String getTipo(){
    return tipo;  
    }
}