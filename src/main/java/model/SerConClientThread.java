package model;

import org.springframework.stereotype.Service;
import org.tinylog.Logger;
import util.*;
import dao.testRedis;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

@Service
/**
 * Thread for each user, after the user logs in successfully,
 * the server will create a separate thread for the user.
 */
public class SerConClientThread extends Thread{

    /**
     * To get the socket which has created in MyServer
     */
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
            /**
             * Pass a User object with all the information of this user
             */
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(redis.returnUserInfo(account));

            while (true){
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(inputStream);

                // Accept the message come from client
                Message message = (Message) ois.readObject();

                if(message.getMesType().equals("common_Message")){
                    //Determine if the getter online
                    if(!manageClientThread.isClientOnline(message.getGetter())){
                        Logger.info(message.getGetter() + " is not online");
                        //Count how many notifications
                        redis.storeUnread(message.getGetter(),message.getSender());

                        //Store the chat Record
                        byte[] messageObject = SerializeUtil.serialize(message);
                        redis.storeChatRecord(message.getSender(), message.getGetter(),messageObject);

                        continue;
                    }

                    // Get the recipient's thread
                    SerConClientThread clientThread = manageClientThread.getClientThread(message.getGetter());
                    ObjectOutputStream oos1 = new ObjectOutputStream(clientThread.socket.getOutputStream());
                    // Transfer the message which send by sender to getter
                    oos1.writeObject(message);
                    Logger.info("The message has been transfer to " + message.getGetter() + "from " + message.getSender());

                    //Store the chat Record
                    byte[] messageObject = SerializeUtil.serialize(message);
                    redis.storeChatRecord(message.getSender(), message.getGetter(),messageObject);

                } else if (message.getMesType().equals("search_Friend")) {
                    //The user who should be added
                    String getter = message.getGetter();
                    if (redis.isAccountExist(getter)){
                        User friendInfo = new User();
                        friendInfo.setAccount(getter);

                        Message friendInfo1 = Message.builder().build();
                        friendInfo1.setUserInfo(friendInfo);
                        friendInfo1.setMesType("search_Friend");

                        SerConClientThread clientThread = manageClientThread.getClientThread(message.getSender());
                        ObjectOutputStream oos2 = new ObjectOutputStream(clientThread.socket.getOutputStream());
                        oos2.writeObject(friendInfo1);

                    }else {

                    }
                } else if (message.getMesType().equals("add_Friend")) {

                    //add friend
                    String isAddSuccess = redis.addFriend(message.getSender(),message.getGetter());
                    Message message1 = Message.builder().build();
                    message1.setMesType("add_Result");
                    message1.setCon(isAddSuccess);

                    SerConClientThread clientThread = manageClientThread.getClientThread(message.getSender());
                    //Send the add friend result to client
                    ObjectOutputStream oos3 = new ObjectOutputStream(clientThread.socket.getOutputStream());
                    oos3.writeObject(message1);

                    //Send the user info with new friendList to client
                    ObjectOutputStream oosUserInfo = new ObjectOutputStream(clientThread.socket.getOutputStream());
                    oosUserInfo.writeObject(redis.returnUserInfo(account));


                } else if (message.getMesType().equals("change_NewName")) {
                    String result = redis.changeName(message.getSender(), message.getCon());
                    Message message1 = Message.builder().build();
                    message1.setMesType("change_Result");
                    message1.setCon(result);

                    SerConClientThread clientThread = manageClientThread.getClientThread(message.getSender());
                    ObjectOutputStream oos4 = new ObjectOutputStream(clientThread.socket.getOutputStream());
                    oos4.writeObject(message1);

                } else if (message.getMesType().equals("change_Avatar")) {
                    String result = redis.changeAvatar(message.getSender(), message.getCon());

                    Message change_avatar_result = Message.builder()
                            .mesType("change_Avatar_Result")
                            .con(result).build();
                    System.out.println("shou dao");

                    SerConClientThread clientThread = manageClientThread.getClientThread(message.getSender());
                    ObjectOutputStream oos5 = new ObjectOutputStream(clientThread.socket.getOutputStream());
                    oos5.writeObject(change_avatar_result);
                    System.out.println("shou dao");

                } else if (message.getMesType().equals("exit_Message")) {
                    Logger.info(account + " is offline");
                    manageClientThread.delClientThread(account);
                    break;

                } else if (message.getMesType().equals("clear_Unread")) {
                    redis.clearUnread(message.getSender(),message.getGetter());

                } else if (message.getMesType().equals("create_group")) {
                    String sender = message.getSender();
                    Group group = message.getGroup();

                    ArrayList<String> getterList = message.getGetterList();
                    //Store members
                    for (String userId:getterList) {
                        redis.storeGroup(userId , group);
                    }
                } else if (message.getMesType().equals("group_message")){
                    ArrayList<String> memberList = message.getGetterList();
                    memberList.remove(message.getSender());
                    String groupName = message.getGroup().getGroupName();
                    System.out.println(memberList);

                    for (String member:memberList) {
                        if(!manageClientThread.isClientOnline(member)){
                            redis.storeUnread(member,groupName);
                            redis.storeGroup(member,groupName);

                            continue;
                        }

                        SerConClientThread clientThread = manageClientThread.getClientThread(member);
                        ObjectOutputStream oos1 = new ObjectOutputStream(clientThread.socket.getOutputStream());
                        oos1.writeObject(message);

                        redis.storeGroup(member,groupName);
                    }

                    byte[] messageObject = SerializeUtil.serialize(message);
                    redis.storeGroupChatRecord(groupName,messageObject);

                }
            }


        } catch (EOFException e){
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
