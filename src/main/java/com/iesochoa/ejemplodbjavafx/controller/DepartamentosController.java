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

/**
 * Controlador para la vista de gestión de departamentos.
 * Implementa la interfaz {@code Initializable} para realizar tareas de inicialización
 * después de que se haya cargado el archivo FXML asociado.
 */
public class DepartamentosController implements Initializable {
    /**
     * Lista observable que contiene los departamentos mostrados en la tabla.
     */
    private ObservableList<Departamento> listaDepartamentos;
    /**
     * Instancia del Data Access Object para la entidad Departamento,
     * utilizada para interactuar con la base de datos.
     */
    private DepartamentoDAO departamentoDAO = DepartamentoDAO.getInstance();

    /**
     * Imagen utilizada como botón para crear un nuevo departamento.
     */
    @FXML
    private ImageView btNuevo;

    /**
     * Otra imagen utilizada como botón para crear un nuevo departamento (posiblemente redundante).
     */
    @FXML
    private ImageView btNuevo1;

    /**
     * Otra imagen utilizada como botón para crear un nuevo departamento (posiblemente redundante).
     */
    @FXML
    private ImageView btNuevo11;

    /**
     * Imagen utilizada como botón para salir de la ventana.
     */
    @FXML
    private ImageView btSalir;

    /**
     * Botón para dar de alta (crear) un nuevo departamento.
     */
    @FXML
    private Button tbAlta;

    /**
     * TableView para mostrar la lista de departamentos.
     */
    @FXML
    private TableView<Departamento> tvDepartamentos;

    /**
     * Columna de la tabla para mostrar el código del departamento.
     */
    @FXML
    private TableColumn<Departamento, Integer> tcCod;

    /**
     * Columna de la tabla para mostrar el nombre del departamento.
     */
    @FXML
    private TableColumn<Departamento, String> tcNombre;

    /**
     * Columna de la tabla para mostrar el nombre completo del jefe del departamento.
     */
    @FXML
    private TableColumn<Departamento, String> tcJefe;

    /**
     * Inicializa la TableView de departamentos, configurando las columnas para mostrar
     * las propiedades de la clase {@link Departamento} y cargando todos los departamentos
     * desde la base de datos al iniciar. También añade un evento de doble clic
     * para abrir la ventana de edición/detalle del departamento seleccionado.
     */
    private void iniciaTableViewDepartamentos() {
        /*echo: por terminar*/
        listaDepartamentos = FXCollections.observableArrayList();

        // Asociamos las columnas con los datos indicando el nombre del campo de la clase.
        tcCod.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        // Para mostrar el nombre del jefe, se utiliza una función lambda
        // para acceder a la propiedad anidada 'jefe' y luego a 'nombreCompleto'.
        // Si el jefe es null, se muestra "Sin jefe".
        tcJefe.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(
                        param.getValue().getJefe() != null ?
                                param.getValue().getJefe().getNombreCompleto() :
                                "Sin jefe"
                )
        );
        try {
            // Carga todos los departamentos desde la base de datos y los añade a la lista observable.
            listaDepartamentos.addAll(departamentoDAO.listAllDepartamentos());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // Asigna la lista observable a la TableView para mostrar los datos.
        tvDepartamentos.setItems(listaDepartamentos);
        // Asigna un evento de doble clic a la TableView.
        tvDepartamentos.setOnMouseClicked(event -> {
            System.out.println(event.getClickCount());
            // Si se hace doble clic en una fila.
            if (event.getClickCount() == 2) {
                // Obtiene el departamento seleccionado en la tabla.
                Departamento departamento = tvDepartamentos.getSelectionModel().getSelectedItem();
                // Abre la ventana para ver/editar los detalles del departamento.
                abrirDepartamento(departamento);
            }
        });
    }

    /**
     * Método invocado al hacer clic en el botón de borrar departamento.
     * Obtiene el departamento seleccionado y muestra una alerta de confirmación
     * antes de proceder con la eliminación.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickBorrar(ActionEvent event) {
        Departamento departamento = tvDepartamentos.getSelectionModel().getSelectedItem();
        if (departamento != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrar");
            alert.setHeaderText("Borrar Departamento");
            alert.setContentText("¿Está seguro de borrar el departamento " + departamento.getNombre() + "?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    borrarDepartamento(departamento);
                }
            });

        }
    }

    /**
     * Realiza la eliminación del departamento seleccionado de la base de datos
     * y actualiza la TableView.
     *
     * @param departamento El departamento a borrar.
     */
    private void borrarDepartamento(Departamento departamento) {
        try {
            departamentoDAO.deleteDepartamento(departamento.getCodigo());

            listaDepartamentos = FXCollections.observableArrayList(
                    departamentoDAO.listAllDepartamentos()
            );
            tvDepartamentos.setItems(listaDepartamentos);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Método invocado al hacer clic en el botón de editar departamento.
     * Obtiene el departamento seleccionado en la tabla y abre la ventana de edición
     * con los datos de ese departamento.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickEditar(ActionEvent event) {
        Departamento departamento = tvDepartamentos.getSelectionModel().getSelectedItem();
        if (departamento != null)
            abrirDepartamento(departamento);
    }

    /**
     * Método invocado al hacer clic en el botón de salir.
     * Cierra la ventana actual.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickSalir(ActionEvent event) {
        // Obtiene el Stage actual a partir del nodo de la TableView.
        Stage stage = (Stage) tvDepartamentos.getScene().getWindow();
        stage.close();
    }

    /**
     * Método invocado al hacer clic en el botón de alta (nuevo departamento).
     * Abre la ventana de edición de departamento con un objeto departamento nulo,
     * indicando que se va a crear un nuevo departamento.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickbtAlta(ActionEvent event) {
        abrirDepartamento(null);
    }

    /**
     * Método llamado para inicializar el controlador después de que su nodo raíz
     * haya sido completamente procesado. Llama al método para inicializar la TableView.
     *
     * @param url            La ubicación utilizada para resolver rutas relativas para el objeto raíz, o {@code null} si la ubicación no se conoce.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz, o {@code null} si el objeto raíz no se localizó.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaTableViewDepartamentos();
    }

    /**
     * Abre la ventana para crear un nuevo departamento o para editar los detalles de un departamento existente.
     * La ventana se carga desde el archivo FXML 'departamento-view.fxml' y se muestra de forma modal.
     * Después de cerrar la ventana, se actualiza la lista de departamentos en la TableView.
     *
     * @param departamento El departamento a editar (puede ser {@code null} para crear un nuevo departamento).
     */
    private void abrirDepartamento(Departamento departamento) {
        try {
            // Carga la escena desde el recurso FXML.
            FXMLLoader loader = new FXMLLoader(EjemploDBJavaFx.class.getResource("views/departamento-view.fxml"));
            Parent root = loader.load();

            // Obtiene el controlador de la ventana de edición de departamento.
            DepartamentoController departamentoController = loader.getController();
            // Inicializa el controlador con el departamento a editar (si es null, se prepara para una alta).
            departamentoController.initialize(departamento);

            Scene scene = new Scene(root);
            // Crea un nuevo Stage para la ventana modal.
            Stage stage = new Stage();
            if (departamento == null)
                stage.setTitle("Alta de Departamento");
            else
                stage.setTitle("Departamento: " + departamento.getCodigo() + " " + departamento.getNombre());
            // Establece la modalidad de la ventana como modal, bloqueando la interacción con la ventana padre.
            stage.initModality(Modality.WINDOW_MODAL);
            // Establece la ventana padre de la ventana modal.
            stage.initOwner(tvDepartamentos.getScene().getWindow());
            stage.setResizable(false);
            // Define un evento que se ejecuta al cerrar la ventana de edición.
            stage.setOnHidden(event -> {
                // Al cerrar la ventana, se actualiza la lista de departamentos en la TableView.
                try {
                    tvDepartamentos.setItems(FXCollections.observableArrayList(departamentoDAO.listAllDepartamentos()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            // Asigna la escena al Stage y muestra la ventana modal, esperando a que se cierre.
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}