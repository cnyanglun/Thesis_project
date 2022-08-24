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
import model.clientUser;
import org.tinylog.Logger;
import util.User;

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

    User user;
    clientUser clientUser;

    @FXML
    void actionLogin(ActionEvent event) throws IOException {
        user = new User();
        clientUser = new clientUser();
        if(textAccount.getText().isEmpty() || textPassword.getText().isEmpty()){
            Logger.info("The information is incomplete!");
        }
        else {
            user.setAccount(textAccount.getText());
            user.setPassword(textPassword.getText());
            boolean isValidPassword = clientUser.sendLoginInfo(user);
            if(isValidPassword){
                FXMLLoader root = new FXMLLoader(getClass().getResource("/JavaFx/IndexView.fxml"));
//                root = FXMLLoader.load(getClass().getResource("/JavaFx/IndexView.fxml"));
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root.load());
                stage.setScene(scene);
                stage.show();
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
