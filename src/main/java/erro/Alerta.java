/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.erro;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
public class Alerta {
        // Method to display an alert box (implementation details may vary)
    public static void showAlertBox(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR); // Use ERROR for informative errors
        alert.setTitle(title);
        alert.setHeaderText(null); // Remove default header
        alert.setContentText(message);
        alert.showAndWait();
}
    public static Boolean  confirmationAlert(String titulo,String headText,String contentText){
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle(titulo);
                confirmationAlert.setHeaderText(headText);
                confirmationAlert.setContentText(contentText);

                // Add buttons and get user response
                ButtonType okButton = new ButtonType("OK");
                ButtonType cancelButton = new ButtonType("Cancelar");
                confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == okButton){
                    return true;
                }else{
                    return false;
                }
    }
}
