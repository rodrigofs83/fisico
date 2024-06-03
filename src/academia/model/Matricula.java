/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package academia.model;


/**
 *
 * @author POSITIVO
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 *
 * @author POSITIVO
 */
@Data
@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Matricula {
    private Integer id ;
    private Date dataDeVigencia;
    private Date dataVecimento;
    private Date dataFim;
    private Double valor;
    private Boolean status;
    private Aluno aluno;
    private List<Modalidade> modalidades; // Relacionamento 1:n com Modalidade
    
     // Construtor
    public Matricula() {
      
    }
    
 
    public void addModalidade(Modalidade modalidade) {
        this.modalidades.add(modalidade);
    }

    public void removeModalidade(Modalidade modalidade) {
        this.modalidades.remove(modalidade);
    }
    
}
