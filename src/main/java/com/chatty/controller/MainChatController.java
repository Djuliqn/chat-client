package com.chatty.controller;

import com.chatty.controller.socket.SockJsJavaClient;
import com.chatty.model.ActiveChat;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@FXMLController
public class MainChatController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private SockJsJavaClient sockJsJavaClient;

    @FXML private Button btnSend;
    @FXML private Button btnAddChat;
    @FXML private TextField messageText;
    @FXML private TextField newChatText;
    @FXML private ScrollPane currentChatPane;
    @FXML private ScrollPane chatsPane;

    private ListView<String> allChatsListView = new ListView<String>();
    private ObservableList<String> allChats = FXCollections.observableArrayList();
    private ListView<String> currentChatListView = new ListView<String>();
    private ObservableList<String> chatMessages = FXCollections.observableArrayList();

    private List<ActiveChat> activeChats = new ArrayList<>();
    private String currentChat;
    
    public void init() {
        sockJsJavaClient.connectClient();
        sockJsJavaClient.subscribeToGroupChat();
        sockJsJavaClient.subscribeToChatNotifications();
        
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				currentChatListView.setItems(chatMessages);
		        currentChatPane.setContent(currentChatListView);
		        
		        allChats.add("Main Chat");
		        currentChat = "Main Chat";
		        allChatsListView.setItems(allChats);
		        allChatsListView.getSelectionModel().selectFirst();
		        allChatsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		            public void changed(ObservableValue<? extends String> ov,
		                    String old_val, String new_val) {
		                  currentChat = new_val;
		                  changeActiveChat();
		                }
		              });
		        chatsPane.setContent(allChatsListView);
		        
		        ActiveChat activeChat = ActiveChat.builder().name("Main Chat").messages(chatMessages).notification(false).build();
		        activeChats.add(activeChat);
			}
    	});
    }

    @PreDestroy
    private void destroy() {
        sockJsJavaClient.disconnectClient();
    }

    @FXML
    public void btnSendAction(ActionEvent event) {
        sockJsJavaClient.send(userSession.getLoggedInUser(), messageText.getText(), currentChat);
        messageText.setText("");
    }
    
    @FXML
    public void btnAddChatAction(ActionEvent event) {
        sockJsJavaClient.createChat(userSession.getLoggedInUser(), newChatText.getText(), currentChat);
        newChatText.setText("");
    }

    public void addToList(String sender, String recipients, LocalDate date, String text) {
    	
    	List<String> chatMembers = new ArrayList<>(Arrays.asList(recipients.trim().split("\\s*,\\s*")));
    	chatMembers = chatMembers.stream().distinct().collect(Collectors.toList());

    	// Overwrite in case this is a main chat message. 
    	final String chatName = recipients.equals("Main Chat") ? recipients : getChatName("", chatMembers);
    	
    	Optional<ActiveChat> selectedChatOptional = activeChats.stream().filter( c -> c.getName().equals(chatName)).findFirst();
    	
    	if (selectedChatOptional.isPresent()) {
    		
    		ActiveChat selectedChat = selectedChatOptional.get();
			String message = sender+"@" +date.toString("dd-MM-yyyy")+ ": " +text;
			selectedChat.setNotification(true);
			
			if (currentChat.equals(chatName)) {
				Platform.runLater(new Runnable() {
			
					@Override
					public void run() {
						selectedChat.getMessages().add(message);
						chatMessages = FXCollections.observableArrayList(selectedChat.getMessages());
						currentChatListView.setItems(chatMessages);
						currentChatPane.setContent(currentChatListView);
					}
				});
			}
			else {
				selectedChat.getMessages().add(message);
			}
    	}
    }
    
    public void addNewChat(String sender, String text) {
    	
    	List<String> chatMembers = new ArrayList<>(Arrays.asList(text.trim().split("\\s*,\\s*")));
    	chatMembers = chatMembers.stream().distinct().collect(Collectors.toList());
    	
    	// Only add the chat for this user if they're in the members list.
    	boolean shouldAddSession = chatMembers.contains(userSession.getLoggedInUser())
    			|| (sender.equals(userSession.getLoggedInUser()) && chatMembers.size() > 0);
    	
    	if (shouldAddSession) {
    		ObservableList<String> newChatMessages = FXCollections.observableArrayList();
    		
    		String chatName = getChatName(sender, chatMembers);
    		
    		// Only proceed if this chat doesn't already exist and the generated name isn't empty.
    		boolean shouldProceed = true;
    		for (ActiveChat chat : activeChats) {
    			if (chat.getName().equals(chatName)) {
    				shouldProceed = false;
    				break;
    			}
    		}
    		
    		shouldProceed = shouldProceed && !chatName.equals("");
    		
    		if (shouldProceed) {
				ActiveChat newActiveChat = ActiveChat.builder().name(chatName).messages(newChatMessages).notification(true).build();
				activeChats.add(newActiveChat);
				
				Platform.runLater(new Runnable() {
		
					@Override
					public void run() {
						allChats.add(chatName);
						allChatsListView.setItems(allChats);
						chatsPane.setContent(allChatsListView);
					}
		    	});
    		}
    	}
    }
    
    private String getChatName(String sender, List<String> chatMembers) {
    	
    	StringBuilder chatNameBuilder = new StringBuilder();
	boolean firstElementFlag = false;
	if (!sender.equals("")) {
		chatNameBuilder.append(sender);
	} else {
		firstElementFlag = true;
	}
	
	for (String chatMember : chatMembers) {
		if (!firstElementFlag) {	
			chatNameBuilder.append(", ");
		} else {
			firstElementFlag = false;
		}
		chatNameBuilder.append(chatMember);
	}

	return chatNameBuilder.toString();
    }
    
    private void changeActiveChat() {
    	
    	for (ActiveChat chat : activeChats) {
    		if (chat.getName().equals(currentChat)) {
    			
    			chatMessages = chat.getMessages();
    			
    			Platform.runLater(new Runnable() {

    				@Override
    				public void run() {
    					currentChatListView.setItems(chatMessages);
    					currentChatPane.setContent(currentChatListView);
    				}
    	    	});
    			break;
    		}
    	}
    }
}
