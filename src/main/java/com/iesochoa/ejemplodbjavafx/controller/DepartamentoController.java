package com.iesochoa.ejemplodbjavafx.controller;

import com.iesochoa.ejemplodbjavafx.EjemploDBJavaFx;
import com.iesochoa.ejemplodbjavafx.db.DepartamentoDAO;
import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DepartamentoController implements Initializable {
    private Departamento departamento;
    private Empleado jefe;

    private DepartamentoDAO departamentoDAO = DepartamentoDAO.getInstance();
    @FXML
    private ImageView btSalir;

    @FXML
    private ImageView btSalir1;

    @FXML
    private Label lbCodigo;

    @FXML
    private Label lbError;

    @FXML
    private Label lbJefe;

    @FXML
    private Label lbTitulo;

    @FXML
    private TextField tfNombre;

    @FXML
    void onClickBuscarJefe(ActionEvent event) {
        //cargamos la escena desde el recurso
        try {
            FXMLLoader loader = new FXMLLoader(EjemploDBJavaFx.class.getResource("views/empleados-view.fxml"));
            // FXMLLoader loader=new FXMLLoader(BibliotecaApplication.class.getResource("views/hello-view.fxml"));
            Parent root = null;

            root = loader.load();

            EmpleadosController empleadosController = loader.getController();
            empleadosController.setSeleccionEmpleadoCallback(empleado -> {
                if (empleado != null) {
                    jefe = empleado;
                    lbJefe.setText(jefe.getNombreCompleto());
                }
            });
            Scene scene = new Scene(root);
            //iniciamos nuevo stage en forma modal con la scene
            Stage stage = new Stage();
            stage.setTitle("Ies Ochoa/Empleados");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (
                IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void onClickGuardar(ActionEvent event) {
        if (datosCorrectos()) {
            try {//editando
                if (departamento != null) {
                    departamento.setNombre(tfNombre.getText());
                    departamento.setJefe(jefe);
                    departamentoDAO.updateDepartamento(departamento);
                    cerrar();
                } else {//nuevo
                    departamento = new Departamento(0, tfNombre.getText(), jefe);
                    departamentoDAO.createDepartamento(departamento);
                    cerrar();
                }
            } catch (Exception e) {
                lbError.setText("Error al crear el departamento");
            }
        }
    }

    @FXML
    void onClickSalir(ActionEvent event) {
        cerrar();
    }

    private boolean datosCorrectos() {
        Boolean esCorrecto = true;
        if (tfNombre.getText().isEmpty()) {
            lbError.setText("El nombre no puede estar vacío");
            esCorrecto = false;
        }
        return esCorrecto;
    }

    private void cerrar() {
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initialize(Departamento departamento) {

        this.departamento = departamento;
        iniciaDepartamento(departamento);
        lbError.setText("");
    }

    private void iniciaDepartamento(Departamento departamento) {
        //si es nuevo departamento
        if (departamento == null) {
            lbTitulo.setText("Nuevo Departamento");
            lbCodigo.setText("000");
            lbJefe.setText("Sin Jefe");
        } else {
            lbTitulo.setText("Editar: " + departamento.getNombre());
            lbCodigo.setText(String.valueOf(departamento.getCodigo()));
            tfNombre.setText(departamento.getNombre());
            if (departamento.getJefe() != null) {
                lbJefe.setText(departamento.getJefe().getNombre() + " " + departamento.getJefe().getApellidos());
            } else {
                lbJefe.setText("Sin Jefe");
            }
        }
    }
}

