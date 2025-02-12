


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.controller;

import main.java.academia.MatriculaForm;
import main.java.academia.MatriculaTable;
import main.java.dao.AlunoDAO;
import main.java.dao.MatriculaDAO;
import main.java.dao.ModalidadeDAO;
import main.java.model.Aluno;
import main.java.model.Matricula;
import main.java.model.Modalidade;
import main.java.sqlite.ConexaoSQLiteJDBC;
import main.java.erro.AlunoDAOException;
import main.java.erro.ApagaMsgError;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author POSITIVO
 */
public class FXMLMatriculaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    ApagaMsgError apagaMsg = new ApagaMsgError();
    
    @FXML
    private Label labelErroModalidade;
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btn_add_modalidade;

    @FXML
    private Button btn_close;
    @FXML
    private ChoiceBox<String> choiceBoxModalidade;

    @FXML
    private Button btn_excluir_modalidade;
    @FXML
    private DatePicker data_vigencia;
    @FXML
    private DatePicker data_vencimento;
    @FXML
    private Label label_alunoFone;

    @FXML
    private Label label_alunoId;
    @FXML
    private Label msgErro;
    @FXML
    private Label label_alunoNome;
    @FXML
    private Label matricula_id;
   
    @FXML
    private TextField textfieldTotal;
    @FXML
    private TableView<Modalidade> tableViewModalidade = new TableView<Modalidade>();
    @FXML
    private TableColumn<Modalidade, String> columnNome = new TableColumn<>();
    @FXML
    private TableColumn<Modalidade, Double> columnValor = new TableColumn<>();

    @FXML
    void cancelarAction(ActionEvent event) {
        this.telaClose();
    }

    @FXML
    void closeAction(ActionEvent event) {
        this.telaClose();
    }

    public void telaClose() {
        if (MatriculaForm.getStage() != null) {
            MatriculaForm.closeStage();
        }

    }

    @FXML
    public void preencherLabelAluno(Aluno aluno) {
        label_alunoId.setText(Integer.toString(aluno.getId()));
        label_alunoNome.setText(aluno.getNome());
        label_alunoFone.setText(aluno.getFone());

    }
   
    private LocalDate dataDaquiUmMes(LocalDate dataAtual){
        LocalDate dataUmMes = dataAtual.plusMonths(1);
        //long diferencaEmDias = ChronoUnit.DAYS.between(dataAtual, dataUmMes);
        return dataUmMes;
    }
   
    private void preecheDatas() {
        LocalDate dataAtual = LocalDate.now();
        // Adicione um mês à data atual
        LocalDate dataDaquiUmMes = dataDaquiUmMes(dataAtual);
        // Calcule a diferença em dias entre as duas datas
       // long diferencaEmDias = ChronoUnit.DAYS.between(dataAtual, dataDaquiUmMes);
        data_vigencia.setValue(dataAtual);
        data_vencimento.setValue(dataDaquiUmMes);

    }
    @FXML
    public void preencherCamposMatricula(Matricula matricula){
       
        Aluno aluno = matricula.getAluno();
        this.preencherLabelAluno(aluno);
        matricula_id.setText(Integer.toString(matricula.getId()));
        java.util.Date utilDateVigencia = new java.util.Date(matricula.getDataDeVigencia().getTime());
        java.util.Date utilDateVencimento = new java.util.Date(matricula.getDataVecimento().getTime());
        // Convertendo java.util.Date para java.time.LocalDate
        LocalDate localDateVigencia = utilDateVigencia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        data_vigencia.setValue(localDateVigencia);
         LocalDate localDateVecimento = utilDateVencimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        data_vencimento.setValue( localDateVecimento);
        Double totalValor = 0.0;
        for(Modalidade modalidade : matricula.getModalidades()){
         modalidadeList.add(modalidade);
         totalValor += modalidade.getValor();
          System.out.println("valor soma "+totalValor);
        }
        textfieldTotal.setText(Double.toString(totalValor));
        tableViewModalidade.setItems(modalidadeList);
    }
    private Matricula pegaMatricula() {
        LocalDate data_vig = data_vigencia.getValue();
        LocalDate data_venc = data_vencimento.getValue();
        if(data_venc == null || data_vig == null ||textfieldTotal.getText().isEmpty() ){
          labelErroModalidade.setText("campo vazio");
          apagaMsg.apagaMsg(labelErroModalidade);
            return null;
        }
       
        LocalDate data_inicio = data_venc.plusMonths(1);
        Date dataVig = java.sql.Date.valueOf(data_vig);
        Date dataVenc = java.sql.Date.valueOf(data_venc);
        Date dataInicio = java.sql.Date.valueOf(data_inicio);
        Double valor = Double.parseDouble(textfieldTotal.getText());
        boolean status = true;
      
        Matricula mat = new Matricula();
        int alunoId = Integer.parseInt(label_alunoId.getText());
        if (!matricula_id.getText().isEmpty()) {
            int id = Integer.parseInt(matricula_id.getText());
            
            System.out.println("tem id metodo put");
            try {
                Matricula matricula_bd = Matricula.find(id);
                Aluno aluno = matricula_bd.getAluno();
                mat.setAluno(aluno);
                for (Modalidade modalidade : modalidadeList) {
                    mat.addModalidade(modalidade);
                }
                mat.setId(id);
                mat.setDataDeVigencia(dataVig);
                mat.setDataVecimento(dataVenc);
                mat.setDataInicio(dataInicio);
                mat.setValor(valor);
                mat.setStatus(status);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            try {
                Aluno aluno = new Aluno();
                aluno = Aluno.find(alunoId);
                if(modalidadeList.isEmpty()){
                     msgErro.setText("adicione uma Modalidade ");
                    apagaMsg.apagaMsg(msgErro);
                   
                }else{
                    for (Modalidade modalidade : modalidadeList) {
                        mat.addModalidade(modalidade);
                    }
                    mat.setAluno(aluno);
                    mat.setDataDeVigencia(dataVig);
                    mat.setDataVecimento(dataVenc);
                    mat.setDataInicio(dataInicio);
                    mat.setValor(valor);
                    mat.setStatus(status);
                }
               
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return mat;
        
        
    }

    @FXML
    public boolean salvarAction() throws SQLException {
        boolean result = false;
        Matricula matricula = pegaMatricula();

        /*System.out.println(matricula.getDataDeVigencia());
        System.out.println(matricula.getDataVecimento());
        System.out.println(matricula.getDataFim());
        System.out.println(matricula.getValor());
        System.out.println(matricula.getStatus());
        System.out.println(matricula.getAluno());
        System.out.println(matricula.getModalidades());
        */
        if (matricula == null) {
            msgErro.setText("Error ao matricular null");
            apagaMsg.apagaMsg(msgErro);
        }else if(matricula.getModalidades().isEmpty()){
            msgErro.setText("adicione uma modalidade ");
            apagaMsg.apagaMsg(msgErro);
        } else {
            
            matricula.save();
            Aluno aluno = matricula.getAluno();
            aluno.setStatus(true);
            try {
                aluno.save();  // Atualiza o aluno no banco de dados
                return result = true;
            } catch (SQLException ex) {
                msgErro.setText("Error ao matricular");
                apagaMsg.apagaMsg(msgErro);
                Logger.getLogger(FXMLMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
            }
           // System.out.println(matricula.getAluno().getStatus()+" status");
           // System.out.println(matricula.getAluno().getStatus()+" status");
          
        }

        return result;
    }

    private List<String> getModalidadeNomes() {
        List<String> nomes = new ArrayList<>();

        try {
            List<Modalidade> modalidades;
            modalidades = Modalidade.getAll();
            System.out.println("sucesso" + modalidadeList);
            for (Modalidade modalidade : modalidades) {
                nomes.add(modalidade.getNome());
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nomes;
    }

    private Modalidade selectModalidade(Modalidade modalidade) {
        if (modalidade != null) {
            System.out.println(modalidade.getId());
            return modalidade;
        } else {
            return null;
        }
    }
    @FXML
    private ObservableList<Modalidade> modalidadeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textfieldTotal.setText("0.0");
        preecheDatas();
        // TODO
        tableViewModalidade.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> selectModalidade(newSelection));
        choiceBoxModalidade.getItems().addAll(getModalidadeNomes());
        // Configuring column resizing policy
        tableViewModalidade.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //btn_add_modalidade. 
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        
     data_vigencia.valueProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            // Ajusta para o último dia do mês
            data_vencimento.setValue(this.dataDaquiUmMes(newValue));
        } else {
            // Limpa a data de vencimento se a data de vigência for nula
        data_vencimento.setValue(null);
        }
    });
        btn_add_modalidade.setOnMouseClicked((MouseEvent e) -> {
            String modalidadeSelect = choiceBoxModalidade.getValue();
            if (modalidadeSelect == null) {
                System.out.println("campo vazio " + modalidadeSelect);
            } else {
                try (Connection conn = ConexaoSQLiteJDBC.getConexao()) {
                    ModalidadeDAO modalidadeDAO = new ModalidadeDAO();
                    Modalidade modalidade = (Modalidade)modalidadeDAO.getNome(modalidadeSelect).get();
                    if(modalidadeList.stream().anyMatch(m -> m.getNome().equalsIgnoreCase(modalidade.getNome()))) {
                         msgErro.setText("você ja adicionou " + modalidade.getNome());
                        apagaMsg.apagaMsg(msgErro);
                        System.out.println("ja contem essa modalidade" + modalidade);
                    } else {
                        
                       modalidadeList.add(modalidade);
                    }
                    tableViewModalidade.setItems(modalidadeList);
                    Double totalValor = 0.0;
                    for (Modalidade m : modalidadeList) {
                        totalValor += m.getValor();
                        System.out.println(modalidadeList);
                    }
                    textfieldTotal.setText(Double.toString(totalValor));

                } catch (SQLException ex) {
                    Logger.getLogger(FXMLMatriculaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        btn_excluir_modalidade.setOnMouseClicked((MouseEvent e) -> {
            Modalidade modalidadeSelect = tableViewModalidade.getSelectionModel().getSelectedItem();
            if (modalidadeSelect != null) {
                if (modalidadeList.contains(modalidadeSelect)) {
                    modalidadeList.remove(modalidadeSelect);
                    Double totalValor = Double.parseDouble(textfieldTotal.getText());
                    if(totalValor >0){
                        totalValor -= modalidadeSelect.getValor();
                    }
                    textfieldTotal.setText(Double.toString(totalValor));

                } else {
                    msgErro.setText("error ao excluir " + modalidadeSelect.getNome());
                    apagaMsg.apagaMsg(msgErro);
                    System.out.println("error ao excluir " + modalidadeSelect);
                }
            } else {
                msgErro.setText("selecione uma modalidade");
                apagaMsg.apagaMsg(msgErro);
            }

        });
        btnSalvar.setOnMouseClicked((MouseEvent e) -> {
            try {
                if (salvarAction()) {
                     System.out.println("salva matricula sucesso ");
                    Stage stage = (Stage) btnSalvar.getScene().getWindow(); // Obtém o Stage atual
                    stage.close();
                    
                } else {
                    System.out.println("erro ao salva matricula ");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMatriculaController.class.getName()).log(Level.SEVERE, null, ex);

            } 
        });
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
