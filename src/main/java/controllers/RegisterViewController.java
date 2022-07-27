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
import org.tinylog.Logger;
import redis.testRedis;


import java.awt.*;
import java.io.IOException;
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

    testRedis redis;


    @FXML
    void actionRegister(ActionEvent event) throws IOException, InterruptedException {
        redis = new testRedis(textAccount.getText(),textPassword.getText(),textEmail.getText());
        if(textAccount.getText().isEmpty() || textPassword.getText().isEmpty() || textEmail.getText().isEmpty()){
            Logger.info("The information is incomplete!");
            hint.setText("The information is incomplete!");
        }
        else {
            if(redis.isAccountExist()){
                Logger.info("The account has existed");
                hint1.setText("Invalid!");
            }
            else{
                Logger.info("The account can be used!");
                hint1.setText("Valid!");
                redis.accountRegister();
                goToLogin(event);
            }

        }
//        Logger.info("Success to Register!!!");

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
