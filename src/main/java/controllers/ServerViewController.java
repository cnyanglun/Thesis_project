package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.MyServer;

public class ServerViewController {

    MyServer server;
    Thread thread1;

    @FXML
    public void startServer(ActionEvent event) {
        server = new MyServer();
        thread1 = new Thread(server);
        thread1.start();

    }
    @FXML
    public void turnOffServer(ActionEvent event){

    }
}
