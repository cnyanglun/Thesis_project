package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.clientUser;
import org.tinylog.Logger;
import util.User;
import util.registerInfo;


import java.awt.*;
import java.io.IOException;
import java.lang.management.LockInfo;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {

    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    private Label hint;

    @FXML
    private Label hint1;

    @FXML
    private TextField textAccount;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField textEmail;

    @FXML
    private TextField textCode;

    registerInfo ri;

    clientUser clientUser;


    @FXML
    void actionRegister(ActionEvent event) throws IOException, InterruptedException {
        //Get user input Information
        ri = new registerInfo(textAccount.getText(),textPassword.getText(),textEmail.getText());
        //Connect Server
        clientUser = new clientUser();
        if(textAccount.getText().isEmpty() || textPassword.getText().isEmpty() || textEmail.getText().isEmpty()){
            Logger.info("The information is incomplete!");
        }
        else {
            if (clientUser.sendRegisterInfo(ri)) {
                Logger.info("Success to create A new Account");
            }
            else
                Logger.info("Failed to create a new Account");
        }
    }

    @FXML
    void actionLogin(ActionEvent event) throws IOException {
       goToLogin(event);
    }

    private void goToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/JavaFx/loginView.fxml"));
        stage = (Stage) (((Node)(event.getSource())).getScene().getWindow());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
