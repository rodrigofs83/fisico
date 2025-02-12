/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.java.academia.PagamentoForm;
import main.java.model.Matricula;
import main.java.model.Pagamento;
import main.java.util.DateSystem;

/**
 * FXML Controller class
 *
 * @author POSITIVO
 */
public class FXMLPagamentoFormController extends Observable implements Initializable {

    /**
     * Initializes the controller class.
     */
    private String[] formasPagamento = {"Cartão", "Pix", "Dinheiro"};
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btn_close;

    @FXML
    private ChoiceBox<String> choiceBoxFormaPag;

    @FXML
    private DatePicker dataPickerRefFinal;

    @FXML
    private DatePicker dataPickerRefInicial;
    @FXML
    private DatePicker dataPickerPagamento;

    @FXML
    private Label idMatricula;

    @FXML
    private Label labelFone;

    @FXML
    private Label labelnome;

    @FXML
    private TextField textFieldValor;

    private static FXMLPagamentoFormController instance;

    public static FXMLPagamentoFormController getInstance() {
        return instance;
    }

    public FXMLPagamentoFormController() {
        instance = this;
    }

    void preencherLabelPagamento(Matricula matricula) {
        idMatricula.setText(Integer.toString(matricula.getId()));
        labelFone.setText(matricula.getAluno().getFone());
        labelnome.setText(matricula.getAluno().getNome());
        java.util.Date utilDateVigencia = new java.util.Date(matricula.getDataDeVigencia().getTime());
        java.util.Date utilDateVencimento = new java.util.Date(matricula.getDataVecimento().getTime());
        LocalDate localDateVigencia = utilDateVigencia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateVenc = utilDateVencimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dataPickerRefInicial.setValue(localDateVigencia);
        dataPickerRefFinal.setValue(localDateVenc);
        textFieldValor.setText(Double.toString(matricula.getValor()));
        LocalDate dataAtual = LocalDate.now();
        dataPickerPagamento.setValue(dataAtual);
    }

    private Pagamento pegaPagamento() throws SQLException {
        Pagamento pagamento = new Pagamento();
        LocalDate data = dataPickerPagamento.getValue();
        int idMatri = Integer.parseInt(idMatricula.getText());
        if (dataPickerPagamento.getValue() != null && !textFieldValor.getText().isEmpty()) {
            Date dataPag = java.sql.Date.valueOf(data);
            Double valor = Double.parseDouble(textFieldValor.getText());
            String formPag = choiceBoxFormaPag.getValue();
            Matricula matricula = new Matricula();
            matricula = Matricula.find(idMatri);
            pagamento.setData_pg(dataPag);
            pagamento.setValor_pg(valor);
            pagamento.setForma_pg(formPag);
            pagamento.setMatricula(matricula);
            System.out.println(dataPag);
            System.out.println(valor);
            System.out.println(formPag);
            return pagamento;
        } else {
            return null;
        }

    }

    public void atualizaDataVenc(Pagamento pagamento) {
        java.util.Date dataVenc = DateSystem.convertSQLDateToDate(pagamento.getMatricula().getDataVecimento());
        LocalDate matDataVenc = DateSystem.converteDateParalocalDate(dataVenc);
        matDataVenc = DateSystem.atualizarParaProximoMes(matDataVenc);
        java.util.Date proxDataVenc = DateSystem.convertLocalDateToDate(matDataVenc);
        pagamento.getMatricula().setDataVecimento(proxDataVenc);

        java.util.Date dataVig = DateSystem.convertSQLDateToDate(pagamento.getMatricula().getDataDeVigencia());
        LocalDate matDataVig = DateSystem.converteDateParalocalDate(dataVig);
        matDataVig = DateSystem.retrocedeMes(matDataVenc);
        java.util.Date proxDataVig = DateSystem.convertLocalDateToDate(matDataVig);
        pagamento.getMatricula().setDataDeVigencia(proxDataVig);
        System.out.println("data vence  " + pagamento.getMatricula().getDataVecimento());

    }

    @FXML
    public boolean salvarAction() throws SQLException {
        boolean result = false;
        Pagamento pagamento = pegaPagamento();
        System.out.println(pagamento);
        if (pagamento == null) {
            System.out.println("erro");
        } else {
            pagamento.save();
            atualizaDataVenc(pagamento);
            pagamento.getMatricula().save();
            System.out.println("data vencimento" + pagamento.getMatricula().getDataVecimento());
            result = true;
        }
        return result;
    }

    @FXML
    void cancelarAction(ActionEvent event) {
        this.telaClose();
    }

    public void telaClose() {
        if (PagamentoForm.getStage() != null) {
            PagamentoForm.getStage().close();
        }

    }

    @FXML
    void closeAction(ActionEvent event) {

        this.telaClose();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        choiceBoxFormaPag.getItems().addAll(formasPagamento);
        choiceBoxFormaPag.setValue(formasPagamento[2]);
        dataPickerRefInicial.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Ajusta para o último dia do mês
                dataPickerRefFinal.setValue(DateSystem.atualizarParaProximoMes(newValue));
            } else {
                // Limpa a data de vencimento se a data de vigência for nula
                dataPickerRefFinal.setValue(null);
            }
        });
        dataPickerRefFinal.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Ajusta para o último dia do mês
                dataPickerRefInicial.setValue(DateSystem.retrocedeMes(newValue));
            } else {
                // Limpa a data de vencimento se a data de vigência for nula
                dataPickerRefInicial.setValue(null);
            }
        });
        btnSalvar.setOnMouseClicked((MouseEvent e) -> {

            try {
                if (salvarAction()) {
                    System.out.println("salva pagamento sucesso ");
                    setChanged();
                    notifyObservers(true);
                    Stage stage = (Stage) btnSalvar.getScene().getWindow(); // Obtém o Stage atual
                    stage.close();

                } else {
                    System.out.println("erro ao salva pagamento ");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLPagamentoFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

}
