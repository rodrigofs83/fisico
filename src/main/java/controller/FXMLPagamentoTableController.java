/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.academia.PagamentoTable;
import main.java.dao.MatriculaDAO;
import main.java.model.Matricula;
import main.java.model.Pagamento;

/**
 * FXML Controller class
 *
 * @author POSITIVO
 */
public class FXMLPagamentoTableController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btn_Closer;

    @FXML
    private Button btn_close;

    @FXML
    private Button btn_home;

    @FXML
    private TableColumn<Pagamento, Date> columnDataPg = new TableColumn<>();

    @FXML
    private TableColumn<Pagamento, String> columnFormaPg = new TableColumn<>();

    @FXML
    private TableColumn<Pagamento, Integer> columnId = new TableColumn<>();

    @FXML
    private TableColumn<Pagamento, Matricula> columnNome = new TableColumn<>();

    @FXML
    private TableColumn<Pagamento, Double> columnValorPg = new TableColumn<>();

    @FXML
    private Label lbAluno;

    @FXML
    private TableView<Pagamento> tvPagamento = new TableView<Pagamento>();

    private ObservableList<Pagamento> pagamentoList = FXCollections.observableArrayList();

    @FXML
    void closeAction(ActionEvent event) {
        this.telaClose();

    }

    public void telaClose() {
        if (PagamentoTable.getStage() != null) {
            PagamentoTable.getStage().close();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tvPagamento.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));

        columnNome.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        columnNome.setCellFactory(column -> {
            TableCell<Pagamento, Matricula> cell = new TableCell<Pagamento, Matricula>() {
                @Override
                protected void updateItem(Matricula item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {

                        if (item.getAluno() != null) { // Check if Aluno is not null
                            setText(item.getAluno().getNome());
                        } else {
                            setText("Aluno não encontrado"); // Or some other default text
                        }
                    }
                }
            };
            return cell;

        });
        columnValorPg.setCellValueFactory(new PropertyValueFactory<>("valorPg"));
        columnFormaPg.setCellValueFactory(new PropertyValueFactory<>("formaPg"));
        columnDataPg.setCellValueFactory(new PropertyValueFactory<>("dataPg"));
        columnDataPg.setCellFactory(column -> {
            TableCell<Pagamento, Date> cell = new TableCell<Pagamento, Date>() {
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
            List<Pagamento> pagamentos = Pagamento.getAll();
            if (pagamentos.isEmpty()) {
                System.out.println("Nenhum pagamento encontrado!");
            } else {
                for (Pagamento p : pagamentos) {
                    System.out.println("ID: " + p.getId() + " valor = " + p.getValorPg());
                }
            }

            pagamentoList.addAll(pagamentos);
            tvPagamento.setItems(pagamentoList);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLPagamentoTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
