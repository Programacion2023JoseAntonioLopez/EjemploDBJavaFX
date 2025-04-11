package com.iesochoa.ejemplodbjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class VentanaInicio {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}