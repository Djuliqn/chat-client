package com.chatty.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

@FXMLController
public class MainController {

    @FXML
    private Button openSocket;

    @FXML
    private void openSocketClicked(){
        openSocket.setOnMouseClicked(event ->
                System.out.print("init"));
    }
}
