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

/**
 * Controlador para la vista de creación y edición de departamentos.
 * Implementa la interfaz {@code Initializable} para realizar tareas de inicialización
 * después de que se haya cargado el archivo FXML asociado.
 */
public class DepartamentoController implements Initializable {
    /**
     * El departamento que se está creando o editando en la vista.
     * Puede ser {@code null} si se está creando un nuevo departamento.
     */
    private Departamento departamento;
    /**
     * El empleado que se asignará como jefe del departamento.
     */
    private Empleado jefe;

    /**
     * Instancia del Data Access Object para la entidad Departamento,
     * utilizada para interactuar con la base de datos.
     */
    private DepartamentoDAO departamentoDAO = DepartamentoDAO.getInstance();

    /**
     * Imagen utilizada como botón para salir de la ventana.
     */
    @FXML
    private ImageView btSalir;

    /**
     * Otra imagen utilizada como botón para salir de la ventana (posiblemente redundante).
     */
    @FXML
    private ImageView btSalir1;

    /**
     * Label para mostrar el código del departamento.
     */
    @FXML
    private Label lbCodigo;

    /**
     * Label para mostrar mensajes de error al usuario.
     */
    @FXML
    private Label lbError;

    /**
     * Label para mostrar el nombre del jefe del departamento.
     */
    @FXML
    private Label lbJefe;

    /**
     * Label para mostrar el título de la ventana (Nuevo Departamento o Editar Departamento).
     */
    @FXML
    private Label lbTitulo;

    /**
     * TextField para introducir el nombre del departamento.
     */
    @FXML
    private TextField tfNombre;

    /**
     * Método invocado al hacer clic en el botón para buscar y seleccionar un jefe para el departamento.
     * Abre una ventana modal que muestra la lista de empleados y permite seleccionar uno para asignarlo como jefe.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickBuscarJefe(ActionEvent event) {
        // Intenta cargar la vista de empleados para seleccionar un jefe.
        try {
            FXMLLoader loader = new FXMLLoader(EjemploDBJavaFx.class.getResource("views/empleados-view.fxml"));
            Parent root = loader.load();

            // Obtiene el controlador de la vista de empleados.
            EmpleadosController empleadosController = loader.getController();
            // Establece un callback para recibir el empleado seleccionado como jefe.
            empleadosController.setSeleccionEmpleadoCallback(empleado -> {
                //si se ha seleccionar un empleado
                if (empleado != null) {
                    jefe = empleado;
                    lbJefe.setText(jefe.getNombreCompleto()); // Muestra el nombre completo del jefe seleccionado.
                }
            });
            Scene scene = new Scene(root);
            // Crea y muestra una nueva ventana modal para la selección de empleados (jefes).
            Stage stage = new Stage();
            stage.setTitle("Ies Ochoa/Empleados");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la interacción con la ventana padre.
            stage.setScene(scene);
            stage.showAndWait(); // Espera hasta que la ventana de selección de empleados se cierre.

        } catch (IOException e) {
            // Imprime un mensaje de error si no se puede cargar la vista de empleados.
            System.err.println(e.getMessage());
        }
    }

    /**
     * Método invocado al hacer clic en el botón de guardar.
     * Valida que el nombre del departamento no esté vacío y, si es válido,
     * crea un nuevo departamento o actualiza uno existente en la base de datos.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickGuardar(ActionEvent event) {
        // Verifica si los datos introducidos por el usuario son correctos.
        if (datosCorrectos()) {
            try {
                // Si se está editando un departamento existente.
                if (departamento != null) {
                    departamento.setNombre(tfNombre.getText()); // Actualiza el nombre del departamento.
                    departamento.setJefe(jefe); // Actualiza el jefe del departamento.
                    departamentoDAO.updateDepartamento(departamento); // Guarda los cambios en la base de datos.
                    cerrar(); // Cierra la ventana actual.
                } else {// Si se está creando un nuevo departamento.
                    departamento = new Departamento(0, tfNombre.getText(), jefe); // Crea un nuevo objeto Departamento.
                    departamentoDAO.createDepartamento(departamento); // Guarda el nuevo departamento en la base de datos.
                    cerrar(); // Cierra la ventana actual.
                }
            } catch (Exception e) {
                // Muestra un mensaje de error si ocurre algún problema al crear o actualizar el departamento.
                lbError.setText("Error al crear el departamento");
            }
        }
    }

    /**
     * Método invocado al hacer clic en el botón de salir.
     * Cierra la ventana actual.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickSalir(ActionEvent event) {
        cerrar();
    }

    /**
     * Valida que los datos introducidos por el usuario en los campos de texto sean correctos.
     * Actualmente, solo verifica que el nombre del departamento no esté vacío.
     *
     * @return {@code true} si los datos son correctos, {@code false} en caso contrario.
     */
    private boolean datosCorrectos() {
        Boolean esCorrecto = true;
        if (tfNombre.getText().isEmpty()) {
            lbError.setText("El nombre no puede estar vacío");
            esCorrecto = false;
        }
        return esCorrecto;
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrar() {
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Método llamado para inicializar el controlador después de que su nodo raíz
     * haya sido completamente procesado. Actualmente no realiza ninguna acción específica.
     *
     * @param url            La ubicación utilizada para resolver rutas relativas para el objeto raíz, o {@code null} si la ubicación no se conoce.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz, o {@code null} si el objeto raíz no se localizó.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Método de inicialización específico para cuando se carga un departamento para editar.
     * Inicializa la vista con los datos del departamento proporcionado.
     *
     * @param departamento El objeto {@link Departamento} que se va a editar.
     */
    public void initialize(Departamento departamento) {
        this.departamento = departamento;
        iniciaDepartamento(departamento); // Muestra los datos del departamento en la interfaz.
        lbError.setText(""); // Limpia cualquier mensaje de error previo.
    }

    /**
     * Muestra los datos del departamento proporcionado en los campos de la interfaz.
     * Si el departamento es {@code null}, se configura la vista para la creación de un nuevo departamento.
     *
     * @param departamento El objeto {@link Departamento} cuyos datos se van a mostrar.
     */
    private void iniciaDepartamento(Departamento departamento) {
        // Si el departamento es null, se está creando un nuevo departamento.
        if (departamento == null) {
            lbTitulo.setText("Nuevo Departamento");
            lbCodigo.setText("000"); // Muestra un código por defecto para el nuevo departamento.
            lbJefe.setText("Sin Jefe"); // Indica que no se ha seleccionado un jefe.
        } else {
            // Si hay un departamento para editar, se llenan los campos con sus datos.
            lbTitulo.setText("Editar: " + departamento.getNombre());
            lbCodigo.setText(String.valueOf(departamento.getCodigo()));
            tfNombre.setText(departamento.getNombre());
            // Muestra el nombre completo del jefe si el departamento tiene uno asignado.
            if (departamento.getJefe() != null) {
                lbJefe.setText(departamento.getJefe().getNombre() + " " + departamento.getJefe().getApellidos());
            } else {
                lbJefe.setText("Sin Jefe"); // Indica que el departamento no tiene jefe asignado.
            }
        }
    }
}