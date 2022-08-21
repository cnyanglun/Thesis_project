package model;

import controllers.IndexView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import util.Message;
import util.User;
import util.manageChat;

import java.awt.*;
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
