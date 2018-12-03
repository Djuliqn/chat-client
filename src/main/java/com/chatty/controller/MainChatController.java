package com.chatty.controller;

import com.chatty.controller.socket.SockJsJavaClient;
import com.chatty.model.MessageRecipient;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;

@FXMLController
public class MainChatController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private SockJsJavaClient sockJsJavaClient;

    @FXML private Button btnSend;
    @FXML private TextField messageText;
    @FXML private ScrollPane allMessagesPane;


    public void init() {
        sockJsJavaClient.connectClient();
        sockJsJavaClient.subscribeClient();
    }

    @PreDestroy
    private void destroy() {
        sockJsJavaClient.disconnectClient();
    }

    @FXML
    public void btnSendAction(ActionEvent event){
        List<MessageRecipient> recipients = Arrays.asList(userSession.getLoggedInUser());
        sockJsJavaClient.send(userSession.getLoggedInUser().getRecipientName(), messageText.getText(), recipients);
    }

    public void addToList(String sender, LocalDate date, String text) {
        allMessagesPane.setContent(new Text(text));
    }
}
