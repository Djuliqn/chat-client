package com.chatty.view.dialog;


import com.chatty.util.HTTPRequestExecutor;
import com.chatty.view.exception.FieldValidationException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.*;

public class RegisterDialog extends Dialog {

    public static final int FIELD_MIN_LENGHT = 4;

    private TextField username;
    private PasswordField password;
    private PasswordField rePassword;
    private Label validationErrors;
    private ButtonType register;

    public RegisterDialog() {
        this.setTitle("Register form");
        this.initStyle(StageStyle.UTILITY);
        initComponents();

        final Button btRegister = (Button) this.getDialogPane().lookupButton(register);
        attachValidationListener(btRegister);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == register) {
                Map<String, String> data = getParameterMap(Arrays.asList(username, password, rePassword));
                HTTPRequestExecutor reqExecutor = new HTTPRequestExecutor("localhost", "8080");
                CloseableHttpResponse entity = reqExecutor.executePost("/user/register", data);
                return entity;
            }
            return null;
        });

        Optional<HttpEntity> result = this.showAndWait();

        result.ifPresent(httpEntity -> {
            System.out.println("Registered");
        });
    }

    private void initComponents() {
        // Set the button types.
        register = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(register, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        username = new TextField();
        username.setId("username");
        username.setPromptText("Username");

        password = new PasswordField();
        password.setId("password");
        password.setPromptText("Password");

        rePassword = new PasswordField();
        rePassword.setId("confirmPassword");
        rePassword.setPromptText("Retype Password");

        validationErrors = new Label();
        validationErrors.setPrefWidth(200);
        validationErrors.setId("validationErrors");
        validationErrors.setVisible(false);

        grid.add(username, 0, 0);
        grid.add(password, 0, 1);
        grid.add(rePassword, 0, 2);
        grid.add(validationErrors, 0, 3, 2, 1);


        this.getDialogPane().setContent(grid);
    }

    private void attachValidationListener(Button btRegister) {
        btRegister.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    // Check whether some conditions are fulfilled
                    try {
                        validateFields(Arrays.asList(username, password, rePassword));
                        // The conditions are not fulfilled so we consume the event
                        // to prevent the dialog to close
                    } catch (FieldValidationException e) {
                        validationErrors.setText(e.getMessage());
                        validationErrors.setVisible(true);
                        event.consume();
                    }
                }
        );
    }

    private Map<String, String> getParameterMap(List<TextField> fields) {
        Map<String, String> data = new HashMap<>();
        fields.forEach(f -> data.put(f.getId(), f.getText()));
        return data;
    }

    private void validateFields(List<TextField> fields) throws FieldValidationException {
        for (TextField f : fields) {
            if (f.getText().trim().length() < FIELD_MIN_LENGHT)
                throw new FieldValidationException(f.getId() + " is less than " + FIELD_MIN_LENGHT + " symbols!");
        }
    }
}
