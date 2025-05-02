package com.iesochoa.ejemplodbjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class EjemploDBJavaFx extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EjemploDBJavaFx.class.getResource("views/inicio-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("IES Severo Ochoa");
        stage.setResizable(false);
        stage.setScene(scene);
        //le ponemos un icono a la ventana
        Image icon = new Image(getClass().getResourceAsStream("images/icon.png"));
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}