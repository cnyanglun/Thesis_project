package model;


import javafx.fxml.Initializable;
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

public class MyServer {
    testRedis redis = new testRedis();

    public MyServer(){
        try {
            Logger.info("Start Server, Port 9999");
            ServerSocket ss = new ServerSocket(9999);
            while (true) {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message = new Message();
                Object o = ois.readObject();


                if(o instanceof Message){

                }
                else if(o instanceof User){
                    if (isLoginSuccess(o)) {
                        message.setMesType("1");
                        oos.writeObject(message);
                    }
                    else {
                        message.setMesType("2");
                        oos.writeObject(message);
                        socket.close();
                    }
                } else if (o instanceof registerInfo) {
                    if(isRegisterSuccess(o)){
                        message.setMesType("1");
                        oos.writeObject(message);
                    }
                    else {
                        message.setMesType("2");
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
}
