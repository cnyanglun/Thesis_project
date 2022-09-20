package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.MyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

public class ServerViewController {
    @Autowired
    MyServer server;
    Thread thread1;

    @FXML
    public void startServer(ActionEvent event) {
        //new a MyServer Object which means open the Server
        server = new MyServer();
        thread1 = new Thread(server);
        thread1.start();

    }
    @FXML
    public void turnOffServer(ActionEvent event){

    }
}
