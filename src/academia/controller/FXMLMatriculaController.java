/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package academia.controller;

import academia.MatriculaForm;
import academia.dao.ModalidadeDAO;
import academia.model.Aluno;
import academia.model.Matricula;
import academia.model.Modalidade;
import academia.sqlite.ConexaoSQLiteJDBC;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author POSITIVO
 */
public class FXMLMatriculaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btn_add_modalidade;

    @FXML
    private Button btn_close;
   @FXML
   private ChoiceBox<String> choiceBoxModalidade;

    @FXML
    private Button btn_excluir_modalidade;
    @FXML
    private DatePicker data_vigencia;
    @FXML
    private DatePicker data_vencimento;
    @FXML
    private Label label_alunoFone;

    @FXML
    private Label label_alunoId;

    @FXML
    private Label label_alunoNome;
    @FXML
    void cancelarAction(ActionEvent event) {
        this.telaClose();
    }

    @FXML
    void closeAction(ActionEvent event) {
         this.telaClose();
    }
    public void telaClose() {
        if (MatriculaForm.getStage() != null) {
            MatriculaForm.getStage().close();
       }

    }
    @FXML
    public void preencherLabelAluno(Aluno aluno) {
        label_alunoId.setText(Integer.toString(aluno.getId()));
        label_alunoNome.setText(aluno.getNome());
        label_alunoFone.setText(aluno.getFone());
        
    }

    private void preecheDatas(){
        LocalDate dataAtual = LocalDate.now();
         // Adicione um mês à data atual
        LocalDate dataDaquiUmMes = dataAtual.plusMonths(1);
        // Calcule a diferença em dias entre as duas datas
        long diferencaEmDias = ChronoUnit.DAYS.between(dataAtual, dataDaquiUmMes);
        data_vigencia.setValue(dataAtual);
        data_vencimento.setValue(dataDaquiUmMes);
        
    }
   
    
    private Matricula pegaMatricula(){
         LocalDate data_vig = data_vigencia.getValue();
         LocalDate data_venc = data_vencimento.getValue();
         LocalDate datafim = data_venc.plusMonths(1);
         Matricula mat = new Matricula();
         return mat;
   } 
    private List<String> getModalidadeNomes(){
        List<String> nomes = new ArrayList<>();
          try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            ModalidadeDAO modalidadeDAO = new ModalidadeDAO(conn);
            List<Modalidade> modalidades = modalidadeDAO.getAll();
           
            System.out.println("sucesso" + modalidadeList);
            for(Modalidade modalidade : modalidades ){
                nomes.add(modalidade.getNome()); 
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ModalidadeFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
         return nomes;
    }
     @FXML
    private ObservableList<Modalidade> modalidadeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         preecheDatas();
        // TODO
          
          choiceBoxModalidade.getItems().addAll(getModalidadeNomes());
          String modalidadeSelect  = choiceBoxModalidade.getValue();
          
          
    }    
    
}
