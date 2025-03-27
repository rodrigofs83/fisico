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
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import main.java.dao.AlunoDAO;
import main.java.model.Aluno;
import main.java.model.Matricula;
import main.java.util.DateSystem;

/**
 *
 * @author POSITIVO
 */
public class FXMLAcademiaController implements Initializable, Observer {

    Alerta alerta = new Alerta();
    @FXML
    private Button btnMesalidadeEmdia;

    @FXML
    private Button btnModalidade;

    @FXML
    private Button btnTotalAlunos;

    @FXML
    private Button btnVencidas;

    @FXML
    private Button btnVencidoHoje;

    @FXML
    private Label labelQtdEmdia;

    @FXML
    private Label labelTotalAlunos;

    @FXML
    private Label labelValorVencidas;

    @FXML
    private Label labelVencendoHoje;

    @FXML
    private Label labelVencidas;

    @FXML
    private Label labelValorEmDia;

    @FXML
    private Label labelValorVencendoHoje;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnMatriculas;

    @FXML
    private Label labelAlunosAtivos;

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
    private static FXMLAcademiaController instance;

    public FXMLAcademiaController() {
        instance = this;
    }

    public static FXMLAcademiaController getInstance() {
        return instance;
    }

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
            if (curretStage != null) {
                curretStage.toFront();
                curretStage.close();
            }
            Academia.getStage().close();
            Academia a = new Academia();
            a.start(new Stage());
        } else {
            Academia a = new Academia();
            a.start(new Stage());
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("AlunoTableController  Método update chamado!");
        if (arg instanceof Boolean && (Boolean) arg) {
            Platform.runLater(() -> {
                atualizarLabelTotalAlunos();
                atualizarTotalAlunosAtivos();
                atualizarTotalMatriculaEmDia();

            });
        }
    }

    private void registerObserver(FXMLAlunoTableController instance) {
        instance.addObserver(this);
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
            FXMLAlunoTableController alunoTableController = FXMLAlunoTableController.getInstance();
            if (alunoTableController != null) {
                this.registerObserver(alunoTableController);

            } else {
                System.err.println("Instância de FXMLAlunoFormController é nula.");
            }
        }

    }

    private ObservableList<Aluno> listAlunos = FXCollections.observableArrayList();

    private void atualizarLabelTotalAlunos() {
        try {
            List<Aluno> alunos = Aluno.getAll();
            listAlunos.setAll(alunos); // Atualiza ObservableList
            labelTotalAlunos.setText(String.valueOf(alunos.size()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarTotalAlunosAtivos() {
        try {
            List<Aluno> alunos = Aluno.getAll();
            long totalAtivos = alunos.stream()
                    .filter(Aluno::getStatus) // status == true
                    .count();

            labelAlunosAtivos.setText(String.valueOf(totalAtivos));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarTotalMatriculaEmDia() {
        try {
            int qtdEmDia = 0;
            List<Matricula> matriculas = Matricula.getAll();
            long totalMatriculaEmDia = matriculas.stream()
                    .filter(m -> {
                        if (m.getDataVecimento() == null) {
                            return false;
                        }
                        return !DateSystem.isVencido(DateSystem.converteDateParalocalDate(DateSystem.convertSQLDateToDate(m.getDataVecimento())));
                    }) // status == true
                    .count();
            labelQtdEmdia.setText(String.valueOf(totalMatriculaEmDia));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelTotalAlunos.getText();
        this.atualizarLabelTotalAlunos();
        this.atualizarTotalAlunosAtivos();
        this.atualizarTotalMatriculaEmDia();
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
                        FXMLMatriculaTableController matriculaTableController = FXMLMatriculaTableController.getInstance();
                        matriculaTableController.addObserver(this); // ✅ "this" = FXMLAcademiaController

                    }
                } else {
                    // curretStage.close();
                    MatriculaTable matriculaTable = new MatriculaTable();
                    curretStage = new Stage();
                    matriculaTable.start(curretStage);
                    FXMLMatriculaTableController matriculaTableController = FXMLMatriculaTableController.getInstance();
                    matriculaTableController.addObserver(this); // ✅ "this" = FXMLAcademiaController

                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(FXMLAcademiaController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        });

    }

}
