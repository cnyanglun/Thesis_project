package model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;
import util.Message;
import util.User;
import dao.testRedis;
import util.registerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Service
/**
 * The thread of server , Used to determine whether to login or register
 */
public class MyServer implements Runnable{

    private boolean isThreadFinish = false;

    testRedis redis;

    private String account;

    public MyServer(){

    }

    /**
     * A method to determine whether the user registration account is successful
     * @param o is a registerInfo object , is the user information entered by user
     * @return a boolean value , Whether the user registration account is successful
     */
    private boolean isRegisterSuccess(Object o) {
        registerInfo ri = (registerInfo) o;
        //To determine whether the account has existed
        if (redis.isAccountExist(ri.getAccount())) {
            return false;
        }
        else{
            redis.accountRegister(ri.getAccount(),ri.getPassword(),ri.getEmail());
            return true;
        }
    }

    /**
     * A method to determine whether the user login is successful
     * @param o is a User object , it contains account and password which entered by user
     * @return a boolean value , Whether the user login account is successful ,return false which means failed to login
     */
    private boolean isLoginSuccess(Object o){
        User user = (User) o;
        account = user.getAccount();
        Logger.info("Get Account --> Account: " + user.getAccount() + " , Password: " + user.getPassword());

        if(redis.accountLogin(user.getAccount(), user.getPassword())){
            Logger.info("Success to Login!!!");
            return true;
        }
        else {
            Logger.info("Password is incorrect");
            return false;
        }
    }

    @Override
    //Create a new thread for Server
    public void run() {
        try {
            Logger.info("Start Server, Port 9999");
            //Create serverSocket , Configure the appropriate information
            ServerSocket ss = new ServerSocket(9999);
            redis = new testRedis();
            while (!isThreadFinish) {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message = Message.builder().build();
                //Accept the information passed by client
                Object o = ois.readObject();

                //To determine whether the object is User or registerInfo
                if(o instanceof User){
                    if (isLoginSuccess(o)) {
                        message.setMesType("loginSuccess");
                        oos.writeObject(message);
                        Logger.info("User " + account + " is online!");

                        //if user login success, create a new thread for this user
                        SerConClientThread serConClientThread = new SerConClientThread(socket,redis,account);
                        //put this user to manageClientThread
                        manageClientThread.addClientThread(account,serConClientThread);
                        serConClientThread.start();

                    }
                    else {
                        message.setMesType("loginFailed");
                        oos.writeObject(message);
                        socket.close();
                    }
                } else if (o instanceof registerInfo) {
                    if(isRegisterSuccess(o)){
                        message.setMesType("registerSuccess");
                        oos.writeObject(message);
                        socket.close();
                    }
                    else {
                        message.setMesType("registerFailed");
                        oos.writeObject(message);
                        socket.close();
                    }
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
