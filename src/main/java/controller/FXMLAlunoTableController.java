/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.controller;

import main.java.controller.FXMLAlunoFormController;
import main.java.academia.AlunoForm;
import main.java.academia.AlunoTable;
import main.java.erro.Alerta;
import main.java.erro.AlunoDAOException;
import main.java.erro.ApagaMsgError;
import main.java.model.Aluno;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.academia.Academia;

/**
 *
 *
 * @author POSITIVO
 */
public class FXMLAlunoTableController implements Initializable, Observer {

    ApagaMsgError apagaMsg = new ApagaMsgError();
    @FXML
    private VBox alunoFormContainer;
    @FXML
    private Stage curretStage;
    @FXML
    private Label lbAluno;
    @FXML
    private Label msgError;
    @FXML
    private Button btnMatricular;
    @FXML
    private Button btn_cadastra;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btn_editarAluno;
    @FXML
    private Button btn_excluirAluno;
    @FXML
    private Button btn_home;
    @FXML
    private TableColumn<Aluno, Integer> columnId = new TableColumn<>();
    @FXML
    private TableColumn<Aluno, String> columnNome = new TableColumn<>();
    @FXML
    private TableColumn<Aluno, Date> columnDataNasc = new TableColumn<>();
    @FXML
    private TableColumn<Aluno, String> columnFone = new TableColumn<>();

    @FXML
    private TableView<Aluno> tvAluno = new TableView<Aluno>();

    private ObservableList<Aluno> alunosList = FXCollections.observableArrayList();

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private Button btn_close;

    @FXML
    void closeAction(ActionEvent event) {
        this.telaClose();

    }

    public void telaClose() {
        if (AlunoTable.getStage() != null) {
            AlunoTable.getStage().close();
        }

    }
    @FXML
    void home(ActionEvent event) {
           
    }

    public Aluno selectAluno(Aluno aluno) {
        if (aluno != null) {
            System.out.println(aluno.getId());
            return aluno;
        } else {
            return null;
        }
    }

    @FXML
    public boolean editaAluno(Aluno aluno) throws SQLException {
        // Obtém o aluno selecionado na tabela    
        boolean result = false;
        Aluno alunoSelecionado = this.selectAluno(aluno);
        if (alunoSelecionado == null) {
            //  Se for nulo, exibe uma mensagem de erro
            System.out.println("erro ao remover aluno ");
            // labelError.setText("Erro campos vazios.");
            //labelError.setTextFill(Color.RED);
        } else {
            try {
                alunoSelecionado.save();
                result = true;
            } catch (SQLException ex) {
                Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
//            // Obtém a conexão antes de utilizá-la no AlunoDAO
//            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
//                // Inserir o aluno no banco usando o AlunoDAO
//                AlunoDAO alunoDAO = new AlunoDAO();
//                if (alunoDAO.update(alunoSelecionado)) {
//                    // Fecha a conexão após utilizar o AlunoDAO
//                    // labelError.setText("Aluno cadastrado com sucesso");
//                    //labelError.setTextFill(Color.GREEN);
//                    System.out.println("Aluno editado com sucesso");
//                } else {
//                    // labelError.setText("erro ao cadastra aluno");
//                    System.out.println("erro ao editar aluno ");
//
//                }
//            // Obtém a conexão antes de utilizá-la no AlunoDAO
//            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
//                // Inserir o aluno no banco usando o AlunoDAO
//                AlunoDAO alunoDAO = new AlunoDAO();
//                if (alunoDAO.update(alunoSelecionado)) {
//                    // Fecha a conexão após utilizar o AlunoDAO
//                    // labelError.setText("Aluno cadastrado com sucesso");
//                    //labelError.setTextFill(Color.GREEN);
//                    System.out.println("Aluno editado com sucesso");
//                } else {
//                    // labelError.setText("erro ao cadastra aluno");
//                    System.out.println("erro ao editar aluno ");
//
//                }
        }

        System.out.println(result);
        return result;
    }

    ;

    @FXML
    public boolean removerAction(Aluno aluno) throws SQLException {
        boolean result = false;
        Aluno alunoSelecionado = this.selectAluno(aluno);
        if (alunoSelecionado == null) {
            //  Se for nulo, exibe uma mensagem de erro
            System.out.println("erro ao remover aluno ");
        } else {
            if (aluno.getStatus()) {
                msgError.setText("aluno matriculado não pode se excluidor");
                apagaMsg.apagaMsg(msgError);
            } else {
                alunoSelecionado.delete();
                result = true;
            }

        }
        System.out.println(result);
        return result;
    }

    @FXML
    public void atualizarTable() {
        tvAluno.getItems().clear();

        try {
            //        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
//            AlunoDAO alunoDAO = new AlunoDAO();
            List<Aluno> alunos = Aluno.getAll();
            System.out.println(alunos);
            alunosList.addAll(alunos);
//            // Associar a lista de alunos à tabela
            tvAluno.setItems(alunosList);
//
//        } catch (SQLException ex) {
//            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void abrirFormularioEdicao(Aluno aluno, boolean edicao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/FXMLAlunoForm.fxml"));
            Parent root = loader.load();
            FXMLAlunoFormController controller = loader.getController();
            controller.preencherCampos(aluno);
            curretStage = new Stage();
            curretStage.setScene(new Scene(root));
            curretStage.setTitle("editar");
            curretStage.showAndWait();
            curretStage = null;
        } catch (IOException ex) {
            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void abrirFormularioMatricula(Aluno aluno) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/FXMLMatricula.fxml"));
            Parent root = loader.load();
            FXMLMatriculaController controller = loader.getController();
            controller.preencherLabelAluno(aluno);
            curretStage = new Stage();
            curretStage.setScene(new Scene(root));
            curretStage.setTitle("matricular");
            curretStage.showAndWait();
            curretStage = null;
        } catch (IOException ex) {
            Logger.getLogger(FXMLMatriculaController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

 

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Método update chamado!");
        if (arg instanceof Boolean && (Boolean) arg) {
            Platform.runLater(() -> {
                atualizarTable();
            });
        }
    }

    private void registerObserver(FXMLAlunoFormController instance) {
        instance.addObserver(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Garante que a instância de FXMLAlunoFormController seja inicializada antes de chamar registerObserver

        // Configuring column resizing policy
        tvAluno.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        columnDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
        // Configurando a formatação da célula para a coluna de data de nascimento
        columnDataNasc.setCellFactory(column -> {
            TableCell<Aluno, Date> cell = new TableCell<Aluno, Date>() {
                //private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

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
        columnFone.setCellValueFactory(new PropertyValueFactory<>("fone"));
        columnFone.setCellFactory(column -> {
            TableCell<Aluno, String> cell = new TableCell<Aluno, String>() {
                //private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
      
                        setText(FXMLAlunoFormController.formatarTelefone(item));
                    }
                }
            };
            return cell;
        });
//        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
//            AlunoDAO alunoDAO = new AlunoDAO();
        try {
            List<Aluno> alunos = Aluno.getAll();
            System.out.println(alunos);
            alunosList.addAll(alunos);
            // Associar a lista de alunos à tabela
            tvAluno.setItems(alunosList);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

//        } catch (SQLException ex) {
//            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        // Adicionar um ouvinte de evento para capturar a seleção do usuário na tabela
        tvAluno.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> selectAluno(newSelection));
        btn_editarAluno.setOnMouseClicked((MouseEvent e) -> {
            Aluno alunoSelecionado = tvAluno.getSelectionModel().getSelectedItem();
            if (alunoSelecionado != null) {
                if (curretStage != null) {
                    System.out.println(" if 1 janela esta abreta");
                    if (curretStage.getTitle().equals("editar")) {
                        System.out.println("if 1,2  o titulo e editar");
                        curretStage.toFront();
                    } else {
                        String title = "Janela";
                        String headerText = "janela " + curretStage.getTitle() + " esta abreta ";
                        String contentText = "Para continua aperte ok para fechala  ";
                        if (Alerta.confirmationAlert(title, headerText, contentText)) {
                            System.out.println(" else 1 if2 o titulo não e edita ");
                            curretStage.toFront();
                            curretStage.close();
                        }
                    }
                } else {
                    System.out.println("a janela esta null ou fechada ");
                    try {
                        this.abrirFormularioEdicao(alunoSelecionado, true);
                        this.atualizarTable();
                    } catch (Exception ex) {
                        Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                msgError.setText("selecione um aluno na tabela para edição");
                apagaMsg.apagaMsg(msgError);
            }
        });
        btn_excluirAluno.setOnMouseClicked((MouseEvent e) -> {
            if (curretStage != null && curretStage.isShowing()) {
                String title = "telas ";
                String headerText = "telas abretas  ";
                String contentText = "Tela" + curretStage.getTitle() + "esta abreta sera fechada.";
                if (Alerta.confirmationAlert(title, headerText, contentText)) {
                    curretStage.close(); // Traz a janela para frente

                }
            } else {
                Aluno alunoSelecionado = tvAluno.getSelectionModel().getSelectedItem();
                if (alunoSelecionado != null) {
                    String title = "Confirmação de Exclusão";
                    String headerText = "Deseja realmente excluir o aluno " + alunoSelecionado.getNome() + "?";
                    String contentText = "Esta ação é irreversível.";
                    if (Alerta.confirmationAlert(title, headerText, contentText)) {
                        try {
                            if (this.removerAction(alunoSelecionado)) {
                                System.out.println("Aluno remover com sucesso");
                                // Atualiza a tabela
                                alunosList.remove(alunoSelecionado);
                                tvAluno.refresh();
                            } else {
                                // User clicked "Cancelar" or closed the alert
                                System.out.println("Exclusão cancelada.");
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                } else {
                    System.out.println("Nenhum aluno selecionado.");
                    msgError.setText("selecione um aluno na tabela para exclusão");
                    apagaMsg.apagaMsg(msgError);
                }
            }
        });
        btn_cadastra.setOnMouseClicked((MouseEvent e) -> {
            if (AlunoForm.isStageOpen()) {
                System.out.println(" if = 1   btn cadastra janela esta abreta ");
                if (AlunoForm.getStage().getTitle().equals("cadastrar")) {
                    System.out.println(" if = 1.2 o titulo e cadastra.");
                    AlunoForm.getStage().toFront();
                } else {
                    System.out.println("else 1 do if 1.2  titulo nao e cadastra ");
                    AlunoForm.getStage().toFront();
                    AlunoForm.getStage().close();

                }
            } else {
                System.out.println("else 1 janela fechada ou null");

                AlunoForm a = new AlunoForm();
                try {
                    // Check if the AlunoForm window is open
                    Stage stage = new Stage();
                    stage.setTitle("cadastrar");
                    a.start(stage);

                    FXMLAlunoFormController formController = FXMLAlunoFormController.getInstance();
                    if (formController != null) {
                        this.registerObserver(formController);

                    } else {
                        System.err.println("Instância de FXMLAlunoFormController é nula.");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btn_home.setOnMouseClicked((MouseEvent e) -> {
           if(Academia.isStageOpen()){
               this.telaClose();
               Academia.getStage().toFront();
           }else{
               Academia academia = new Academia();
               try {
                   academia.start(new Stage());
               } catch (Exception ex) {
                   Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
        });
        btnRefresh.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Botão de atualização clicado");
            this.atualizarTable();
        });
        btnMatricular.setOnMouseClicked((MouseEvent e) -> {
            Aluno alunoSelecionado = tvAluno.getSelectionModel().getSelectedItem();

            if (alunoSelecionado == null) {
                msgError.setText("Selecione um aluno na tabela para matricular.");
                apagaMsg.apagaMsg(msgError);
                return; // Sai da função se nenhum aluno foi selecionado
            }
            if (alunoSelecionado.getStatus()) {
                msgError.setText("O aluno selecionado já está matriculado.");
                apagaMsg.apagaMsg(msgError);
                return; // Sai da função se o aluno já está matriculado
            }

            // Verifica se já existe uma janela de matrícula aberta e trata a ação do usuário
            if (curretStage != null && curretStage.getTitle().equals("matricular")) {
                // Se a janela já está aberta, traz ela para frente
                curretStage.toFront();
                return; // Sai da função, pois a janela já está aberta
            }

            // Caso contrário, tenta abrir a janela de matrícula
            try {
                this.abrirFormularioMatricula(alunoSelecionado);
                this.atualizarTable();
            } catch (Exception ex) {
                // Exibe uma mensagem de erro mais amigável para o usuário
                String title = "Janela";
                String headerText = "janela " + curretStage.getTitle() + " esta abreta ";
                String contentText = "Para continua aperte ok para fechala  ";
                Alerta.confirmationAlert(title, headerText, contentText);

            }
            /*
            if (alunoSelecionado != null) {
                System.out.println(" if 1 aluno nao e nulo");
                if (curretStage != null) {
                    System.out.println(" if 1.1 janela esta abreta");
                    if (curretStage.getTitle().equals("matricular")) {
                        System.out.println("if 1.2  o titulo e editar");
                        curretStage.toFront();
                    } else {
                        String title = "Janela";
                        String headerText = "janela " + curretStage.getTitle() + " esta abreta ";
                        String contentText = "Para continua aperte ok para fechala  ";
                        if (Alerta.confirmationAlert(title, headerText, contentText)) {
                            System.out.println(" else 1 if 1.2 o titulo não e edita ");
                            curretStage.toFront();
                            curretStage.close();
                        }
                    }
                }else if(!alunoSelecionado.getStatus()){
                    try {
                        this.abrirFormularioMatricula(alunoSelecionado);
                    } catch (Exception ex) {
                        Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    msgError.setText("aluno selecionado  ja esta matriculado");
                    apagaMsg.apagaMsg(msgError);
                }
            } else {

                msgError.setText("selecione um aluno na tabela para Matricula");
                apagaMsg.apagaMsg(msgError);
            }*/
        });
    }

}
