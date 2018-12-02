package com.chatty.controller;

import com.chatty.util.HTTPRequestExecutor;
import com.chatty.util.HTTPResponseMessageExtractor;
import com.chatty.view.RegisterView;
import com.chatty.view.dialog.RegisterDialog;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@FXMLController
public class LoginController {

    @FXML
    private Button login;
    @FXML
    private Button register;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;

    @FXML
    private void loginBtnClicked() {
        login.setOnMouseClicked(event -> {
            message.setText("");
            HTTPRequestExecutor reqExecutor = new HTTPRequestExecutor("localhost", "8080");

            // Request parameters and other properties.
            Map<String,String> params = new HashMap<>(2);
            params.put("username", username.getText());
            params.put("password", password.getText());

            CloseableHttpResponse entity = reqExecutor.executePost("/user/login", params);
            if(entity.getStatusLine().getStatusCode() != 200){
                message.setText(HTTPResponseMessageExtractor.getResponseErrorMessage(entity));
            }else{
                //TODO redirect to main view
            }

        });
    }

    @FXML
    private void registerBtnClicked(){
        register.setOnMouseClicked(event -> {
            AbstractJavaFxApplicationSupport.showView(RegisterView.class);
        });
    }

}
