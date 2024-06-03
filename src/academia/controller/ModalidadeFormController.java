/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package academia.controller;

import academia.ModalidadeForm;
import academia.dao.ModalidadeDAO;
import academia.model.Modalidade;
import academia.sqlite.ConexaoSQLiteJDBC;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author POSITIVO
 */
public class ModalidadeFormController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label id;
    @FXML
    private Button addBtn;

    @FXML
    private Button btn_close;
    @FXML
    private Label msgErro;

    @FXML
    private TableColumn<Modalidade, Integer> columnId = new TableColumn<>();

    @FXML
    private TableColumn<Modalidade, String> columnNome = new TableColumn<>();

    @FXML
    private TableColumn<Modalidade, Double> columnValor = new TableColumn<>();

    @FXML
    private Button editarBtn;

    @FXML
    private Button excluirBtn;

    @FXML
    private TextField texFieldNome;

    @FXML
    public static void maskNumero(TextField textField) {
//        textField.lengthProperty().addListener((observable,oldValue,newValue)->{
//        String textDigitado = textField.getText();               
//        textDigitado =  textDigitado.replaceAll("\\d*(\\.\\d{0,2})?","");
//        textField.setText(textDigitado);
//        });
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Verifica se o novo valor corresponde ao padrão desejado
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                // Se não corresponder, redefine o texto para o valor antigo
                textField.setText(oldValue);
            }
        });

    }
    @FXML
    private TableView<Modalidade> tvModalidade = new TableView<Modalidade>();
    private ObservableList<Modalidade> modalidadeList = FXCollections.observableArrayList();
    @FXML
    private TextField texFieldValor;

    @FXML
    void closeAction(ActionEvent event) {
        this.telaClose();

    }

    public void telaClose() {
        if (ModalidadeForm.getStage() != null) {
            ModalidadeForm.getStage().close();
        }

    }

    private boolean verificaCamposVazios(String nome, String valor) {
        boolean result = true;
        if (nome.isEmpty()) {
            result = false;
            msgErro.setText("campo nome vazio");
        }
        if (valor.isEmpty()) {
            result = false;
            msgErro.setText("campo valor vazio");
        }
        return result;
    }

    @FXML
    private void limparForm() {
        if(!id.getText().isEmpty()){
            id.setText("");
        }
        texFieldNome.setText("");
        texFieldValor.setText("");

    }

    @FXML
    private void limparMensagemErro(Label labelErro) {
        msgErro.setText("");
    }

    private Modalidade pegaModalidade() {
        String nome = texFieldNome.getText();
        String v = texFieldValor.getText();
        if (verificaCamposVazios(nome, v)) {
            double valor = Double.parseDouble(v);
            if (!id.getText().isEmpty()) {

                Integer id_modalidade = Integer.parseInt(id.getText());
                Modalidade modalidade = new Modalidade(id_modalidade, nome, valor);
                System.out.println(modalidade.getNome() + " " + modalidade.getValor());
                return modalidade;
            } else {
                Modalidade modalidade = new Modalidade(null, nome, valor);
                System.out.println(modalidade.getNome() + " " + modalidade.getValor());

                return modalidade;

            }

        }
        return null;
    }

    @FXML
    public boolean salvarAction() throws SQLException {
        boolean result = false;
        Modalidade modalidade = pegaModalidade();
        if (modalidade == null) {
            //  Se for nulo, ex
            System.out.println("erro ao cadastra modalidade ");
            msgErro.setText("campos vazio");
            msgErro.setTextFill(Color.RED);
        } else {

            // Obtém a conexão antes de utilizá-la no AlunoDAO
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                ModalidadeDAO modalidadeDAO = new ModalidadeDAO(conn);
                List<Modalidade> modalidades = modalidadeDAO.getAll();
                if (modalidades.stream().anyMatch(m -> m.getNome().equalsIgnoreCase(modalidade.getNome()))) {
                    msgErro.setText("Essa modidade já foi adicionada");
                    System.out.println("Essa modidade já foi adicionada");

                } else {
                    if (modalidadeDAO.insert(modalidade)) {
                        msgErro.setText("sucesso");
                        msgErro.setTextFill(Color.GREEN);
                        System.out.println("sucesso");
                        result = true;
                    } else {
                        msgErro.setText("error");
                    }
                }
            }
        }
        System.out.println(result);
        return result;
    }
    @FXML
    public boolean removerAction(Modalidade modalidade) throws SQLException {
        boolean result = false;
        Modalidade modalidadeSelect = this.selectModalidade(modalidade);
        if (modalidadeSelect == null) {
            //  Se for nulo, exibe uma mensagem de erro
            System.out.println("erro ao remover Modalidade  ");
          
        } else {

            // Obtém a conexão antes de utilizá-la no AlunoDAO
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                // Inserir o aluno no banco usando o AlunoDAO
               ModalidadeDAO modalidadeDAO = new ModalidadeDAO(conn);
                if (modalidadeDAO.delete(modalidadeSelect.getId())) {
                    
                    System.out.println("modalidade removida com sucesso");
                    result = true;
                } else {
                    // labelError.setText("erro ao cadastra aluno");
                    System.out.println("erro ao remover modalidade ");

                }
            }
        }
        System.out.println(result);
        return result;

    }
    public Modalidade selectModalidade(Modalidade modalidade) {
        if (modalidade != null) {
            System.out.println(modalidade.getId());
            return modalidade;
        } else {
            return null;
        }
    }
    @FXML
     public void preencherCampos(Modalidade modalidade){
         id.setText(Integer.toString(modalidade.getId()));
         texFieldNome.setText(modalidade.getNome());
         texFieldValor.setText(Double.toString(modalidade.getValor()));
     }

    @FXML
    public void atualizarTable() {
        tvModalidade.getItems().clear();
        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            ModalidadeDAO modalidadeDAO = new ModalidadeDAO(conn);
            List<Modalidade> modalidades = modalidadeDAO.getAll();
            modalidadeList.addAll(modalidades);
            System.out.println("sucesso" + modalidadeList);
            tvModalidade.setItems(modalidadeList);
        } catch (SQLException ex) {
            Logger.getLogger(ModalidadeFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Configuring column resizing policy
        tvModalidade.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
            ModalidadeDAO modalidadeDAO = new ModalidadeDAO(conn);
            List<Modalidade> modalidades = modalidadeDAO.getAll();
            modalidadeList.addAll(modalidades);
            System.out.println("sucesso" + modalidadeList);
            tvModalidade.setItems(modalidadeList);
        } catch (SQLException ex) {
            Logger.getLogger(ModalidadeFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
       // Adiciona o listener para a seleção da TableView
        tvModalidade.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            preencherCampos(newValue);
           System.out.println("Essa modidade foi selecionada para ediçao"+newValue);

        }
    });
        maskNumero(texFieldValor);
         
        addBtn.setOnMouseClicked((MouseEvent e) -> {
            try {
                if (salvarAction()) {
                    // Se a operação de salvar for bem-sucedida, limpa o formulário e fecha o formulário de edição
                    limparForm();
                    // Notifica o observador
                    this.atualizarTable();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModalidadeFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        editarBtn.setOnMouseClicked((MouseEvent e) -> {
           
            try {
                if (salvarAction()) {
                    // Se a operação de salvar for bem-sucedida, limpa o formulário e fecha o formulário de edição
                    limparForm();
                    // Notifica o observador
                    this.atualizarTable();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ModalidadeFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        excluirBtn.setOnMouseClicked((MouseEvent e) -> {
            try {
                   Modalidade modalidade = tvModalidade.getSelectionModel().getSelectedItem();
                   if(modalidade != null){
                       
                       
                           if(this.removerAction(modalidade)){
                               System.out.println("madalidade removida com sucesso");
                               // Atualiza a tabela
                               modalidadeList.remove(modalidade);
                                this.atualizarTable();
                                limparForm();
                           }
                    } else {
                    System.out.println("Nenhuma modalidade selecionado.");
                    }
            } catch (SQLException ex) {
                    Logger.getLogger(ModalidadeFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
                         
        });
    } 
   
}
