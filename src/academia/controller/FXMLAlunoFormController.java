/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package academia.controller;

import academia.AlunoForm;
import academia.AlunoTable;
import academia.dao.AlunoDAO;

import academia.model.Aluno;
import academia.sqlite.ConexaoSQLiteJDBC;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author POSITIVO
 */
public class FXMLAlunoFormController extends Observable implements Initializable {

    /**
     * Initializes the controller class.
     */
    ;
  

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btn_close;

    @FXML
    private Label labelDataNasc;

    @FXML
    private Label labelError;

    @FXML
    private Label nomeError;

    @FXML
    private Label foneError;
    @FXML
    private Label dataError;
    @FXML
    private Label labelFone;

    @FXML
    private Label labelNome;
    @FXML
    private Label id;

    @FXML
    private TextField texFieldNome;

    @FXML
    private DatePicker textFieldDataNasc;

    @FXML
    private TextField textFieldFone;
    @FXML
    private TextField textFieldCpf;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldEndereco;
    @FXML
    private Label labelTitulo;
    private static FXMLAlunoFormController instance;

    public static FXMLAlunoFormController getInstance() {
        return instance;
    }

    public FXMLAlunoFormController() {
        instance = this;
    }

    @FXML
    public void setLabelEdicao(boolean edicao) {
        labelTitulo.setText("Editar Aluno"); // Modifica o label para indicar edição

    }

    @FXML
    void cancelarAction(ActionEvent event) {
        this.telaClose();
    }

    @FXML
    void closeAction(ActionEvent event) {
        this.telaClose();

    }

    public void telaClose() {
        if (AlunoForm.getStage() != null) {
            AlunoForm.getStage().close();
        }

    }

    private boolean verificaCamposVazios(String nome, String fone, LocalDate dataNasc) {

        boolean result = true;
        if (nome.isEmpty()) {
            result = false;
            nomeError.setText("campo nome vazio");

        }
        if (fone.isEmpty()) {
            result = false;
            foneError.setText("campo fone vazio");

        }
        if (dataNasc == null) {
            result = false;
            dataError.setText("campo data vazio");
        }
        return result;
    }

    @FXML
    private void limparForm() {
        texFieldNome.setText("");
        textFieldFone.setText("");
        textFieldDataNasc.setValue(null);
        textFieldCpf.setText("");
        textFieldEmail.setText("");
        textFieldEndereco.setText("");
    }

    @FXML
    private void limparMensagemErro(Label labelErro) {
        labelErro.setText("");
    }

    @FXML
    public void preencherCampos(Aluno aluno) {

        this.setLabelEdicao(true);

        id.setText(Integer.toString(aluno.getId()));

        texFieldNome.setText(aluno.getNome());
        textFieldFone.setText(aluno.getFone());
        // Converte a data de nascimento de java.util.Date para java.time.LocalDate
        // Converte a data de nascimento de java.sql.Date para java.time.LocalDate
        java.util.Date utilDate = new java.util.Date(aluno.getDataNasc().getTime());
        // Convertendo java.util.Date para java.time.LocalDate
        LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        textFieldDataNasc.setValue(localDate);
        textFieldCpf.setText(aluno.getCpf());
        textFieldEmail.setText(aluno.getEmail());
        textFieldEndereco.setText(aluno.getEndereco());
    }

    private Aluno pegaAluno() throws SQLException {
        String nome = texFieldNome.getText();
        String fone = textFieldFone.getText();
        LocalDate data = textFieldDataNasc.getValue();
        String cpf = textFieldCpf.getText();
        String email = textFieldEmail.getText();
        String endereco = textFieldEndereco.getText();
        // Criar um novo objeto Aluno com os dados preenchidos
        if (verificaCamposVazios(nome, fone, data)) {
            Date dataNasc = java.sql.Date.valueOf(data);
            if (!id.getText().isEmpty()) {
                Integer id_aluno = Integer.parseInt(id.getText());
                System.out.println("tem id metodo put");
                try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                    // Inserir o aluno no banco usando o AlunoDAO
                    AlunoDAO alunoDAO = new AlunoDAO();
                    Aluno aluno_bd = alunoDAO.getById(id_aluno).get();
                    Aluno aluno = new Aluno(id_aluno, nome, fone, dataNasc, cpf, email, endereco, aluno_bd.getStatus());
                    System.out.println(conn);
                    return aluno;
                }
            } else {
                Aluno aluno = new Aluno(null, nome, fone, dataNasc, cpf, email, endereco, false);
                // Inserir o aluno no banco usando o AlunoDAO
                System.out.println("Nome " + aluno.getNome() + "Fone" + aluno.getFone() + " " + "dataNasc = " + aluno.getDataNasc());
                return aluno;
            }
        }
        return null;
    }

    @FXML
    public boolean salvarAction() throws SQLException {
        boolean result = false;
        Aluno aluno = this.pegaAluno();
        if (aluno == null) {
            //  Se for nulo, exibe uma mensagem de erro
            System.out.println("erro ao cadastra aluno ");
            labelError.setText("Erro campos vazios.");
            labelError.setTextFill(Color.RED);
        } else {
            // Obtém a conexão antes de utilizá-la no AlunoDAO
            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                // Inserir o aluno no banco usando o AlunoDAO
                AlunoDAO alunoDAO = new AlunoDAO();
                if (alunoDAO.insert(aluno)) {
                    // Fecha a conexão após utilizar o AlunoDAO
                    labelError.setText("Aluno cadastrado com sucesso");
                    labelError.setTextFill(Color.GREEN);
                    System.out.println("sucesso");
                    // Após inserir com sucesso, atualiza a tabela

                    result = true;
                } else {
                    labelError.setText("erro ao cadastra aluno");
                    System.out.println("erro ao cadastra aluno ");

                }
            }
        }
        System.out.println(result);
        return result;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        btnSalvar.setOnMouseClicked((MouseEvent e) -> {
            String nome = texFieldNome.getText();
            String fone = textFieldFone.getText();
            LocalDate data = textFieldDataNasc.getValue();
            String cpf = textFieldCpf.getText();
            String email = textFieldEmail.getText();
            String endereco = textFieldEndereco.getText();
            try {
                // Chama o método salvarAction() para realizar a operação de salvar
                if (salvarAction()) {
                    // Se a operação de salvar for bem-sucedida, limpa o formulário e fecha o formulário de edição
                    limparForm();
                    // Notifica o observador
                    setChanged();
                    notifyObservers(true);

                    Stage stage = (Stage) btnSalvar.getScene().getWindow(); // Obtém o Stage atual
                    stage.close(); // Fecha o formulário de edição
                    // Após fechar o formulário de edição, atualiza a tabela de alunos

                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLAlunoFormController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FXMLAlunoFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

// Adiciona ouvintes para limpar mensagens de erro ao focar nos campos
        texFieldNome.setOnMouseClicked(e -> limparMensagemErro(nomeError));
        textFieldFone.setOnMouseClicked(e -> limparMensagemErro(foneError));
        textFieldDataNasc.getEditor().setOnMouseClicked(e -> limparMensagemErro(dataError));
        btnCancelar.setOnMouseClicked((MouseEvent e) -> {
            Stage stage = (Stage) btnCancelar.getScene().getWindow(); // Obtém o Stage atual
            stage.close();
        });
        btn_close.setOnMouseClicked((MouseEvent e) -> {
            Stage stage = (Stage) btnCancelar.getScene().getWindow(); // Obtém o Stage atual
            stage.close();
        });

    }

}
