/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package main.java.academia;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author POSITIVO
 */
public class MatriculaForm extends Application {
     private static Stage stage;//uma janela
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/main/resources/view/FXMLMatricula.fxml"));//carrega FXML        
        Scene scene = new Scene(root);//coloca o FXML em uma sena 
        stage.setTitle("Matricular");
        stage.setScene(scene);//coloca a cena em uma janela 
        stage.show();//abre a janela 
        setStage(stage);//seta janela 
    }
    public static Stage getStage(){
        return stage;
    }
    public void setStage(Stage stage){
        MatriculaForm.stage = stage;
    }
     public static boolean isStageOpen() {
        return stage != null && stage.isShowing();
    }

    public static void closeStage() {
        if (stage != null && stage.isShowing()) {
            stage.close();
            stage = null; // Set the reference to null after closing
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
