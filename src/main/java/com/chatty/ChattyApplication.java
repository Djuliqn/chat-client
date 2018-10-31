package com.chatty;

import com.chatty.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChattyApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launchApp(ChattyApplication.class, MainView.class, args);
    }
}
