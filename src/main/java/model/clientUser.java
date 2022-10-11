package model;

import org.springframework.stereotype.Component;
import org.tinylog.Logger;
import util.Message;
import util.User;
import util.tool.manageObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Send login and registration requests to the server.
 */
public class clientUser {
    public static Socket socket;

    public User userInfo;

    public clientUser(){
        try {
            //Create socket and connect Server
            socket = new Socket("127.0.0.1",9999);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send a request to the server and get a reply whether the login is allowed.
     * If a successful login creates a thread dedicated to the communication server for the user.
     * @param o is a user object , and send it to server.
     * @return a boolean to determine whether to allow login.
     */
    public boolean sendLoginInfo(Object o){
        boolean isSuccessLogin = false;
        try {
            //Send message to server.
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(o);

            //Review message from server.
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            if(message.getMesType().equals("loginSuccess")){
                Logger.info("Success to Login!");
                isSuccessLogin = true;

                //Create a thread dedicated communication server
                ClientConServerThread clientConServerThread = new ClientConServerThread(socket);
                manageObject.addObject("clientConServerThread",clientConServerThread);
                clientConServerThread.start();
            } else if (message.getMesType().equals("loginFailed")) {
                Logger.info("Failed to Login!");
                isSuccessLogin = false;
                socket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return isSuccessLogin;
    }

    /**
     * Send a request to server to register an account.
     * @param o is a user object , and send it to server.
     * @return a boolean to determine whether registration is successful.
     */
    public boolean sendRegisterInfo(Object o){
        boolean isSuccessLogin = false;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(o);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            if (message.getMesType().equals("registerSuccess")) {
                isSuccessLogin = true;
            }
            else if(message.getMesType().equals("registerFailed")) {
                isSuccessLogin = false;
                socket.close();
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return isSuccessLogin;
    }

}
