package com.chatty.controller;

import org.springframework.stereotype.Component;
import com.chatty.view.MainFrame;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
@Component
public class TestWebsocketClient {

    private final String uri="ws://google.com";
    private Session session;
    private MainFrame clientWindow;

    public TestWebsocketClient(MainFrame cw){
        clientWindow=cw;
        try{
            WebSocketContainer container= ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
            clientWindow.writeServerMessage("Connection opened");

        }catch(Exception ex){
            System.out.println(ex);
            clientWindow.writeServerMessage("Error opening socket!");
        }
    }

    @OnOpen
    public void onOpen(Session session){
        this.session=session;
    }

    @OnMessage
    public void onMessage(String message, Session session){
        clientWindow.writeServerMessage(message);
    }

    public void sendMessage(String message){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException ex) {

        }
    }
}
