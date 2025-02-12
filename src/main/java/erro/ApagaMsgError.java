/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.erro;

/**
 *
 * @author POSITIVO
 */
import javafx.application.Platform;
import javafx.scene.control.Label;
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


@ToString
public class ApagaMsgError {
    
   
    public void apagaMsg(Label msg){
        new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000); // Aguardar 10 segundos
                                Platform.runLater(() -> { // Executar na thread principal do JavaFX
                                    msg.setText(""); // Modificar o texto do label
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }       }
                    }).start();
    }
}
