package com.iesochoa.ejemplodbjavafx.controller;

import com.iesochoa.ejemplodbjavafx.db.DepartamentoDAO;
import com.iesochoa.ejemplodbjavafx.model.Departamento;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DepartamentosController implements Initializable {
    private ObservableList<Departamento> listaDepartamentos;
    private DepartamentoDAO departamentoDAO= DepartamentoDAO.getInstance();

    @FXML
    private ImageView btNuevo;

    @FXML
    private ImageView btNuevo1;

    @FXML
    private ImageView btNuevo11;

    @FXML
    private ImageView btSalir;

    @FXML
    private Button tbAlta;
    @FXML
    private TableView<Departamento> tvDepartamentos;


    @FXML
    private TableColumn<Departamento, Integer> tcCod;

    @FXML
    private TableColumn<Departamento, String> tcNombre;
    @FXML
    private TableColumn<Departamento, String> tcJefe;

    private void iniciaTableViewDepartamentos(){
        /*echo: por terminar*/
        listaDepartamentos= FXCollections.observableArrayList();

        //asociamos las columnas con los datos indicando el nombre del campo de la clase
        tcCod.setCellValueFactory(new PropertyValueFactory("codigo"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcJefe.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(
                        param.getValue().getJefe()!=null?
                                param.getValue().getJefe().getNombreCompleto():
                                "Sin jefe"
                )
        );
        try {
            listaDepartamentos.addAll(departamentoDAO.listAllDepartamentos());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
       tvDepartamentos.setItems(listaDepartamentos);/*
        //asignamos evento de doble click
        tvEmpleados.setOnMouseClicked(event -> {
            System.out.println(event.getClickCount());
            if (event.getClickCount() == 2 ) {
                Empleado empleado = tvEmpleados.getSelectionModel().getSelectedItem();
                abrirEmpleado(empleado);

            }
        });*/
    }
    @FXML
    void onClickBorrar(ActionEvent event) {

    }

    @FXML
    void onClickEditar(ActionEvent event) {

    }

    @FXML
    void onClickSalir(ActionEvent event) {
        //obtenemos el stage actual
        Stage stage = (Stage) tvDepartamentos.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickbtAlta(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaTableViewDepartamentos();
    }
}

