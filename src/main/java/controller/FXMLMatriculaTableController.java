/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.controller;

import main.java.academia.MatriculaTable;
import main.java.dao.MatriculaDAO;
import main.java.erro.Alerta;
import main.java.erro.AlunoDAOException;
import main.java.model.Aluno;
import main.java.model.Matricula;
import main.java.sqlite.ConexaoSQLiteJDBC;
import main.java.model.Aluno;
import main.java.model.Matricula;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.academia.Academia;
import main.java.util.DateSystem;

/**
 * FXML Controller class
 *
 * @author POSITIVO
 */
public class FXMLMatriculaTableController extends Observable implements Initializable, Observer {

    @FXML
    private Button btnMatricular;
    @FXML
    private Stage curretStage;
    @FXML
    private Label labelMatriculaError;
    @FXML
    private Button btn_close;
    @FXML
    private Button btn_home;
    @FXML
    private Button btn_editarMatricula;

    @FXML
    private Button btn_excluirMatricula;
    @FXML
    private TextField textFieldPesq;
     private static FXMLMatriculaTableController instance;

    public static FXMLMatriculaTableController getInstance() {
        return instance;
    }

    public FXMLMatriculaTableController() {
        instance = this;
    }

    @FXML
    private TableColumn<Matricula, Aluno> columnAluno = new TableColumn<>();
    ;

    @FXML
    private TableColumn<Matricula, Integer> columnId = new TableColumn<>();
    ;
;

    @FXML
    private TableColumn<Matricula, Double> columnValor = new TableColumn<>();
    ;
;

    @FXML
    private TableColumn<Matricula, Date> columndataDeVigencia = new TableColumn<>();
    ;
;

    @FXML
    private TableColumn<Matricula, Date> columndataVecimento = new TableColumn<>();
    ;
;

    @FXML
    private Label lbMatricula;

    @FXML
    private TableView<Matricula> tvMatricula = new TableView<Matricula>();

    private ObservableList<Matricula> matriculaList = FXCollections.observableArrayList();

    @FXML
    void closeAction(ActionEvent event) {
        this.telaClose();
    }

    public void telaClose() {
        if (MatriculaTable.getStage() != null) {
            MatriculaTable.getStage().close();
        }

    }

    private void abrirFormularioEdicaoMatricula(Matricula matricula, boolean edicao) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/FXMLMatricula.fxml"));
            Parent root = loader.load();
            FXMLMatriculaController controller = loader.getController();
            controller.preencherCamposMatricula(matricula);
            curretStage = new Stage();
            curretStage.setScene(new Scene(root));
            curretStage.setTitle("editar");
            curretStage.showAndWait();
            curretStage = null;
        } catch (IOException ex) {
            Logger.getLogger(FXMLMatriculaTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Matricula selectMatricula(Matricula matricula) {
        if (matricula != null) {
            System.out.println(matricula.getId());
            return matricula;
        } else {
            return null;
        }

    }

    private void abrirFormularioPagamento(Matricula matricula) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/FXMLPagamentoForm.fxml"));
            Parent root = loader.load();
            FXMLPagamentoFormController controller = loader.getController();
            registerObserver(controller);
            controller.preencherLabelPagamento(matricula);
            curretStage = new Stage();
            curretStage.setScene(new Scene(root));
            curretStage.setTitle("matricular");
            curretStage.showAndWait();
            curretStage = null;
        } catch (IOException ex) {
            Logger.getLogger(FXMLPagamentoFormController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @FXML
    public boolean removerAction(Matricula matricula) throws SQLException {
        boolean result = false;
        Matricula matriculaSelecionado = this.selectMatricula(matricula);
        if (matriculaSelecionado == null) {
            //  Se for nulo, exibe uma mensagem de erro
            System.out.println("erro ao remover aluno ");

        } else {
            matriculaSelecionado.delete();
            setChanged();
            notifyObservers(true);
            result = true;
        }
        return result;

    }

    @FXML
    public void atualizarTable() {
        tvMatricula.getItems().clear();
        try {
            List<Matricula> matriculas = Matricula.getAll();
            matriculaList.addAll(matriculas);
            tvMatricula.setItems(matriculaList);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMatriculaTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(" Método update chamado!");
        if (arg instanceof Boolean && (Boolean) arg) {
            Platform.runLater(() -> {
                atualizarTable();
            });
        }
    }

    private void registerObserver(FXMLPagamentoFormController instance) {
        instance.addObserver(this);
    }

    public void atualizaStatusMatricula(LocalDate data) {
        if (DateSystem.isVencendoHoje(data)) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tvMatricula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAluno.setCellValueFactory(new PropertyValueFactory<>("aluno"));
        columnAluno.setCellFactory(column -> {
            TableCell<Matricula, Aluno> cell = new TableCell<Matricula, Aluno>() {
                @Override
                protected void updateItem(Aluno item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNome());
                    }
                }
            };
            return cell;

        });

        columnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        columndataDeVigencia.setCellValueFactory(new PropertyValueFactory<>("dataDeVigencia"));
        columndataVecimento.setCellValueFactory(new PropertyValueFactory<>("dataVecimento"));
        columndataVecimento.setCellFactory(column -> {
            TableCell<Matricula, Date> cell = new TableCell<Matricula, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        // Convert Date to LocalDate for comparison and formatting
                        // Convert java.sql.Date to LocalDate
                        LocalDate dataVenc = item instanceof java.sql.Date ? ((java.sql.Date) item).toLocalDate() : item.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        // Formata a data no padrão dd/MM/yyyy
                        String formattedDate = dataVenc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        setText(formattedDate);
                        if (DateSystem.isVencido(dataVenc)) {
                            //setStyle("-fx-text-fill: red;  /* Light Red */");
                            setStyle("-fx-background-color: #FF4424; /* Light Red */");
                        } else if (DateSystem.isVencendoHoje(dataVenc)) {
                            //setStyle("-fx-text-fill: orange;");
                            setStyle("-fx-background-color: #FFA500");

                        } else {
                            // setStyle("-fx-text-fill: green;");
                            setStyle("-fx-background-color: #90EE90; /* Light Green */");
                        }
                    } else {
                        setText(null);
                        setStyle("");
                    }
                }
            };
            return cell;
        });
        // Configurando a formatação da célula para a coluna de data de nascimento
        columndataDeVigencia.setCellFactory(column -> {
            TableCell<Matricula, Date> cell = new TableCell<Matricula, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(format.format(item));
                    }
                }
            };
            return cell;
        });
        try {
            List<Matricula> matriculas = Matricula.getAll();
            matriculaList.addAll(matriculas);
            tvMatricula.setItems(matriculaList);
            
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMatriculaTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tvMatricula.getSelectionModel()
                .selectedItemProperty().addListener((obs, oldSelection, newSelection) -> selectMatricula(newSelection));

        btn_excluirMatricula.setOnMouseClicked(
                (MouseEvent e) -> {
                    Matricula matriculaSelecionada = tvMatricula.getSelectionModel().getSelectedItem();
                    if (matriculaSelecionada != null) {
                        try {
                            String title = "Confirmação de Exclusão";
                            String headerText = "Deseja realmente excluir o Matricula " + matriculaSelecionada.getAluno().getNome() + "?";
                            String contentText = "Esta ação é irreversível.";
                            if (Alerta.confirmationAlert(title, headerText, contentText)) {
                                // Atualiza a tabela
                                if (this.removerAction(matriculaSelecionada)) {
                                    Aluno aluno = matriculaSelecionada.getAluno();
                                    aluno.setStatus(false);
                                    try {
                                        aluno.save();
                                        matriculaList.remove(matriculaSelecionada);
                                        tvMatricula.refresh();
                                        
                                    } catch (SQLException ex) {
                                        Logger.getLogger(FXMLMatriculaTableController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(FXMLMatriculaTableController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {

                        labelMatriculaError.setText("selecione uma matricula  na tabela");

                    }
                }
        );
        btn_editarMatricula.setOnMouseClicked(
                (MouseEvent e) -> {
                    Matricula matriculaSelecionada = tvMatricula.getSelectionModel().getSelectedItem();
                    if (matriculaSelecionada == null) {
                        labelMatriculaError.setText("selecione uma matricula  na tabela");
                    } else {
                        try {
                            if (curretStage != null && curretStage.isShowing()) {
                                curretStage.toFront();
                            } else {
                                this.abrirFormularioEdicaoMatricula(matriculaSelecionada, true);
                                this.atualizarTable();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLMatriculaTableController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
        );
        btnMatricular.setOnMouseClicked(
                (MouseEvent e) -> {
                    Matricula matriculaSelecionada = tvMatricula.getSelectionModel().getSelectedItem();
                    if (matriculaSelecionada == null) {
                        labelMatriculaError.setText("selecione uma matricula  na tabela");
                    } else {
                        this.abrirFormularioPagamento(matriculaSelecionada);
                    }
                }
        );
        btn_home.setOnMouseClicked((MouseEvent e) -> {
            if (Academia.isStageOpen()) {
                this.telaClose();
                Academia.getStage().toFront();
            } else {
                Academia academia = new Academia();
                try {
                    academia.start(new Stage());
                } catch (Exception ex) {
                    Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        textFieldPesq.textProperty().addListener((observable, oldValue, newValue) -> {
            new Thread(() -> {
                try {
                    List<Matricula> resultados = MatriculaDAO.getMatriculasByAlunoNome(newValue);
                    // Atualize a lista na thread da UI
                    Platform.runLater(() -> {
                        tvMatricula.setItems(FXCollections.observableArrayList(resultados));
                    });
                } catch (SQLException e) {
                    // Trate a exceção
                    e.printStackTrace();
                }
            }).start();
        });


    }

}
