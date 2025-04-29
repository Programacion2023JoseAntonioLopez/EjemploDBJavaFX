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


/**
 * Controlador para la vista de gestión de empleados.
 * Implementa la interfaz {@code Initializable} para realizar tareas de inicialización
 * después de que se haya cargado el archivo FXML asociado.
 */
public class EmpleadosController implements Initializable {

    /**
     * Botón para buscar empleados por nombre.
     */
    @FXML
    private Button btBuscarPorNombre;

    /**
     * Botón para buscar empleados por DNI.
     */
    @FXML
    private Button btBuscarPorDNI;

    /**
     * Imagen utilizada como botón para crear un nuevo empleado.
     */
    @FXML
    private ImageView btNuevo;

    /**
     * Imagen utilizada como botón para salir de la ventana.
     */
    @FXML
    private ImageView btSalir;

    /**
     * Botón para seleccionar un empleado y posiblemente devolverlo a otra ventana.
     */
    @FXML
    private Button btSeleccionEmpleado;

    /**
     * ComboBox para filtrar empleados por departamento.
     */
    @FXML
    private ComboBox<Departamento> cbDepartamento;

    /**
     * Botón para dar de baja (eliminar) un empleado.
     */
    @FXML
    private Button tbBaja;
    /**
     * Lista observable que contiene los empleados mostrados en la tabla.
     */
    private ObservableList<Empleado> listaEmpleados;

    /**
     * TableView para mostrar la lista de empleados.
     */
    @FXML
    private TableView<Empleado> tvEmpleados;

    /**
     * Columna de la tabla para mostrar el ID del empleado.
     */
    @FXML
    private TableColumn<Empleado, Integer> tcId;

    /**
     * Columna de la tabla para mostrar el DNI del empleado.
     */
    @FXML
    private TableColumn<Empleado, String> tcDNI;

    /**
     * Columna de la tabla para mostrar el nombre del empleado.
     */
    @FXML
    private TableColumn<Empleado, String> tcNombre;

    /**
     * Columna de la tabla para mostrar los apellidos del empleado.
     */
    @FXML
    private TableColumn<Empleado, String> tcApellidos;

    /**
     * Columna de la tabla para mostrar la edad del empleado.
     */
    @FXML
    private TableColumn<Empleado, Integer> tcEdad;

    /**
     * Columna de la tabla para mostrar el código del departamento del empleado.
     */
    @FXML
    private TableColumn<Empleado, Integer> tcidDep;

    /**
     * Columna de la tabla para mostrar el nombre del departamento del empleado.
     */
    @FXML
    private TableColumn<Empleado, String> tcDepartamento;

    /**
     * TextField para introducir el nombre para la búsqueda.
     */
    @FXML
    private TextField tfNombre;

    /**
     * TextField para introducir el DNI para la búsqueda.
     */
    @FXML
    private TextField tfDNI;

    /**
     * Método llamado para inicializar el controlador después de que su nodo raíz
     * haya sido completamente procesado.
     *
     * @param url            La ubicación utilizada para resolver rutas relativas para el objeto raíz, o {@code null} si la ubicación no se conoce.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz, o {@code null} si el objeto raíz no se localizó.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaTableViewEmpleados(); // Inicializa la TableView de empleados.
        iniciaCbDepartamentos(); // Inicializa el ComboBox de departamentos.
        // Si se ha establecido un callback para la selección de empleados, muestra el botón de selección.
        if (seleccionEmpleadoCallback != null)
            btSeleccionEmpleado.setVisible(true);
        else
            btSeleccionEmpleado.setVisible(false);
    }

    /**
     * Método invocado al hacer clic en el botón de buscar por nombre.
     * Realiza una búsqueda de empleados por el nombre introducido en el TextField.
     * Si el TextField está vacío, muestra todos los empleados.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickBuscarPorNombre(ActionEvent event) {
        ArrayList<Empleado> empleados;
        try {
            String nombreBusqueda = tfNombre.getText();
            if (!nombreBusqueda.isEmpty()) {
                empleados = EmpleadoDAO.getInstance().selectEmpleadosPorNombre(nombreBusqueda);
            } else {
                empleados = EmpleadoDAO.getInstance().listAllEmpleados();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        listaEmpleados = FXCollections.observableArrayList(empleados);
        tvEmpleados.setItems(listaEmpleados);
    }

    /**
     * Método invocado al hacer clic en el botón de buscar por DNI.
     * Realiza una búsqueda de empleados por el DNI introducido en el TextField.
     * Si el TextField está vacío, muestra todos los empleados.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickBuscarPorDNI(ActionEvent event) {
        ArrayList<Empleado> empleados;
        try {
            String dniBusqueda = tfDNI.getText();
            if (!dniBusqueda.isEmpty()) {
                empleados = EmpleadoDAO.getInstance().selectEmpleadosPorDNI(dniBusqueda);
            } else {
                empleados = EmpleadoDAO.getInstance().listAllEmpleados();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        listaEmpleados = FXCollections.observableArrayList(empleados);
        tvEmpleados.setItems(listaEmpleados);
    }

    /**
     * Método invocado al hacer clic en el botón de salir.
     * Cierra la ventana actual.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickSalir(ActionEvent event) {
        // Obtenemos el Stage actual a partir del nodo del botón.
        Stage stage = (Stage) btBuscarPorNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Inicializa el ComboBox de departamentos, cargando los departamentos desde la base de datos
     * y añadiendo un listener para filtrar la tabla de empleados según el departamento seleccionado.
     */
    private void iniciaCbDepartamentos() {
        ArrayList<Departamento> departamentos = null;
        try {
            //Buscamos todos los departamentos
            departamentos = DepartamentoDAO.getInstance().listAllDepartamentos();
            // Añade una opción "Todos" al principio del ComboBox para mostrar todos
            Departamento todos = new Departamento(0, "Todos", null);
            departamentos.add(0, todos);
            // Añade cada departamento al ComboBox.
            departamentos.forEach(
                    departamento -> cbDepartamento.getItems().add(departamento)
            );
            // Selecciona la opción "Todos" por defecto.
            cbDepartamento.getSelectionModel().select(0);
            // Añade un listener para detectar cambios en la selección del ComboBox.
            cbDepartamento.getSelectionModel().selectedItemProperty().addListener(
                    (observableValue, depAnterior, depSeleccionado) -> {
                        ArrayList<Empleado> empleados;
                        try {
                            // Si se selecciona "Todos", carga todos los empleados.
                            if (depSeleccionado.getCodigo() == 0) {
                                empleados = EmpleadoDAO.getInstance().listAllEmpleados();

                            } else {
                                // Si se selecciona un departamento específico, carga los empleados de ese departamento.
                                empleados = EmpleadoDAO.getInstance().empleadosDepartamento(depSeleccionado.getCodigo());
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        // Actualiza la lista observable y la TableView con los empleados filtrados.
                        listaEmpleados = FXCollections.observableArrayList(empleados);
                        tvEmpleados.setItems(listaEmpleados);
                    }

            );
            // Define una acción al seleccionar un departamento (actualmente comentado).
            cbDepartamento.setOnAction(event -> {
                Departamento departamento = cbDepartamento.getValue();
                /* echo: por terminar*/
            });
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * Inicializa la TableView de empleados, configurando las columnas para mostrar
     * las propiedades de la clase {@link Empleado} y cargando todos los empleados
     * desde la base de datos al iniciar. También añade un evento de doble clic
     * para abrir la ventana de edición/detalle del empleado seleccionado.
     */
    private void iniciaTableViewEmpleados() {
        /*echo: por terminar*/
        listaEmpleados = FXCollections.observableArrayList();

        // Asociamos las columnas con los datos indicando el nombre del campo de la clase.
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        tcEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        // Para mostrar el código del departamento, se utiliza una función lambda
        // para acceder a la propiedad anidada 'departamento' y luego a 'codigo'.
        tcidDep.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(
                        param.getValue().getDepartamento() != null ? param.getValue().getDepartamento().getCodigo() : null
                )
        );
        // Para mostrar el nombre del departamento, se utiliza una función lambda
        // para acceder a la propiedad anidada 'departamento' y luego a 'nombre'.
        tcDepartamento.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(
                        param.getValue().getDepartamento() != null ? param.getValue().getDepartamento().getNombre() : "")
        );
        try {
            // Carga todos los empleados desde la base de datos y los añade a la lista observable.
            listaEmpleados.addAll(EmpleadoDAO.getInstance().listAllEmpleados());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // Asigna la lista observable a la TableView para mostrar los datos.
        tvEmpleados.setItems(listaEmpleados);
        // Asigna un evento de doble clic a la TableView.
        tvEmpleados.setOnMouseClicked(event -> {
            System.out.println(event.getClickCount());
            // Si se hace doble clic en una fila.
            if (event.getClickCount() == 2) {
                // Obtiene el empleado seleccionado en la tabla.
                Empleado empleado = tvEmpleados.getSelectionModel().getSelectedItem();
                // Abre la ventana para ver/editar los detalles del empleado.
                abrirEmpleado(empleado);
            }
        });
    }

    /**
     * Método invocado al hacer clic en el botón de alta (nuevo empleado).
     * Abre la ventana de edición de empleado con un objeto empleado nulo,
     * indicando que se va a crear un nuevo empleado.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickbtAlta(ActionEvent event) {
        abrirEmpleado(null);
    }

    /**
     * Método invocado al hacer clic en el botón de editar empleado.
     * Obtiene el empleado seleccionado en la tabla y abre la ventana de edición
     * con los datos de ese empleado.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickEditar(ActionEvent event) {
        Empleado empleado = tvEmpleados.getSelectionModel().getSelectedItem();
        if (empleado != null)
            abrirEmpleado(empleado);
    }

    /**
     * Método invocado al hacer clic en el botón de borrar empleado.
     * Obtiene el empleado seleccionado y muestra una alerta de confirmación
     * antes de proceder con la eliminación.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickBorrar(ActionEvent event) {
        //obtenemos el empleado seleccionado
        Empleado empleado = tvEmpleados.getSelectionModel().getSelectedItem();
        if (empleado != null) {
            //Abrimos un cuadro de diálogo para que confirme si borra o no
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrar Empleado");
            alert.setHeaderText("Borrar Empleado");
            alert.setContentText("¿Está seguro de borrar el empleado " + empleado.getNombre() + " " + empleado.getApellidos() + "?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    borrarEmpleado(empleado);
                }
            });

        }
    }

    /**
     * Realiza la eliminación del empleado seleccionado de la base de datos
     * y actualiza la TableView.
     *
     * @param empleado El empleado a borrar.
     */
    private void borrarEmpleado(Empleado empleado) {
        try {
            EmpleadoDAO.getInstance().deleteEmpleado(empleado.getId());

            listaEmpleados = FXCollections.observableArrayList(
                    EmpleadoDAO.getInstance().listAllEmpleados()
            );
            tvEmpleados.setItems(listaEmpleados);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Abre la ventana para crear un nuevo empleado o para editar los detalles de un empleado existente.
     * La ventana se carga desde el archivo FXML 'empleado-view.fxml' y se muestra de forma modal.
     * Después de cerrar la ventana, se actualiza la lista de empleados en la TableView.
     *
     * @param empleado El empleado a editar (puede ser {@code null} para crear un nuevo empleado).
     */
    private void abrirEmpleado(Empleado empleado) {
        try {
            // Carga la escena desde el recurso FXML.
            FXMLLoader loader = new FXMLLoader(EjemploDBJavaFx.class.getResource("views/empleado-view.fxml"));
            Parent root = loader.load();

            // Obtiene el controlador de la ventana de edición de empleado.
            EmpleadoController empleadoController = loader.getController();
            // Inicializa el controlador con el empleado a editar (si es null, se prepara para una alta).
            empleadoController.initialize(empleado);

            Scene scene = new Scene(root);
            // Crea un nuevo Stage para la ventana modal.
            Stage stage = new Stage();
            if (empleado == null)
                stage.setTitle("Alta de Empleado");
            else
                stage.setTitle("Empleado: " + empleado.getNombre() + " " + empleado.getApellidos());
            // Establece la modalidad de la ventana como modal, bloqueando la interacción con la ventana padre.
            stage.initModality(Modality.WINDOW_MODAL);
            // Establece la ventana padre de la ventana modal.
            stage.initOwner(btBuscarPorNombre.getScene().getWindow());
            stage.setResizable(false);
            // Define un evento que se ejecuta al cerrar la ventana de edición.
            stage.setOnHidden(event -> {
                // Al cerrar la ventana, se actualiza la lista de empleados en la TableView.
                try {
                    tvEmpleados.setItems(FXCollections.observableArrayList(EmpleadoDAO.getInstance().listAllEmpleados()));
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

    /**
     * Interfaz funcional para permitir la devolución de un empleado seleccionado
     * a otra ventana que haya registrado un callback con este controlador.
     */
    public interface SeleccionEmpleadoCallback {
        /**
         * Método llamado cuando un empleado es seleccionado.
         *
         * @param empleado El empleado seleccionado.
         */
        void onEmpleadoSelected(Empleado empleado);
    }

    /**
     * Instancia del callback para la selección de empleados.
     */
    private SeleccionEmpleadoCallback seleccionEmpleadoCallback;

    /**
     * Establece el callback para la selección de empleados. Si se establece un callback,
     * el botón de selección de empleado se hace visible.
     *
     * @param seleccionEmpleadoCallback El callback a ejecutar cuando se selecciona un empleado.
     */
    public void setSeleccionEmpleadoCallback(SeleccionEmpleadoCallback seleccionEmpleadoCallback) {
        this.seleccionEmpleadoCallback = seleccionEmpleadoCallback;
        //mostramos el botón que permite mostrar el botón de selección
        btSeleccionEmpleado.setVisible(true);
    }

    /**
     * Método invocado al hacer clic en el botón de selección de empleado.
     * Obtiene el empleado seleccionado en la tabla y, si hay un callback registrado,
     * llama al método del callback con el empleado seleccionado y cierra la ventana actual.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickSeleccion(ActionEvent event) {
        Empleado empleado=tvEmpleados.getSelectionModel().getSelectedItem();
        if (empleado!=null){
            if (seleccionEmpleadoCallback!=null){
                //devolvemos el empleado seleccionado
                seleccionEmpleadoCallback.onEmpleadoSelected(empleado);
            }
            //cerramos la ventana
            Stage stage=(Stage) tvEmpleados.getScene().getWindow();
            stage.close();
        }
    }
}
