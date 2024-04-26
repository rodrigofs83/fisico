/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package academia.controller;

import academia.controller.FXMLAlunoFormController;
import academia.AlunoForm;
import academia.AlunoTable;
import academia.dao.AlunoDAO;

import academia.model.Aluno;
import academia.sqlite.ConexaoSQLiteJDBC;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author POSITIVO
 */
public class FXMLAlunoTableController implements Initializable, Observer {

    @FXML
    private VBox alunoFormContainer;

    @FXML
    private Label lbAluno;
    @FXML
    private Button btn_cadastra;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btn_editarAluno;
    @FXML
    private Button btn_excluirAluno;
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

    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

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

            // Obtém a conexão antes de utilizá-la no AlunoDAO
            try (Connection conn = ConexaoSQLiteJDBC.conectar()) {
                // Inserir o aluno no banco usando o AlunoDAO
                AlunoDAO alunoDAO = new AlunoDAO(conn);
                if (alunoDAO.put(alunoSelecionado)) {
                    // Fecha a conexão após utilizar o AlunoDAO
                    // labelError.setText("Aluno cadastrado com sucesso");
                    //labelError.setTextFill(Color.GREEN);
                    System.out.println("Aluno editado com sucesso");
                    result = true;
                } else {
                    // labelError.setText("erro ao cadastra aluno");
                    System.out.println("erro ao editar aluno ");

                }
            }
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
            // labelError.setText("Erro campos vazios.");
            //labelError.setTextFill(Color.RED);
        } else {

            // Obtém a conexão antes de utilizá-la no AlunoDAO
            try (Connection conn = ConexaoSQLiteJDBC.conectar()) {
                // Inserir o aluno no banco usando o AlunoDAO
                AlunoDAO alunoDAO = new AlunoDAO(conn);
                if (alunoDAO.delete(alunoSelecionado.getId())) {
                    // Fecha a conexão após utilizar o AlunoDAO
                    // labelError.setText("Aluno cadastrado com sucesso");
                    //labelError.setTextFill(Color.GREEN);
                    System.out.println("Aluno remover com sucesso");
                    result = true;
                } else {
                    // labelError.setText("erro ao cadastra aluno");
                    System.out.println("erro ao remover aluno ");

                }
            }
        }
        System.out.println(result);
        return result;

    }

    @FXML
    public void atualizarTable() {
        tvAluno.getItems().clear();
        try (Connection conn = ConexaoSQLiteJDBC.conectar()) {
            AlunoDAO alunoDAO = new AlunoDAO(conn);
            List<Aluno> alunos = alunoDAO.getAll();
            System.out.println(alunos);
            alunosList.addAll(alunos);
            // Associar a lista de alunos à tabela
            tvAluno.setItems(alunosList);

        } catch (SQLException ex) {
            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void abrirFormularioEdicao(Aluno aluno, boolean edicao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/academia/View/FXMLAlunoForm.fxml"));
            Parent root = loader.load();
            FXMLAlunoFormController controller = loader.getController();
            controller.preencherCampos(aluno);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
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
        columnFone.setCellValueFactory(new PropertyValueFactory<>("fone"));
        
        try (Connection conn = ConexaoSQLiteJDBC.conectar()) {
            AlunoDAO alunoDAO = new AlunoDAO(conn);
            List<Aluno> alunos = alunoDAO.getAll();
            System.out.println(alunos);
            alunosList.addAll(alunos);
            // Associar a lista de alunos à tabela
            tvAluno.setItems(alunosList);

        } catch (SQLException ex) {
            Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Adicionar um ouvinte de evento para capturar a seleção do usuário na tabela

        tvAluno.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> selectAluno(newSelection));
        btn_editarAluno.setOnMouseClicked((MouseEvent e) -> {

            Aluno alunoSelecionado = tvAluno.getSelectionModel().getSelectedItem();
            if (alunoSelecionado != null) {

                try {
//                    try (Connection conn = ConexaoSQLiteJDBC.conectar()) {
//                    AlunoDAO alunoDAO = new AlunoDAO(conn);
//                        Optional<Aluno> alunoPesq = alunoDAO.getById(alunoSelecionado.getId());
//                        System.out.println(alunoPesq);
                      this.abrirFormularioEdicao(alunoSelecionado, true);
//                } catch (SQLException ex) {
//                    Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
//                }
                    
                    
                    
                } catch (Exception ex) {
                    Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
                }

                this.atualizarTable();

            }
        });
        btn_excluirAluno.setOnMouseClicked((MouseEvent e) -> {
            try {
                Aluno alunoSelecionado = tvAluno.getSelectionModel().getSelectedItem();
                if (alunoSelecionado != null) {
                    if (this.removerAction(alunoSelecionado)) {
                        System.out.println("Aluno remover com sucesso");
                        // Atualiza a tabela
                        alunosList.remove(alunoSelecionado);
                        tvAluno.refresh();
                    }
                } else {
                    System.out.println("Nenhum aluno selecionado.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btn_cadastra.setOnMouseClicked((MouseEvent e) -> {

            try {
                AlunoForm alunoForm = new AlunoForm();
                // Adicionar o nó raiz ao StackPane
                // Adicionar o nó raiz à StackPane
                Stage alunoFormStage = alunoForm.getStage();
                if (alunoFormStage == null || !alunoFormStage.isShowing()) {
                    alunoForm.start(new Stage());
                }
                FXMLAlunoFormController formController = FXMLAlunoFormController.getInstance();
                if (formController != null) {
                    System.err.println("Instância de FXMLAlunoFormController .");
                    this.registerObserver(formController);

                } else {
                    System.err.println("Instância de FXMLAlunoFormController é nula.");
                }
            } catch (Exception ex) {
                Logger.getLogger(FXMLAlunoTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnRefresh.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Botão de atualização clicado");
            this.atualizarTable();
        });
    }

   
}
