/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package main.java.controller;

import main.java.academia.Academia;
import main.java.academia.AlunoTable;
import main.java.academia.AlunoTable;
import main.java.academia.MatriculaTable;
import main.java.academia.ModalidadeForm;
import main.java.erro.Alerta;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
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
    private Button btnHome;

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
    private Stage curretStage;

    @FXML
    void closeAction(ActionEvent event) {
        telaClose();
    }

    public void telaClose() {
        Academia.closeStage();
    }

    @FXML
    void home(ActionEvent event) throws Exception {
        if (Academia.getStage() != null) {
            Academia.getStage().close();
        }
        Academia a = new Academia();
        a.start(new Stage());
    }

    @FXML
    void cadastraAluno() throws Exception {
        if (curretStage != null) {
            if (curretStage.getTitle().equals("Alunos") && curretStage.isShowing()) {
                curretStage.toFront();
            } else {
                curretStage.toFront();
                curretStage.close();
                curretStage = null;
                AlunoTable alunoTable = new AlunoTable();
                alunoTable.start(new Stage());
                curretStage = AlunoTable.getStage();
            }
        } else {
            AlunoTable alunoTable = new AlunoTable();
            alunoTable.start(new Stage());
            curretStage = AlunoTable.getStage();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btn_cadastra.setOnMouseClicked((MouseEvent e) -> {
            try {
                this.cadastraAluno();

            } catch (Exception ex) {

                // Optional: Display a more informative alert box
                alerta.showAlertBox("Erro ao cadastrar aluno", "" + ex);
            }
        });
        btnModalidade.setOnMouseClicked((MouseEvent e) -> {
            try {
                if (curretStage != null) {
                    if (curretStage.getTitle().equals("Modalidades") && curretStage.isShowing()) {
                        curretStage.toFront();
                        System.out.println("modalidades");
                    } else {
                        curretStage.toFront();
                        curretStage.close();
                        curretStage = null;
                        ModalidadeForm modalidadeForm = new ModalidadeForm();
                        modalidadeForm.start(new Stage());
                        curretStage = ModalidadeForm.getStage();
                    }
                } else {
                    ModalidadeForm modalidadeForm = new ModalidadeForm();
                    modalidadeForm.start(new Stage());
                    curretStage = ModalidadeForm.getStage();
                }

            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(FXMLAcademiaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

        });

        btnMatriculas.setOnMouseClicked((MouseEvent e) -> {
            try {
                if (curretStage != null) {
                    if (curretStage.getTitle().equals("Matriculas") && curretStage.isShowing()) {
                        curretStage.toFront();
                    } else {
                        curretStage.toFront();
                        curretStage.close();
                        curretStage = null;
                        MatriculaTable matriculaTable = new MatriculaTable();
                        matriculaTable.start(new Stage());
                        curretStage = MatriculaTable.getStage();

                    }
                } else {
                    // curretStage.close();
                    MatriculaTable matriculaTable = new MatriculaTable();
                    curretStage = new Stage();
                    matriculaTable.start(curretStage);
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(FXMLAcademiaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        });

    }

}
