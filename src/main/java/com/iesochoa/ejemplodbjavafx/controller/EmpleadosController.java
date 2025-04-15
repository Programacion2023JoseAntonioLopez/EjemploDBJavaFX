package com.iesochoa.ejemplodbjavafx.controller;

import com.iesochoa.ejemplodbjavafx.db.DepartamentoDAO;
import com.iesochoa.ejemplodbjavafx.db.EmpleadoDAO;
import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmpleadosController implements Initializable {
    private ObservableList<Empleado> listaEmpleados;
    @FXML
    private Button btBuscarPorNombre;

    @FXML
    private Button btBuscarPorSocio;

    @FXML
    private ImageView btNuevo;

    @FXML
    private ImageView btSalir;

    @FXML
    private Button btSeleccionEmpleado;
    @FXML
    private ComboBox<Departamento> cbDepartamento;

    @FXML
    private Button tbBaja;

//TableView
    @FXML
    private TableView<Empleado> tvEmpleados;
    @FXML
    private TableColumn<Empleado, Integer> tcId;
    @FXML
    private TableColumn<Empleado, String> tcDNI;
    @FXML
    private TableColumn<Empleado, String> tcNombre;
    @FXML
    private TableColumn<Empleado, String> tcApellidos;
    @FXML
    private TableColumn<Empleado, Integer> tcEdad;
    @FXML
    private TableColumn<Empleado, Integer> tcidDep;
    @FXML
    private TableColumn<Empleado, String> tcDepartamento;









    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfNumSocio;



    @FXML
    void onClickBuscarPorNombre(ActionEvent event) {

    }

    @FXML
    void onClickBuscarPorSocio(ActionEvent event) {

    }

    @FXML
    void onClickSalir(ActionEvent event) {

    }

    @FXML
    void onClickSeleccionSocio(ActionEvent event) {

    }

    @FXML
    void onClickbtAlta(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaCbDepartamentos();
        iniciaTvEmpleados();
    }
    private void iniciaCbDepartamentos(){
        ArrayList<Departamento> departamentos = DepartamentoDAO.getInstance().listAllDepartamentos();
        Departamento todos = new Departamento(0, "Todos",null);
        departamentos.add(0, todos);
        departamentos.forEach(
                departamento -> cbDepartamento.getItems().add(departamento)
        );
        cbDepartamento.getSelectionModel().select(0);
        cbDepartamento.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, departamento, t1) -> {
                    if (t1.getCodigo() == 0) {
                       // btSeleccionEmpleado.setDisable(true);
                    } else {
                      //  btSeleccionEmpleado.setDisable(false);
                    }
                }
        );
        cbDepartamento.setOnAction(event ->{
            Departamento departamento=cbDepartamento.getValue();
            /* echo: por terminar*/
        });
    }
    private void iniciaTvEmpleados(){
        /*echo: por terminar*/
        listaEmpleados= FXCollections.observableArrayList();

        //asociamos las columnas con los datos indicando el nombre del campo de la clase
        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcDNI.setCellValueFactory(new PropertyValueFactory("dni"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcApellidos.setCellValueFactory(new PropertyValueFactory("apellidos"));
        tcEdad.setCellValueFactory(new PropertyValueFactory("edad"));
        tcidDep.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue().getDepartamento().getCodigo())
        );
        tcDepartamento.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue().getDepartamento().getNombre())
        );
        try {
            listaEmpleados.addAll(EmpleadoDAO.getInstance().listAllEmpleados());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tvEmpleados.setItems(listaEmpleados);


    }
}
