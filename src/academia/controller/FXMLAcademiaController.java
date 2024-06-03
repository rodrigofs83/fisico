/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package academia.controller;

import academia.Academia;
import academia.AlunoTable;
import academia.Erros.Alerta;
import academia.Login;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author POSITIVO
 */
public class FXMLAcademiaController implements Initializable {
    Alerta alerta = new Alerta();
     @FXML
    private Button btnMatriculas;

    @FXML
    private Button btnModalidade;

   
    @FXML
    private Label titulo;

    @FXML
    private Button btn_cadastra;

    @FXML
    private Button btn_close;
    //função ao clica botão  fecha 
    @FXML
    private StackPane stackpane;
            
    @FXML
    void closeAction(ActionEvent event) {
        telaClose();
    }
    public void telaClose(){
         if ( Academia.getStage() != null) {
               Academia.getStage().close();
         }
    }

    @FXML
    void cadastraAluno() throws Exception {
         
         // Carregar o nó raiz do arquivo FXML
         
         // Acessar o controller do AlunoForm
         AlunoTable alunoTable = new AlunoTable();
         // Adicionar o nó raiz ao StackPane
          // Adicionar o nó raiz à StackPane
          Stage tableStage = alunoTable.getStage();
          if ( tableStage == null || !tableStage.isShowing())
            alunoTable.start(new Stage()); 
         
    }   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
          btn_cadastra.setOnMouseClicked((MouseEvent e)->{
              try {
                  this.cadastraAluno();
                  titulo.setText("sucesso");
              } catch (Exception ex) {
                 
                  // Optional: Display a more informative alert box
                 alerta.showAlertBox("Erro ao cadastrar aluno",""+ex);
                  Logger.getLogger(FXMLAcademiaController.class.getName()).log(Level.SEVERE, null, ex);
              }
           });
        
    }    
    
}
