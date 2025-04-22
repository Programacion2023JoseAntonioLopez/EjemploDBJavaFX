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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmpleadosController implements Initializable {
    private ObservableList<Empleado> listaEmpleados;
    @FXML
    private Button btBuscarPorNombre;

    @FXML
    private Button btBuscarPorDNI;

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
    private TextField tfDNI;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaCbDepartamentos();
        iniciaTableViewEmpleados();
    }
    @FXML
    void onClickBuscarPorNombre(ActionEvent event) {
        ArrayList<Empleado> empleados;
        try {
            String nombreBusqueda = tfNombre.getText();
            if (!nombreBusqueda.isEmpty()) {
                empleados = EmpleadoDAO.getInstance().selectEmpleadosPorNombre(nombreBusqueda);
            }else{
                empleados = EmpleadoDAO.getInstance().listAllEmpleados();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        listaEmpleados= FXCollections.observableArrayList(empleados);
        tvEmpleados.setItems(listaEmpleados);
    }

    @FXML
    void onClickBuscarPorDNI(ActionEvent event) {
        ArrayList<Empleado> empleados;
        try {
            String dniBusqueda = tfDNI.getText();
            if (!dniBusqueda.isEmpty()) {
                empleados = EmpleadoDAO.getInstance().selectEmpleadosPorDNI(dniBusqueda);
            }else{
                empleados = EmpleadoDAO.getInstance().listAllEmpleados();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        listaEmpleados= FXCollections.observableArrayList(empleados);
        tvEmpleados.setItems(listaEmpleados);
    }

    @FXML
    void onClickSalir(ActionEvent event) {
        //obtenemos el stage actual
        Stage stage = (Stage) btBuscarPorNombre.getScene().getWindow();
        stage.close();
    }





    private void iniciaCbDepartamentos(){
        ArrayList<Departamento> departamentos = null;
        try {
            departamentos = DepartamentoDAO.getInstance().listAllDepartamentos();
            Departamento todos = new Departamento(0, "Todos",null);
            departamentos.add(0, todos);
            departamentos.forEach(
                    departamento -> cbDepartamento.getItems().add(departamento)
            );
            cbDepartamento.getSelectionModel().select(0);
            cbDepartamento.getSelectionModel().selectedItemProperty().addListener(
                    (observableValue, depAnterior, depSeleccionado) -> {
                        ArrayList<Empleado> empleados;
                        try {
                            if (depSeleccionado.getCodigo() == 0) {
                                empleados= EmpleadoDAO.getInstance().listAllEmpleados();

                            } else {
                                empleados= EmpleadoDAO.getInstance().empleadosDepartamento(depSeleccionado.getCodigo());
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        listaEmpleados= FXCollections.observableArrayList(empleados);
                        tvEmpleados.setItems(listaEmpleados);
                    }

            );
            cbDepartamento.setOnAction(event ->{
                Departamento departamento=cbDepartamento.getValue();
                /* echo: por terminar*/
            });
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
    private void iniciaTableViewEmpleados(){
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
            System.err.println(e.getMessage());
        }
        tvEmpleados.setItems(listaEmpleados);
        //asignamos evento de doble click
        tvEmpleados.setOnMouseClicked(event -> {
            System.out.println(event.getClickCount());
            if (event.getClickCount() == 2 ) {
                Empleado empleado = tvEmpleados.getSelectionModel().getSelectedItem();
                abrirEmpleado(empleado);

            }
        });
    }
    @FXML
    void onClickSeleccion(ActionEvent event) {

    }
    @FXML
    void onClickbtAlta(ActionEvent event) {
        abrirEmpleado(null);
    }
    @FXML
    void onClickEditar(ActionEvent event) {
        Empleado empleado = tvEmpleados.getSelectionModel().getSelectedItem();
        if(empleado!=null)
            abrirEmpleado(empleado);
    }
    @FXML
    void onClickBorrar(ActionEvent event) {

        Empleado empleado=tvEmpleados.getSelectionModel().getSelectedItem();
        if (empleado!=null){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrar Empleado");
            alert.setHeaderText("Borrar Empleado");
            alert.setContentText("¿Está seguro de borrar el empleado "+empleado.getNombre()+" "+empleado.getApellidos()+"?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    borrarEmpleado(empleado);
                }
            });

        }
    }

    private void borrarEmpleado(Empleado empleado) {
        try {
            EmpleadoDAO.getInstance().deleteEmpleado(empleado.getId());

            listaEmpleados= FXCollections.observableArrayList(
                    EmpleadoDAO.getInstance().listAllEmpleados()
            );
            tvEmpleados.setItems(listaEmpleados);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void abrirEmpleado(Empleado empleado) {
        //es necesario el control de excepciones
        try{
            //cargamos la escena desde el recurso
            FXMLLoader loader=new FXMLLoader(EjemploDBJavaFx.class.getResource("views/empleado-view.fxml"));
            Parent root=loader.load();

            EmpleadoController empleadoController=loader.getController();
            //si es null será nuevo Socio en otro caso actualización
            empleadoController.initialize(empleado);

            Scene scene=new Scene(root);
            //iniciamos nuevo stage en forma modal con la scene
            Stage stage=new Stage();
            if(empleado==null)
                stage.setTitle("Alta de Empleado");
            else
                stage.setTitle("Empleado: "+empleado.getNombre()+" "+empleado.getApellidos());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btBuscarPorNombre.getScene().getWindow());
            stage.setResizable(false);
            stage.setOnHidden(event -> {
                //cerramos el stage y actualizamos la lista
                try {
                    tvEmpleados.setItems(FXCollections.observableArrayList(EmpleadoDAO.getInstance().listAllEmpleados()));
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
