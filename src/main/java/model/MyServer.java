package model;


import org.tinylog.Logger;
import util.Message;
import util.User;
import util.redis.testRedis;
import util.registerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer implements Runnable{

    private boolean isThreadFinish = false;

    testRedis redis = new testRedis();

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
                Message message = new Message();
                Object o = ois.readObject();
                System.out.println("11111");


//                if(o instanceof Message){
//                    Message userInfoMessage = (Message) o;
//                    if(userInfoMessage.getMesType().equals("userInfo")){
//                        System.out.println(account);
//                        oos.writeObject(redis.returnUserInfo(account));
//                    }
//                }
                if(o instanceof User){
                    if (isLoginSuccess(o)) {
                        message.setMesType("loginSuccess");
                        oos.writeObject(message);

//                        Thread clientThread = new Thread(new clientManageThread(socket,account));
//                        clientThread.setName(account + "_Thread");
//                        clientThread.start();
                    }
                    else {
                        message.setMesType("loginFailed");
                        oos.writeObject(message);
                        socket.close();
                    }
                } else if (o instanceof registerInfo) {
                    System.out.println("122222");
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
