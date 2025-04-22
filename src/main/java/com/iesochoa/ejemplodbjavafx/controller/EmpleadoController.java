package com.iesochoa.ejemplodbjavafx.controller;

import com.iesochoa.ejemplodbjavafx.db.DepartamentoDAO;
import com.iesochoa.ejemplodbjavafx.db.EmpleadoDAO;
import com.iesochoa.ejemplodbjavafx.model.Departamento;
import com.iesochoa.ejemplodbjavafx.model.Empleado;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmpleadoController implements Initializable {
    Empleado empleado;
    EmpleadoDAO empleadoDAO = EmpleadoDAO.getInstance();

    @FXML
    private ImageView btSalir;

    @FXML
    private ImageView btSalir1;

    @FXML
    private ComboBox<Departamento> cbDepartamento;

    @FXML
    private Label lbError;

    @FXML
    private TextField tfApellidos;

    @FXML
    private TextField tfDni;

    @FXML
    private TextField tfEdad;

    @FXML
    private TextField tfNombre;
    @FXML
    private Label lbTitulo;

    @FXML
    void onClickGuardar(ActionEvent event) {
        if (datosCorrectos()) {
            Departamento departamento = cbDepartamento.getValue();
            if (departamento.getCodigo() == 0) {//sin departamento
                departamento = null;
            }

            //Empleado empleado = new Empleado(0, tfDni.getText(), tfNombre.getText(), tfApellidos.getText(), Integer.parseInt(tfEdad.getText()), departamento);
            try {
                //estamos editando
                if(empleado!=null){
                    empleado = new Empleado(empleado.getId(), tfDni.getText(), tfNombre.getText(), tfApellidos.getText(), Integer.parseInt(tfEdad.getText()), departamento);
                    empleadoDAO.updateEmpleado(empleado);
                    cerrar();
                    //nuevo empleado
                }else if (empleadoDAO.selectEmpleadoPorDNI(tfDni.getText()) != null) {//si es nuevo comprobamos si existe
                    lbError.setText("El empleado ya existe");
                } else {//creamo el nuevo empleado
                    empleado = new Empleado(0, tfDni.getText(), tfNombre.getText(), tfApellidos.getText(), Integer.parseInt(tfEdad.getText()), departamento);
                    empleadoDAO.createEmpleado(empleado);
                    cerrar();
                }
            } catch (SQLException e) {
                lbError.setText("Error al crear el empleado");
            }
        }
    }


    private boolean datosCorrectos() {
        Boolean esCorrecto = true;
        if (tfDni.getText().isEmpty() || !tfDni.getText().matches("\\d{8}[A-Z]")) {
            lbError.setText("El DNI no puede estar vacío o es incorrecto");
            esCorrecto = false;
        } else if (tfNombre.getText().isEmpty()) {
            lbError.setText("El nombre no puede estar vacío");
            esCorrecto = false;
        } else if (tfApellidos.getText().isEmpty()) {
            lbError.setText("Los apellidos no pueden estar vacíos");
            esCorrecto = false;
        } else if (tfEdad.getText().isEmpty() || !tfEdad.getText().matches("\\d+")) {
            lbError.setText("La edad no puede estar vacía o es incorrecta");
            esCorrecto = false;
        } else if (cbDepartamento.getValue() == null) {
            lbError.setText("El departamento no puede estar vacío");
            esCorrecto = false;
        }
        return esCorrecto;
    }


    @FXML
    void onClickSalir(ActionEvent event) {
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) tfDni.getScene().getWindow();
        stage.close();
    }

    private void iniciaCbDepartamentos() {
        ArrayList<Departamento> departamentos = null;
        try {
            departamentos = DepartamentoDAO.getInstance().listAllDepartamentos();
            Departamento todos = new Departamento(0, "Sin Departamento", null);
            departamentos.add(0, todos);
            departamentos.forEach(
                    departamento -> cbDepartamento.getItems().add(departamento)
            );
            cbDepartamento.getSelectionModel().select(0);

            cbDepartamento.setOnAction(event -> {
                Departamento departamento = cbDepartamento.getValue();
                /* echo: por terminar*/
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initialize(Empleado empleado){
        iniciaCbDepartamentos();
        this.empleado=empleado;
        iniciaEmpleado(empleado);
        lbError.setText("");
    }

    private void iniciaEmpleado(Empleado empleado) {

        if(empleado==null){
            lbTitulo.setText("Nuevo Empleado");
        }else{
            tfDni.setText(empleado.getDni());
            tfNombre.setText(empleado.getNombre());
            tfApellidos.setText(empleado.getApellidos());
            tfEdad.setText(String.valueOf(empleado.getEdad()));
            cbDepartamento.setValue(empleado.getDepartamento());
            lbTitulo.setText("Editar: "+empleado.getNombre()+" "+empleado.getApellidos());
        }

    }
}

