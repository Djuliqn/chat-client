package com.chatty.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@FXMLControllerØ
public class MainController {

    @FXML
    private Button login;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private void openSocketClicked() {
        login.setOnMouseClicked(event -> {
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost("http://localhost:8080/user/login");

                // Request parameters and other properties.
                List<BasicNameValuePair> params = new ArrayList<>(2);
                params.add(new BasicNameValuePair("username", "admin"));
                params.add(new BasicNameValuePair("password", "admin"));
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                //Execute and get the response.
                CloseableHttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print("Login " + username.getText() + " : " + password.getText());
        });
    }

}
