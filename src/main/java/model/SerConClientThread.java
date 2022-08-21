package model;

import org.tinylog.Logger;
import util.Message;
import util.redis.testRedis;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SerConClientThread extends Thread{

    private Socket socket;
    private String account;

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
