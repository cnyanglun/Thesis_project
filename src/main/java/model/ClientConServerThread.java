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
    // Accept messages from the server.
    public void run() {
        while(true){
            try {
                // Get initialization information from the server.
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object o = ois.readObject();

                /* This is initialization information , it contains name , avatar ,friendList and so on.
                * Initialization information is a user object.
                * */
                if (o instanceof User) {
                    user = (User) o;
                    Thread indexThread = thread.getThreadByName("JavaFX Application Thread");
                    indexThread.interrupt();
                } else if (o instanceof Message) {
                    Message message = (Message) o;
                    //Normal message type.
                    if (message.getMesType().equals("common_Message")){
                        String sender = message.getSender();

                        //Put the message into the corresponding message box (TextArea).
                        TextArea chat = manageObject.getChat(sender);
                        chat.appendText(sender + " say: \n");
                        chat.appendText(message.getCon() + "\n" + "\n");

                        // Display unread message.
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
                        //Search Friend type.
                        System.out.println(message.getUserInfo().getAccount());
                        String account = message.getUserInfo().getAccount();

                        //put the inquireResult into the corresponding AnchorPane.
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
                        //Add friend result type, Server will return a message which contain whether successful adding friend.
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
                        //Change name type.
                        IndexView indexView = (IndexView)manageObject.getObject("indexView");
                        Thread indexThread = thread.getThreadByName("JavaFX Application Thread");
                        if(message.getCon().equals("OK")){
                            indexView.setIsOk(true);
                        }else
                            indexView.setIsOk(false);

                        indexThread.interrupt();
                        System.out.println("success to change name");
                    } else if (message.getMesType().equals("change_Avatar_Result")) {
                        //Change avatar type.
                        IndexView indexView = (IndexView)manageObject.getObject("indexView");
                        Thread indexThread = thread.getThreadByName("JavaFX Application Thread");
                        if(message.getCon().equals("OK")){
                            indexView.setIsOk(true);
                        }else
                            indexView.setIsOk(false);
                        indexThread.interrupt();
                        System.out.println("success to change Avatar");

                    } else if (message.getMesType().equals("group_message")){
                        // Group message type , it used to get group message.
                        String con = message.getCon();
                        String sender = message.getSender();
                        String name = message.getGetter();

                        TextArea chat = manageObject.getChat(name);
                        chat.appendText(sender + " say: \n");
                        chat.appendText(con + "\n" + "\n");

                        //Display group unread message.
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

    /**
     * Send message to Server.
     * @param message message object send to Server.
     */
    public void sendToServer(Message message){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(clientUser.socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
