package com.chatty.controller;

import com.chatty.model.MessageRecipient;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    private MessageRecipient loggedInUser;

    public void setLoggedInUser(MessageRecipient loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public MessageRecipient getLoggedInUser() {
        return loggedInUser;
    }
}
