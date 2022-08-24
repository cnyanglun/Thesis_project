package model;

import controllers.IndexView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.Message;
import util.User;
import util.manageChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConServerThread extends Thread{

    private Socket socket;

    public static User user;
    public ClientConServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object o = ois.readObject();

                if (o instanceof User) {
                    user = (User) o;
                } else if (o instanceof Message) {
                    Message message = (Message) o;
                    if (message.getMesType().equals("common_Message")){
                        String sender = message.getSender();
                        TextArea chat = manageChat.getChat(sender);
//                        System.out.println(message.getCon());
                        chat.appendText(sender + " say: \n");
                        chat.appendText(message.getCon() + "\n" + "\n");

                    } else if (message.getMesType().equals("add_Friend")) {
                        System.out.println(message.getUserInfo().getAccount());
                        String account = message.getUserInfo().getAccount();
                        Label friendId = (Label)IndexView.inquireResult.getChildren().get(1);

                        //To avoid no fx Thread Exception
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //更新JavaFX的主线程的代码放在此处
                                friendId.setText(account);
                            }
                        });

                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
