package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;
import redis.testRedis;

import java.io.IOException;

public class LoginViewController {

    Parent root;
    Stage stage;
    Scene scene;

    @FXML
    private TextField textAccount;

    @FXML
    private PasswordField textPassword;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnLogin;

    testRedis redis;

    @FXML
    void actionLogin(ActionEvent event) throws IOException {
        redis = new testRedis();
        if(textAccount.getText().isEmpty() || textPassword.getText().isEmpty()){
            Logger.info("The information is incomplete!");
        }
        else {
            boolean isValidPassword = redis.accountLogin(textAccount.getText(),textPassword.getText());
            if(isValidPassword){
                root = FXMLLoader.load(getClass().getResource("/JavaFx/IndexView.fxml"));
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else{
                Logger.info("The password is incorrect!");
            }
        }
    }

    @FXML
    void actionRegister(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/JavaFx/registerView.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
