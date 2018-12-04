package com.chatty.controller;

import org.springframework.stereotype.Component;

@Component
public class UserSession {

    private String loggedInUser;

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }
}
