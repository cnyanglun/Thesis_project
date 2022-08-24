package model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;
import util.Message;
import util.User;
import dao.testRedis;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class SerConClientThread extends Thread{

    private Socket socket;
    private String account;

//    testRedis redis;
    testRedis redis = new testRedis();


    public SerConClientThread(Socket socket, String account){
        this.socket = socket;
        this.account = account;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(redis.returnUserInfo(account));

            while (true){
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(inputStream);

                Message message = (Message) ois.readObject();
//                System.out.println(message.getMesType() + " " + message.getSender() + " " + message.getGetter() + " " + message.getCon());
                if(message.getMesType().equals("common_Message")){
                    SerConClientThread clientThread = manageClientThread.getClientThread(message.getGetter());
                    ObjectOutputStream oos1 = new ObjectOutputStream(clientThread.socket.getOutputStream());
                    oos1.writeObject(message);
                    Logger.info("The message has been transfer to " + message.getGetter() + "from " + message.getSender());
                } else if (message.getMesType().equals("add_Friend")) {
                    //The user who should be added
                    String getter = message.getGetter();
                    if (redis.isAccountExist(getter)){
                        User friendInfo = new User();
                        friendInfo.setAccount(getter);

                        Message friendInfo1 = new Message();
                        friendInfo1.setUserInfo(friendInfo);
                        friendInfo1.setMesType("add_Friend");

                        SerConClientThread clientThread = manageClientThread.getClientThread(message.getSender());
                        ObjectOutputStream oos2 = new ObjectOutputStream(clientThread.socket.getOutputStream());
                        oos2.writeObject(friendInfo1);

                    }else {

                    }
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            Logger.info("The client not online");
        }
    }
}
