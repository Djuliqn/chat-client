package com.chatty.controller;

import com.chatty.util.HTTPRequestExecutor;
import com.chatty.view.dialog.RegisterDialog;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

@FXMLController
public class MainController {

    @FXML
    private Button login;

    @FXML
    private Button register;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private void openSocketClicked() {
        login.setOnMouseClicked(event -> {
            HTTPRequestExecutor reqExecutor = new HTTPRequestExecutor("localhost", "8080");

            // Request parameters and other properties.
            Map<String,String> params = new HashMap<>(2);
            params.put("username", "admin");
            params.put("password", "admin");

            HttpEntity entity = reqExecutor.executePost("/user/login", params);
        });
    }

    @FXML
    private void registerClicked(){
        register.setOnMouseClicked(event -> {
            new RegisterDialog();
        });
    }

}
