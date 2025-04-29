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

/**
 * Controlador para la vista de creación y edición de empleados.
 * Implementa la interfaz {@code Initializable} para realizar tareas de inicialización
 * después de que se haya cargado el archivo FXML asociado.
 */
public class EmpleadoController implements Initializable {
    /**
     * El empleado que se está creando o editando en la vista.
     * Puede ser {@code null} si se está creando un nuevo empleado.
     */
    Empleado empleado;
    /**
     * Instancia del Data Access Object para la entidad Empleado,
     * utilizada para interactuar con la base de datos.
     */
    EmpleadoDAO empleadoDAO = EmpleadoDAO.getInstance();

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
     * ComboBox para seleccionar el departamento al que pertenece el empleado.
     */
    @FXML
    private ComboBox<Departamento> cbDepartamento;

    /**
     * Label para mostrar mensajes de error al usuario.
     */
    @FXML
    private Label lbError;

    /**
     * TextField para introducir los apellidos del empleado.
     */
    @FXML
    private TextField tfApellidos;

    /**
     * TextField para introducir el DNI del empleado.
     */
    @FXML
    private TextField tfDni;

    /**
     * TextField para introducir la edad del empleado.
     */
    @FXML
    private TextField tfEdad;

    /**
     * TextField para introducir el nombre del empleado.
     */
    @FXML
    private TextField tfNombre;

    /**
     * Label para mostrar el título de la ventana (Nuevo Empleado o Editar Empleado).
     */
    @FXML
    private Label lbTitulo;

    /**
     * Método invocado al hacer clic en el botón de guardar.
     * Valida los datos introducidos por el usuario y, si son correctos,
     * crea un nuevo empleado o actualiza uno existente en la base de datos.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickGuardar(ActionEvent event) {
        // Verifica si los datos introducidos por el usuario son correctos.
        if (datosCorrectos()) {
            // Obtiene el departamento seleccionado en el ComboBox.
            Departamento departamento = cbDepartamento.getValue();
            // Si el código del departamento es 0, se interpreta como "sin departamento" y se establece a null.
            if (departamento.getCodigo() == 0) {//sin departamento
                departamento = null;
            }

            try {
                // Si se está editando un empleado existente.
                if (empleado != null) {
                    // Crea un nuevo objeto Empleado con los datos actualizados.
                    empleado = new Empleado(empleado.getId(), tfDni.getText(), tfNombre.getText(), tfApellidos.getText(), Integer.parseInt(tfEdad.getText()), departamento);
                    // Actualiza el empleado en la base de datos.
                    empleadoDAO.updateEmpleado(empleado);
                    // Cierra la ventana actual.
                    cerrar();
                    // Si se está creando un nuevo empleado.
                } else if (empleadoDAO.selectEmpleadoPorDNI(tfDni.getText()) != null) {//si es nuevo comprobamos si existe
                    // Si ya existe un empleado con el mismo DNI, muestra un mensaje de error.
                    lbError.setText("El empleado ya existe");
                } else {//creamo el nuevo empleado
                    // Crea un nuevo objeto Empleado.
                    empleado = new Empleado(0, tfDni.getText(), tfNombre.getText(), tfApellidos.getText(), Integer.parseInt(tfEdad.getText()), departamento);
                    // Guarda el nuevo empleado en la base de datos.
                    empleadoDAO.createEmpleado(empleado);
                    // Cierra la ventana actual.
                    cerrar();
                }
            } catch (SQLException e) {
                // Si ocurre un error al crear o actualizar el empleado, muestra un mensaje de error.
                lbError.setText("Error al crear el empleado");
            }
        }
    }

    /**
     * Valida que los datos introducidos por el usuario en los campos de texto
     * y el ComboBox sean correctos y no estén vacíos.
     *
     * @return {@code true} si todos los datos son correctos, {@code false} en caso contrario.
     */
    private boolean datosCorrectos() {
        Boolean esCorrecto = true;
        // Valida que el DNI no esté vacío y cumpla con el formato (8 dígitos seguidos de una letra mayúscula).
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
     * Cierra la ventana actual.
     */
    private void cerrar() {
        Stage stage = (Stage) tfDni.getScene().getWindow();
        stage.close();
    }

    /**
     * Inicializa el ComboBox de departamentos, cargando los departamentos desde la base de datos
     * y añadiendo una opción por defecto de "Sin Departamento".
     */
    private void iniciaCbDepartamentos() {
        ArrayList<Departamento> departamentos = null;
        try {
            departamentos = DepartamentoDAO.getInstance().listAllDepartamentos();
            // Crea y añade una opción "Sin Departamento" al inicio de la lista.
            Departamento todos = new Departamento(0, "Sin Departamento", null);
            departamentos.add(0, todos);
            // Añade cada departamento al ComboBox.
            departamentos.forEach(
                    departamento -> cbDepartamento.getItems().add(departamento)
            );
            // Selecciona la opción "Sin Departamento" por defecto.
            cbDepartamento.getSelectionModel().select(0);

            // Define una acción al seleccionar un departamento (actualmente comentado).
            cbDepartamento.setOnAction(event -> {
                Departamento departamento = cbDepartamento.getValue();
                /* echo: por terminar*/
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Obtiene el empleado que se está gestionando en este controlador.
     *
     * @return El objeto {@link Empleado}.
     */
    public Empleado getEmpleado() {
        return empleado;
    }

    /**
     * Establece el empleado que se va a editar en este controlador
     * y actualiza la interfaz de usuario con sus datos.
     *
     * @param empleado El objeto {@link Empleado} a editar.
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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
     * Método de inicialización específico para cuando se carga un empleado para editar.
     * Inicializa el ComboBox de departamentos, guarda la instancia del empleado a editar
     * y llama al método para mostrar sus datos en la interfaz. También limpia cualquier mensaje de error previo.
     *
     * @param empleado El objeto {@link Empleado} que se va a editar.
     */
    public void initialize(Empleado empleado) {
        // Inicializa el ComboBox con los departamentos.
        iniciaCbDepartamentos();
        // Guarda la instancia del empleado a editar.
        this.empleado = empleado;
        // Muestra los datos del empleado en los campos de texto y el ComboBox.
        iniciaEmpleado();
        // Limpia cualquier mensaje de error que pudiera estar visible.
        lbError.setText("");
    }

    /**
     * Muestra los datos del empleado actual en los campos de texto y selecciona
     * su departamento en el ComboBox. Si el empleado es {@code null},
     * se configura la vista para la creación de un nuevo empleado.
     */
    private void iniciaEmpleado() {
        // Si el empleado es null, se está creando un nuevo empleado.
        if (empleado == null) {
            lbTitulo.setText("Nuevo Empleado");
        } else {
            // Si hay un empleado para editar, se llenan los campos de texto con sus datos.
            tfDni.setText(empleado.getDni());
            tfNombre.setText(empleado.getNombre());
            tfApellidos.setText(empleado.getApellidos());
            tfEdad.setText(String.valueOf(empleado.getEdad()));
            // Selecciona el departamento del empleado en el ComboBox.
            cbDepartamento.setValue(empleado.getDepartamento());
            // Actualiza el título de la ventana para indicar que se está editando un empleado.
            lbTitulo.setText("Editar: " + empleado.getNombre() + " " + empleado.getApellidos());
        }
    }
}