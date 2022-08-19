package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.clientUser;
import util.User;

import java.net.URL;
import java.util.ResourceBundle;

public class IndexView implements Initializable{

    @FXML
    private Label accountId;

    @FXML
    private BorderPane chatInterface;

    private Stage stage;

    clientUser clientUser = new clientUser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //get the userInfo from Server
//        System.out.println("ssssss");
//        var userInfo = clientUser.getUserInfo();
//        System.out.println(userInfo.getFriendList());
    }

}
