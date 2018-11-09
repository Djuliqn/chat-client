package com.chatty.controller;

import com.chatty.util.ParameterStringBuilder;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@FXMLController
public class MainController {

    @FXML
    private Button login;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private void openSocketClicked(){
        login.setOnMouseClicked(event -> {
            try {
                URL url = new URL("http://localhost:8080/spring-security-rest/login");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");

                Map<String, String> parameters = new HashMap<>();
                parameters.put("username", username.getText());
                parameters.put("password", password.getText());

                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
                out.flush();
                out.close();

                con.connect();

                System.out.println(con.getResponseCode());

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print("Login " + username.getText() + " : " + password.getText());
        });
    }
}
