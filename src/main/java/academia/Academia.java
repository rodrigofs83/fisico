/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package main.java.academia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author POSITIVO
 */
public class Academia extends Application {
     private static Stage stage;//uma janela
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/main/resources/view/FXMLAcademia.fxml"));//carrega FXML        
        Scene scene = new Scene(root);//coloca o FXML em uma sena 
        stage.setTitle("Academia");
        stage.setScene(scene);//coloca a cena em uma janela 
        stage.show();//abre a janela 
        setStage(stage);//seta janela 
    }
    public static Stage getStage(){
        return stage;
    }
    public void setStage(Stage stage){
        Academia.stage = stage;
    }
     public static boolean isStageOpen() {
        return stage != null && stage.isShowing();
    }

    public static void closeStage() {
        if (stage != null && stage.isShowing()) {
            stage.close();
            stage = null;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
