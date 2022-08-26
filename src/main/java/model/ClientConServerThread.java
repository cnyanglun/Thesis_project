package model;

import controllers.IndexView;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import util.Message;
import util.User;
import util.tool.manageObject;
import util.tool.thread;

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

    private boolean threadSwitch = true;

    @Override
    public void run() {
        while(threadSwitch){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object o = ois.readObject();

                if (o instanceof User) {
                    user = (User) o;
                } else if (o instanceof Message) {
                    Message message = (Message) o;
                    if (message.getMesType().equals("common_Message")){
                        String sender = message.getSender();
                        TextArea chat = manageObject.getChat(sender);
//                        System.out.println(message.getCon());
                        chat.appendText(sender + " say: \n");
                        chat.appendText(message.getCon() + "\n" + "\n");

                    } else if (message.getMesType().equals("search_Friend")) {
                        System.out.println(message.getUserInfo().getAccount());
                        String account = message.getUserInfo().getAccount();

                        AnchorPane inquireResult = (AnchorPane)manageObject.getObject("inquireResult");
                        Label friendId = (Label) inquireResult.getChildren().get(1);
//                        Label friendId = (Label)IndexView.inquireResult.getChildren().get(1);

                        //To avoid no fx Thread Exception
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //更新JavaFX的主线程的代码放在此处
                                friendId.setText(account);
                            }
                        });

                    } else if (message.getMesType().equals("add_Result")) {
                        IndexView indexView = (IndexView)manageObject.getObject("indexView");
                        Thread indexThread = thread.getThreadByName("JavaFX Application Thread");


//                        IndexView indexView = (IndexView)manageObject.getObject("indexView");

//                        Logger.info(message.getCon());
                        if(message.getCon().equals("OK")){
                            indexView.setIsOk(true);
                        }
                        else if (message.getCon().equals("NO")) {
                            indexView.setIsOk(false);
                        }

                        indexThread.interrupt();


                    } else if (message.getMesType().equals("change_Result")) {
                        IndexView indexView = (IndexView)manageObject.getObject("indexView");
                        Thread indexThread = thread.getThreadByName("JavaFX Application Thread");
                        if(message.getCon().equals("OK")){
                            indexView.setIsOk(true);
                        }else
                            indexView.setIsOk(false);

                        indexThread.interrupt();
                        System.out.println("success to change name");
                    }
                }

                
                
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setThreadSwitch(boolean threadSwitch) {
        this.threadSwitch = threadSwitch;
    }

    public void sendToServer(Message message){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(clientUser.socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
