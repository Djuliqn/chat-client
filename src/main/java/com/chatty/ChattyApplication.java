package com.chatty;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

@SpringBootApplication
public class ChattyApplication extends Application {

    private ConfigurableApplicationContext context;
    private Parent rootNode;


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(ChattyApplication.class);
        URL resource = getClass().getClassLoader().getResource("fxml/main.fxml");
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(context::getBean);
        rootNode = loader.load();
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Welcome in the chat world!");
        primaryStage.setScene(new Scene(rootNode, 300, 275));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void stop(){
        context.close();
    }
}
