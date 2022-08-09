package model;

import org.tinylog.Logger;
import util.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class clientUser {
    private Socket socket;

    public boolean sendLoginInfo(Object o){
        boolean isSuccessLogin = false;
        try {
            socket = new Socket("127.0.0.1",9999);
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(o);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            if(message.getMesType().equals("1")){
                Logger.info("登录成功");
                isSuccessLogin = true;
            } else if (message.getMesType().equals("2")) {
                Logger.info("登录失败");
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

    public boolean sendRegisterInfo(Object o){
        boolean isSuccessLogin = false;
        try {
            socket = new Socket("127.0.0.1",9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(o);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();

            if (message.getMesType().equals("1")) {
                isSuccessLogin = true;
            }
            else if(message.getMesType().equals("2")) {
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
