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
public class MyServer implements Runnable{

    private boolean isThreadFinish = false;
//    @Autowired
//    testRedis redis;
    testRedis redis = new testRedis();

//    SerConClientThread serConClientThread;

    private String account;
    public MyServer(){

    }

    private boolean isRegisterSuccess(Object o) {
        registerInfo ri = (registerInfo) o;
        if (redis.isAccountExist(ri.getAccount())) {
            return false;
        }
        else{
            redis.accountRegister(ri.getAccount(),ri.getPassword(),ri.getEmail());
            return true;
        }
    }

    private boolean isLoginSuccess(Object o){
        User user = (User) o;
        account = user.getAccount();
        Logger.info("Get new Account --> Account: " + user.getAccount() + " , Password: " + user.getPassword());

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
    public void run() {
        try {
            Logger.info("Start Server, Port 9999");
            ServerSocket ss = new ServerSocket(9999);
            while (!isThreadFinish) {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                Message message = new Message();
                Message message = Message.builder().build();
                Object o = ois.readObject();

                if(o instanceof User){
                    if (isLoginSuccess(o)) {
                        message.setMesType("loginSuccess");
                        oos.writeObject(message);
                        Logger.info("User " + account + " is online!");

                        SerConClientThread serConClientThread = new SerConClientThread(socket,account);
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

    public void setThreadFinish(boolean isThreadFinish){
        this.isThreadFinish = isThreadFinish;
    }
}
