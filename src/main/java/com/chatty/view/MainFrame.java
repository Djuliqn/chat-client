package com.chatty.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;


@Component
public class MainFrame {

    @FXML
    private TextField serverTextField;

    @FXML
    private Button openSocket;

    public void writeServerMessage(String message) {
        serverTextField.setText(serverTextField.getText() + "\n" + message);
    }
}
