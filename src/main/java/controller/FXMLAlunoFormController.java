/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.controller;

import main.java.academia.AlunoForm;
import main.java.erro.AlunoDAOException;
import main.java.model.Aluno;
import static main.java.model.Aluno.verificaDuplicidadesCpf;
import static main.java.model.Aluno.verificaDuplicidadesFone;

import java.io.IOException;

import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.var;

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
   
    private static final String EMAIL_PATTERN ="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
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
    private Label labelCpfErro;
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
        AlunoForm.closeStage();

    }

    public static String formatarTelefone(String telefone) {
        return String.format("(%s)%s-%s", telefone.substring(0, 2), telefone.substring(2, 7), telefone.substring(7));
    }

    @FXML
    public static void maskTelefone(TextField textField) {

        // Adiciona um listener à propriedade de texto do TextField
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                System.out.println(newValue.substring(newValue.length() - 1));
                System.out.println(newValue);
                if (newValue.length() > oldValue.length()) {
                    if (!newValue.matches("\\(\\d{2}\\)\\d{5}-\\d{4}")) {
                        newValue = newValue.replaceAll("[^\\d]", "");
                        if (newValue.length() >= 2) {
                            newValue = "(" + newValue.substring(0, 2) + ")" + newValue.substring(2);
                        }
                        if (newValue.length() >= 9) {
                            newValue = newValue.substring(0, 9) + "-" + newValue.substring(9);
                        }
                        if (newValue.length() < 15) {
                            textField.setText(newValue);
                            textField.positionCaret(newValue.length());
                        } else {
                            textField.setText(oldValue);
                        }
                    }
                }
            }

        });
    }

    @FXML
    public static void maskCpf(TextField textField) {
        //###.###.###-##"
        // Adiciona um listener à propriedade de texto do TextField
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                System.out.println(newValue.substring(newValue.length() - 1));
                System.out.println(newValue);
                if (newValue.length() > oldValue.length()) {
                    if (!newValue.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
                        newValue = newValue.replaceAll("[^\\d]", "");
                        if (newValue.length() >= 3) {
                            newValue = newValue.substring(0, 3) + "." + newValue.substring(3);
                        }
                        if (newValue.length() >= 7) {
                            newValue = newValue.substring(0, 7) + "." + newValue.substring(7);
                        }
                        if (newValue.length() >= 11) {
                            newValue = newValue.substring(0, 11) + "-" + newValue.substring(11);
                        }
                        if (newValue.length() < 15) {
                            textField.setText(newValue);
                            textField.positionCaret(newValue.length());
                        } else {
                            textField.setText(oldValue);
                        }
                    }
                }
            }

        });
    }

    private String retiraMask(String retMask) {
        String mask = retMask.replaceAll("[()\\s-\\.]", "");
        return mask;
    }

    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
        maskTelefone(textFieldFone);
        // Converte a data de nascimento de java.util.Date para java.time.LocalDate
        // Converte a data de nascimento de java.sql.Date para java.time.LocalDate
        java.util.Date utilDate = new java.util.Date(aluno.getDataNasc().getTime());
        // Convertendo java.util.Date para java.time.LocalDate
        LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        textFieldDataNasc.setValue(localDate);
        if (aluno.getCpf() == null) {
            textFieldCpf.setText("");
        } else {
            textFieldCpf.setText(aluno.getCpf());
            maskCpf(textFieldCpf);
        }
        if (aluno.getEmail() == null) {
            textFieldEmail.setText("");
        } else {
            textFieldEmail.setText(aluno.getEmail());
        }
        if (aluno.getEndereco() == null) {
            textFieldEmail.setText("");
        } else {
            textFieldEndereco.setText(aluno.getEndereco());
        }

    }

    private Aluno pegaAluno() throws SQLException {
        String nome = texFieldNome.getText();
        String fone = retiraMask(textFieldFone.getText());
        LocalDate data = textFieldDataNasc.getValue();
        String cpf = retiraMask(textFieldCpf.getText());
        String email = textFieldEmail.getText();
        String endereco = textFieldEndereco.getText();

        if (fone.length() < 11) {
            System.out.println("erro ao cadastra aluno ");
            foneError.setText(" erro telefone incompleto " + fone);
            return null;
        }
        if (!cpf.isEmpty()) {
            if (cpf.length() < 11) {
                System.out.println("erro ao cadastra aluno ");
                labelCpfErro.setText(" erro cpf incompleto " + cpf);
                return null;
            }
        }
        if(!email.isEmpty()){
            if(!isValid(email) ){
            labelError.setText("email invalido");
            labelError.setTextFill(Color.RED);
                return null;
            }
        }
        // Criar um novo objeto Aluno com os dados preenchidos
        if (verificaCamposVazios(nome, fone, data)) {
            Date dataNasc = java.sql.Date.valueOf(data);
            if (!id.getText().isEmpty()) {
                Integer id_aluno = Integer.parseInt(id.getText());
                System.out.println("tem id metodo put");

//                try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
//                    // Inserir o aluno no banco usando o AlunoDAO
//                   AlunoDAO alunoDAO = new AlunoDAO();
                Aluno aluno_bd = Aluno.find(id_aluno);
                System.out.println("CPF = " + cpf);
                Aluno aluno = new Aluno(id_aluno, nome, fone, dataNasc, cpf, email, endereco, aluno_bd.getStatus());
//                    System.out.println(conn);
                if (verificaDuplicidadesFone(aluno)) {
                    System.out.println("erro ao cadastra aluno ");
                    foneError.setText(" erro telefone ja cadastrado");
                    labelError.setTextFill(Color.RED);
                } else if (verificaDuplicidadesCpf(aluno)) {
                    System.out.println("erro ao cadastra aluno ");
                    labelCpfErro.setText(" erro cpf ja cadastrado");
                    labelError.setTextFill(Color.RED);
                } else {
                    return aluno;
                }
            } else {

                Aluno aluno = new Aluno(null, nome, fone, dataNasc, cpf, email, endereco, false);
                if (verificaDuplicidadesFone(aluno)) {
                    System.out.println("erro ao cadastra aluno ");
                    foneError.setText(" erro telefone ja cadastrado");
                    labelError.setTextFill(Color.RED);
                } else if (verificaDuplicidadesCpf(aluno)) {
                    System.out.println("erro ao cadastra aluno ");
                    labelCpfErro.setText(" erro cpf ja cadastrado");
                    labelError.setTextFill(Color.RED);
                } else {
                    return aluno;
                }
                //System.out.println("Nome " + aluno.getNome() + "Fone " + aluno.getFone()+"cpf " + aluno.getCpf()+ " " + "dataNasc = " + aluno.getDataNasc());
                //return aluno; 

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
            labelError.setText("Erro ao cadastra aluno");
            labelError.setTextFill(Color.RED);

        } else {
            try {
                // Obtém a conexão antes de utilizá-la no AlunoDAO
//            try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
//                // Inserir o aluno no banco usando o AlunoDAO
//                AlunoDAO alunoDAO = new AlunoDAO();

                aluno.save();
                // Fecha a conexão após utilizar o AlunoDAO
                labelError.setText("Aluno cadastrado com sucesso");
                labelError.setTextFill(Color.GREEN);
                System.out.println("sucesso " + aluno.getId());
                // Após inserir com sucesso, atualiza a tabela

                result = true;

            } catch (SQLException ex) {
                labelError.setText("Erro campos vazios." + ex.getMessage());
                labelError.setTextFill(Color.RED);
                Logger
                        .getLogger(FXMLAlunoFormController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println(result);
        return result;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //choiceBoxDDD.setValue(ddd[51]);
        maskTelefone(textFieldFone);
        maskCpf(textFieldCpf);
        btnSalvar.setOnMouseClicked(event -> {

//            String nome = texFieldNome.getText();
//            String fone = textFieldFone.getText();
//            LocalDate data = textFieldDataNasc.getValue();
//            String cpf = textFieldCpf.getText();
//            String email = textFieldEmail.getText();
//            String endereco = textFieldEndereco.getText();
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
                Logger.getLogger(FXMLAlunoFormController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });

// Adiciona ouvintes para limpar mensagens de erro ao focar nos campos
        texFieldNome.setOnMouseClicked(e -> limparMensagemErro(nomeError));
        textFieldFone.setOnMouseClicked(e -> limparMensagemErro(foneError));
        textFieldCpf.setOnMouseClicked(e -> limparMensagemErro(labelCpfErro));
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
