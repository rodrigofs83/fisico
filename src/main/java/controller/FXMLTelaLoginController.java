package main.java.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import main.java.academia.Academia;

import main.java.academia.Login;
import main.java.dao.MatriculaDAO;
import main.java.erro.Alerta;
import main.java.model.Aluno;
import main.java.model.Matricula;
import main.java.model.Modalidade;
import main.java.model.VerificaLogin;
import main.java.sqlite.ConexaoSQLiteJDBC;
import main.java.sqlite.Create;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author POSITIVO
 */
public class FXMLTelaLoginController implements Initializable {

    private VerificaLogin verifica = new VerificaLogin();
    Alerta alerta = new Alerta();
    @FXML
    private Button btn_close;

    @FXML
    private Button btn_login;

    @FXML
    private Hyperlink forgiotPass;

    @FXML
    private PasswordField passWord;

    @FXML
    private TextField userName;

    @FXML
    private Label labelErroMenssage;

    //função ao clica botão  fecha 
    @FXML
    void closeAction(ActionEvent event) {
        telaClose();
    }

//função ao clica botão login 
    @FXML
    void loginAction(ActionEvent event) throws IOException, Exception {

        verificaLogin();

    }

    public void telaClose() {
        Login.getStage().close();

    }

    private void abriTela() throws Exception {
        Academia a = new Academia();
        a.start(new Stage());
    }

    public void verificaLogin() throws Exception {

        if (userName.getText().isEmpty() == false && passWord.getText().isEmpty() == false) {
            boolean v = this.verifica.verificarLogin(userName.getText(), passWord.getText());
            if (v) {
                labelErroMenssage.setText("Login bem-sucedido!");
                labelErroMenssage.setTextFill(Color.GREEN);
                Academia a = new Academia();
                this.telaClose();
                try {

                    a.start(new Stage());
                } catch (Exception ex) {
                    labelErroMenssage.setText("catch");
                    alerta.showAlertBox("erro", ex.getMessage());

                    Logger.getLogger(FXMLTelaLoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                labelErroMenssage.setText("Credenciais inválidas. Tente novamente.");

            }
        } else {
            labelErroMenssage.setText("please entre user nome and password");
            alerta.showAlertBox("erro", labelErroMenssage.getText());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODo
            Create.createTables();
            ConexaoSQLiteJDBC.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLTelaLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        passWord.setText("admin");
        userName.setText("admin");
        btn_login.setOnMouseClicked((MouseEvent e) -> {

            try {
                verificaLogin();
            } catch (Exception ex) {
                labelErroMenssage.setText(ex.getMessage());
                Logger.getLogger(FXMLTelaLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        btn_login.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == KeyCode.ENTER) {

            }

        });

      

    }

}
