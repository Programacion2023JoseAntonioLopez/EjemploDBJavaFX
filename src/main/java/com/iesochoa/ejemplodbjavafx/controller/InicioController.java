package com.iesochoa.ejemplodbjavafx.controller;

import com.iesochoa.ejemplodbjavafx.EjemploDBJavaFx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador para la vista de inicio de la aplicación.
 * Permite la navegación a las secciones de Departamentos y Empleados.
 */
public class InicioController {

    /**
     * Botón para salir de la aplicación.
     */
    @FXML
    private Button btSalir;

    /**
     * Método invocado al hacer clic en el botón para acceder a la gestión de departamentos.
     * Carga y muestra la vista de departamentos en una nueva ventana modal.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickDepartamentos(ActionEvent event) {
        // Es necesario el control de excepciones para manejar posibles errores al cargar la vista.
        try {
            // Cargamos la escena desde el archivo FXML que define la interfaz de departamentos.
            FXMLLoader loader = new FXMLLoader(EjemploDBJavaFx.class.getResource("views/departamentos-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            // Iniciamos un nuevo Stage (ventana) para mostrar la escena de departamentos.
            Stage stage = new Stage();
            stage.setTitle("IES Severos Ochoa/Departamentos"); // Establecemos el título de la ventana.
            stage.setResizable(false); // Deshabilitamos la posibilidad de redimensionar la ventana.
            stage.initModality(Modality.APPLICATION_MODAL); // Establecemos la modalidad de la ventana como modal de aplicación,
            // lo que bloquea la interacción con otras ventanas de la aplicación
            // hasta que esta se cierre.
            stage.setScene(scene); // Asignamos la escena al Stage.
            stage.showAndWait(); // Mostramos la ventana y esperamos a que el usuario la cierre.
        } catch (IOException e) {
            // En caso de error al cargar el archivo FXML, mostramos un mensaje de error en la consola.
            System.err.println(e.getMessage());
        }
    }

    /**
     * Método invocado al hacer clic en el botón para acceder a la gestión de empleados.
     * Carga y muestra la vista de empleados en una nueva ventana modal.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickEmpleados(ActionEvent event) {
        // Es necesario el control de excepciones para manejar posibles errores al cargar la vista.
        try {
            // Cargamos la escena desde el archivo FXML que define la interfaz de empleados.
            FXMLLoader loader = new FXMLLoader(EjemploDBJavaFx.class.getResource("views/empleados-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            // Iniciamos un nuevo Stage (ventana) para mostrar la escena de empleados.
            Stage stage = new Stage();
            stage.setTitle("IES Severos Ochoa/Empleados"); // Establecemos el título de la ventana.
            stage.setResizable(false); // Deshabilitamos la posibilidad de redimensionar la ventana.
            stage.initModality(Modality.APPLICATION_MODAL); // Establecemos la modalidad de la ventana como modal de aplicación,
            // lo que bloquea la interacción con otras ventanas de la aplicación
            // hasta que esta se cierre.
            stage.setScene(scene); // Asignamos la escena al Stage.
            stage.showAndWait(); // Mostramos la ventana y esperamos a que el usuario la cierre.
        } catch (IOException e) {
            // En caso de error al cargar el archivo FXML, mostramos un mensaje de error en la consola.
            System.err.println(e.getMessage());
        }
    }

    /**
     * Método invocado al hacer clic en el botón de salir.
     * Cierra la ventana actual de la aplicación.
     *
     * @param event El evento de acción.
     */
    @FXML
    void onClickSalir(ActionEvent event) {
        // Obtenemos el Stage (ventana) actual a partir del nodo del botón de salir.
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close(); // Cerramos la ventana actual.
    }
}