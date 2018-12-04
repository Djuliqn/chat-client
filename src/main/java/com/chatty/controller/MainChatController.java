package com.chatty.controller;

import com.chatty.controller.socket.SockJsJavaClient;
import com.chatty.model.MessageRecipient;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
    @FXML private Button btnAddChat;
    @FXML private TextField messageText;
    @FXML private ScrollPane currentChatPane;
    @FXML private ScrollPane chatsPane;

    private ListView<String> allChatsListView = new ListView<String>();
    private ObservableList<String> allChats = FXCollections.observableArrayList();
    private ListView<String> currentChatListView = new ListView<String>();
    private ObservableList<String> chatMessages = FXCollections.observableArrayList();

    public void init() {
        sockJsJavaClient.connectClient();
        sockJsJavaClient.subscribeClient();
        
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				currentChatListView.setItems(chatMessages);
		        currentChatPane.setContent(currentChatListView);
		        
		        allChats.add("Main Chat");
		        allChatsListView.setItems(allChats);
		        chatsPane.setContent(allChatsListView);
			}
    	});
    }

    @PreDestroy
    private void destroy() {
        sockJsJavaClient.disconnectClient();
    }

    @FXML
    public void btnSendAction(ActionEvent event){
        List<MessageRecipient> recipients = Arrays.asList(userSession.getLoggedInUser());
        sockJsJavaClient.send(userSession.getLoggedInUser().getRecipientName(), messageText.getText(), recipients);
        messageText.setText("");
    }
    
    @FXML
    public void btnAddChatAction(ActionEvent event){
        //TODO: add functionality to add a new chat
    }

    public void addToList(String sender, LocalDate date, String text) {
    	Platform.runLater(new Runnable() {

			@Override
			public void run() {
				chatMessages.add(sender+"@" +date.toString("dd-MM-yyyy")+ ": " +text);
				currentChatListView.setItems(chatMessages);
				currentChatPane.setContent(currentChatListView);
			}
    	});
    }
}
