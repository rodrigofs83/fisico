package academia.controller;

import java.net.URL;
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



/**
 *
 * @author POSITIVO
 */
public class FXMLTelaLoginController implements Initializable {

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

    @FXML
    void closeAction(ActionEvent event) {
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginAction(ActionEvent event) {
        if(userName.getText().isEmpty() == false && passWord.getText().isEmpty()== false){
            labelErroMenssage.setText("you try to login");
        }else{
            labelErroMenssage.setText("please entre user nome and password");
        }
    }
    
   
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    

}
