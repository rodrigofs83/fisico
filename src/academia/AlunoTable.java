/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package academia;

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
public class AlunoTable extends Application {
    
    private static Stage stage;//uma janela
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/academia/View/FXMLAlunoTable.fxml"));//carrega FXML        
        Scene scene = new Scene(root);//coloca o FXML em uma sena 
        stage.setTitle("Alunos");
         // Carregar o arquivo CSS
      
        stage.setScene(scene);//coloca a cena em uma janela 
        stage.show();//abre a janela 
        setStage(stage);//seta janela 
    }
    public static Stage getStage(){
        return stage;
    }
    public void setStage(Stage stage){
        
        AlunoTable.stage = stage;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
