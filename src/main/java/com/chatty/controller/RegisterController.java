package com.chatty.controller;

import com.chatty.util.HTTPRequestExecutor;
import com.chatty.util.HTTPResponseMessageExtractor;
import com.chatty.view.LoginView;
import com.chatty.view.exception.FieldValidationException;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FXMLController
public class RegisterController {

    @Value("${server.host}")
    private String host;

    public static final int FIELD_MIN_LENGHT = 4;

    @FXML private Button register;
    @FXML private Label errorMessage;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;

    @FXML
    private void btnBackAction(ActionEvent event){
        redirectToLoginView();
    }

    private void redirectToLoginView() {
        AbstractJavaFxApplicationSupport.showView(LoginView.class);
    }

    @FXML
    private void btnRegisterAction(){
        register.setOnMouseClicked(event -> {
            try {
                validateFields(Arrays.asList(username, password, confirmPassword));
                // The conditions are not fulfilled so we consume the event
                // to prevent the dialog to close
            } catch (FieldValidationException e) {
                errorMessage.setText(e.getMessage());
                errorMessage.setVisible(true);
                event.consume();
                return;
            }
            Map<String, String> data = getParameterMap(Arrays.asList(username, password, confirmPassword));
            HTTPRequestExecutor reqExecutor = new HTTPRequestExecutor(host, "80");
            CloseableHttpResponse entity = reqExecutor.executeJSONPost("/api/user/register", data);

            if(entity.getStatusLine().getStatusCode() != 200){
                errorMessage.setText(HTTPResponseMessageExtractor.getResponseErrorMessage(entity));
                event.consume();
            }else{
               redirectToLoginView();
            }
        });
    }

    private void validateFields(List<TextField> fields) throws FieldValidationException {
        for (TextField f : fields) {
            if (f.getText().trim().length() < FIELD_MIN_LENGHT)
                throw new FieldValidationException(f.getId() + " is less than " + FIELD_MIN_LENGHT + " symbols!");
        }
    }

    private Map<String, String> getParameterMap(List<TextField> fields) {
        Map<String, String> data = new HashMap<>();
        fields.forEach(f -> data.put(f.getId(), f.getText()));
        return data;
    }
}
