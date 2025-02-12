/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.erro;

/**
 *
 * @author POSITIVO
 */
public class AlunoDAOException extends  Exception{
    public AlunoDAOException(String message) {   
        super(message);
    }
   
    public AlunoDAOException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
