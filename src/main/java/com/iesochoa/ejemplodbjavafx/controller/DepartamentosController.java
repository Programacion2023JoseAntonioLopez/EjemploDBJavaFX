package com.iesochoa.ejemplodbjavafx.controller;

import com.iesochoa.ejemplodbjavafx.EjemploDBJavaFx;
import com.iesochoa.ejemplodbjavafx.db.DepartamentoDAO;
import com.iesochoa.ejemplodbjavafx.db.EmpleadoDAO;
import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
       tvDepartamentos.setItems(listaDepartamentos);
        //asignamos evento de doble click
        tvDepartamentos.setOnMouseClicked(event -> {
            System.out.println(event.getClickCount());
            if (event.getClickCount() == 2 ) {
                Departamento departamento = tvDepartamentos.getSelectionModel().getSelectedItem();
                abrirDepartamento(departamento);

            }
        });
    }
    @FXML
    void onClickBorrar(ActionEvent event) {
        Departamento departamento=tvDepartamentos.getSelectionModel().getSelectedItem();
        if (departamento!=null){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrar");
            alert.setHeaderText("Borrar Departamento");
            alert.setContentText("¿Está seguro de borrar el departamento "+departamento.getNombre()+"?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    borrarDepartamento(departamento);
                }
            });

        }
    }

    private void borrarDepartamento(Departamento departamento) {
        try {
            departamentoDAO.deleteDepartamento(departamento.getCodigo());

            listaDepartamentos= FXCollections.observableArrayList(
                    departamentoDAO.listAllDepartamentos()
            );
            tvDepartamentos.setItems(listaDepartamentos);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void onClickEditar(ActionEvent event) {
        Departamento departamento = tvDepartamentos.getSelectionModel().getSelectedItem();
        if(departamento!=null)
            abrirDepartamento(departamento);
    }

    @FXML
    void onClickSalir(ActionEvent event) {
        //obtenemos el stage actual
        Stage stage = (Stage) tvDepartamentos.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickbtAlta(ActionEvent event) {
        abrirDepartamento(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaTableViewDepartamentos();
    }
    private void abrirDepartamento(Departamento departamento) {
        //es necesario el control de excepciones
        try{
            //cargamos la escena desde el recurso
            FXMLLoader loader=new FXMLLoader(EjemploDBJavaFx.class.getResource("views/departamento-view.fxml"));
            Parent root=loader.load();

            DepartamentoController departamentoController=loader.getController();
            //si es null será nuevo Socio en otro caso actualización
            departamentoController.initialize(departamento);

            Scene scene=new Scene(root);
            //iniciamos nuevo stage en forma modal con la scene
            Stage stage=new Stage();
            if(departamento==null)
                stage.setTitle("Alta de Departamento");
            else
                stage.setTitle("Departamento: "+departamento.getCodigo()+" "+departamento.getNombre());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tvDepartamentos.getScene().getWindow());
            stage.setResizable(false);
            stage.setOnHidden(event -> {
                //cerramos el stage y actualizamos la lista
                try {
                    tvDepartamentos.setItems(FXCollections.observableArrayList(departamentoDAO.listAllDepartamentos()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            stage.setScene(scene);
            stage.showAndWait();

        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}

