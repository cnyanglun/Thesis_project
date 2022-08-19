package model;

import util.Message;
import util.User;
import util.redis.testRedis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class clientManageThread implements Runnable{

    private Socket socket;
    private String account;

    public clientManageThread(Socket socket, String account){
        this.socket = socket;
        this.account = account;
    }

    @Override
    public void run() {
//        try {
//            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//            Message message = new Message();
//            testRedis redis = new testRedis();
//
//
//            oos.writeObject(redis.returnUserInfo(account));


//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


    }
}
