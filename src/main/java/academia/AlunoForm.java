/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.academia;


import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author POSITIVO
 */
public class AlunoForm extends Application {

    private static Stage stage;//uma janela
    private static FXMLLoader loader;
    
   
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/FXMLAlunoForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);//coloca o FXML em uma sena 
        stage.setScene(scene);//coloca a cena em uma janela 
        setStage(stage);//seta janela 
        stage.show();//abre a janela 

    }

    public static Stage getStage() {
        return stage;
    }
    
    public void setStage(Stage stage) {
        AlunoForm.stage = stage;
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
