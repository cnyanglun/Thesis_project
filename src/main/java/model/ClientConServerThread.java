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
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientConServerThread extends Thread{

    private Socket socket;

    public static User user;

    public ClientConServerThread(Socket socket) {
        this.socket = socket;
    }

    private int count = 0;

    @Override
    public void run() {
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object o = ois.readObject();

                if (o instanceof User) {
                    user = (User) o;
                    Thread indexThread = thread.getThreadByName("JavaFX Application Thread");
                    indexThread.interrupt();
                } else if (o instanceof Message) {
                    Message message = (Message) o;
                    if (message.getMesType().equals("common_Message")){
                        String sender = message.getSender();
                        TextArea chat = manageObject.getChat(sender);
                        chat.appendText(sender + " say: \n");
                        chat.appendText(message.getCon() + "\n" + "\n");

                        Label unread = manageObject.getLabel(message.getSender());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                String text = unread.getText();
                                if(text.equals(" "))
                                    unread.setText("1");
                                else {
                                    int num = Integer.parseInt(text) + 1;
                                    unread.setText(String.valueOf(num));
                                }
                            }
                        });

                    } else if (message.getMesType().equals("search_Friend")) {
                        System.out.println(message.getUserInfo().getAccount());
                        String account = message.getUserInfo().getAccount();

                        AnchorPane inquireResult = (AnchorPane)manageObject.getObject("inquireResult");
                        Label friendId = (Label) inquireResult.getChildren().get(1);

                        //To avoid no fx Thread Exception
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                friendId.setText(account);
                            }
                        });

                    } else if (message.getMesType().equals("add_Result")) {
                        IndexView indexView = (IndexView)manageObject.getObject("indexView");
                        Thread indexThread = thread.getThreadByName("JavaFX Application Thread");

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
                    } else if (message.getMesType().equals("change_Avatar_Result")) {
                        IndexView indexView = (IndexView)manageObject.getObject("indexView");
                        Thread indexThread = thread.getThreadByName("JavaFX Application Thread");
                        if(message.getCon().equals("OK")){
                            indexView.setIsOk(true);
                        }else
                            indexView.setIsOk(false);
                        indexThread.interrupt();
                        System.out.println("success to change Avatar");

                    } else if (message.getMesType().equals("group_message")){
                        String con = message.getCon();
                        String sender = message.getSender();
                        String name = message.getGetter();
                        System.out.println(con + sender + name);

                        TextArea chat = manageObject.getChat(name);
                        chat.appendText(sender + " say: \n");
                        chat.appendText(con + "\n" + "\n");

                        Label unread = manageObject.getLabel(message.getGroup().getGroupName());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                String text = unread.getText();
                                if(text.equals(" "))
                                    unread.setText("1");
                                else {
                                    int num = Integer.parseInt(text) + 1;
                                    unread.setText(String.valueOf(num));
                                }
                            }
                        });

                    }
                }

                
                
            } catch (SocketException e){
                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
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
