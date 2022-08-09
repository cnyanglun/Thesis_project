package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.MyServer;

public class ServerViewController {

    MyServer server;

    @FXML
    public void startServer(ActionEvent event) {
        server = new MyServer();
    }
    @FXML
    public void turnOffServer(ActionEvent event){

    }
}
