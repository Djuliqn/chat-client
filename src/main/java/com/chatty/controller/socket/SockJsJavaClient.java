package com.chatty.controller.socket;

import com.chatty.controller.MainChatController;
import com.chatty.model.MessageRecipient;
import com.chatty.model.MessageView;
import com.chatty.model.OutputMessageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



/**
 * Java client to connect and send messages to web socket
 * using stomp protocol.
 *
 */
@Component
public class SockJsJavaClient {

    @Value("${web.socket.connect.url}")
    private String connectUrl;

    @Value("${web.socket.subscribe.url}")
    private String subscribeUrl;

    @Value("${web.socket.send.url}")
    private String sendUrl;

    @Autowired
    private MainChatController mainChatController;

    private StompSessionHandler stompSessionHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(SockJsJavaClient.class);

    private StompSession session;

    /**
     * Connects to the web socket
     */
    public void connectClient() {
        try {
            List<Transport> transports = new ArrayList<>(2);
            transports.add(new WebSocketTransport(new StandardWebSocketClient()));
            transports.add(new RestTemplateXhrTransport());

            SockJsClient sockJsClient = new SockJsClient(transports);

            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            try {
                session = stompClient.connect(connectUrl, getStompSessionHandler()).get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                LOGGER.error("Error in callable while connecting to the web socket. ", e);
            }
        } catch (Exception e) {
            LOGGER.error("Error connecting to the web socket.", e);
        }
    }

    /**
     * Subscribe client to an end point
     *
     * @return StompSession.Subscription to unsubscribe
     *         if wants
     */
    public StompSession.Subscription subscribeClient() {
        return session.subscribe(subscribeUrl, getStompSessionHandler());
    }

    public void send(String from, String message, List<MessageRecipient> recipients) {
        try {
            StompSession.Receiptable receiptable = session.send(sendUrl, MessageView.builder()
                    .username(from).recipients(recipients).text(message).date(LocalDate.now()).build());
            LOGGER.info("Message sent, [id: {}]", receiptable.getReceiptId());
        } catch (Exception e) {
            LOGGER.error("Error while sending message", e);
        }
    }

    private StompSessionHandler getStompSessionHandler() {
        if (stompSessionHandler == null) {
            stompSessionHandler = new StompSessionHandler() {
                @Override
                public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
                    LOGGER.info("Connected to the server. [session id : {}]", stompSession.getSessionId());
                }

                @Override
                public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
                    LOGGER.error("Exception in session handler. [session id : {}, exception : {}]", stompSession.getSessionId(), throwable);
                }

                @Override
                public void handleTransportError(StompSession stompSession, Throwable throwable) {
                    LOGGER.error("Transport error in th session handler. [session id : {}, exception : {}]", stompSession.getSessionId(), throwable);
                }

                @Override
                public Type getPayloadType(StompHeaders stompHeaders) {
                    return OutputMessageView.class;
                }

                @Override
                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    OutputMessageView outputMessage = (OutputMessageView) o;
                    mainChatController.addToList(outputMessage.getSender(), outputMessage.getDate(), outputMessage.getText());
                }

            };
        }
        return stompSessionHandler;
    }

    /**
     * Disconnects from web socket
     */
    public void disconnectClient() {
        try {
            session.disconnect();
            LOGGER.info("Disconnected from web socket. [url: {}]", connectUrl);
        } catch (Exception e) {
            LOGGER.error("Error while disconnecting from the server. ", e);
        }
    }
}