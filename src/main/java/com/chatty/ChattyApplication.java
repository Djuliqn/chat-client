package com.chatty;

import com.chatty.view.LoginView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChattyApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launchApp(ChattyApplication.class, LoginView.class, args);
    }
}
