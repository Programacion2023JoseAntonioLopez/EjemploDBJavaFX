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

public class InicioController {
    @FXML
    private Button btSalir;
    @FXML
    void onClickDepartamentos(ActionEvent event) {

    }

    @FXML
    void onClickEmpleados(ActionEvent event) {
//es necesario el control de excepciones
        try{
            //cargamos la escena desde el recurso
            FXMLLoader loader=new FXMLLoader(EjemploDBJavaFx.class.getResource("views/empleados-view.fxml"));

            Parent root=loader.load();

            Scene scene=new Scene(root);
            //iniciamos nuevo stage en forma modal con la scene
            Stage stage=new Stage();
            stage.setTitle("IES Severos Ochoa/Empleados");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void onClickSalir(ActionEvent event) {
        //obtenemos el stage actual
        Stage stage = (Stage) btSalir.getScene().getWindow();
        stage.close();
    }
}