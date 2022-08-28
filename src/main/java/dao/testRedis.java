package dao;

import model.manageClientThread;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;
import redis.clients.jedis.Jedis;
import util.Message;
import util.SerializeUtil;
import util.User;
import util.commonUtil;

import java.util.ArrayList;
import java.util.List;
@Repository
public class testRedis {
    private Jedis jedis;

    public void setJedis(){
        jedis = new Jedis("127.0.0.1", 6379);
    }


    public testRedis() {
        setJedis();
        init();
    }

    public void init(){
        jedis.sadd("Accounts","default");
    }

    public boolean isAccountExist(String account){
//        Logger.info("Determine if the account existed.......");
        if(jedis.sismember("Accounts",account)){
            Logger.info("The account has been existed!!");
            return true;
        }
        else{
            Logger.info("The account can be used!!");
            return false;
        }
    }

    public void accountRegister(String account,String password , String email){

        jedis.sadd("Accounts",account);
        jedis.sadd(account,account + "_password",email,account + "_friendList",account + "_nickname",account + "_avatar",
                account + "_chatRecord");
        jedis.hset(account + "_password",account,password);
        Logger.info("The account has been added to database!");
    }

    public boolean accountLogin(String account ,String password){
        try{
            if(jedis.hget(account + "_password",account).equals(password)){
                return true;
            }
            else{
                return false;
            }
        }catch (NullPointerException e){
            return false;
        }
    }

    public User returnUserInfo(String account){
        User user = new User();
        user.setAccount(account);
        user.setNickName(jedis.get(account + "_nickname"));
        user.setImageUrl(jedis.get(account + "_avatar"));
        List<User> friendList = new ArrayList();
        ArrayList<Message> chatRecordList;
        var set= jedis.smembers(account + "_friendList");
        for (String element : set) {
            User friendInfo = new User();
            friendInfo.setAccount(element);
            friendInfo.setNickName(jedis.get(element + "_nickname"));
            friendInfo.setImageUrl(jedis.get(element + "_avatar"));

            chatRecordList = getChatRecord(account, element);
            friendInfo.setChatRecord(chatRecordList);

            friendList.add(friendInfo);
        }
        user.setFriendList(friendList);

//        if(manageClientThread.notificationList.containsKey(account))
//            user.setUnreadCount(manageClientThread.getNotificationList(account));
        return user;
    }

    public void storeChatRecord(String sender, String getter , byte[] messageObject){
//        String chatRecordName = commonUtil.compareStrings(sender,getter);
        String chatRecordName = commonUtil.compareStrings(sender,getter) + "_chatRecord";

        jedis.sadd(sender + "_chatRecord" , chatRecordName);

        jedis.sadd(getter + "_chatRecord" , chatRecordName);

        jedis.rpush(chatRecordName.getBytes(), messageObject);
    }

    public ArrayList<Message> getChatRecord(String sender,String getter){
        String name = commonUtil.compareStrings(sender, getter);
        var messageObject = name + "_chatRecord";
        List<byte[]> messageList = jedis.lrange(messageObject.getBytes(), 0, -1);

        ArrayList<Message> list = new ArrayList<>();
        for (int i = 0; i < messageList.size(); i++) {
            Message message = (Message)SerializeUtil.unSerialize(messageList.get(i));
            list.add(message);
        }

        return list;
    }

    public String addFriend(String senderId, String getterId){

        var a =jedis.sadd(senderId + "_friendList",getterId);
        var b = jedis.sadd(getterId + "_friendList",senderId);
        Logger.info("Success to add Friend");
        if (a == 1 && b ==1)
            return "OK";
        return "NO  ";
    }

    public String changeName(String senderId , String newName){
        String result = jedis.set(senderId + "_nickname", newName);
        return result;
    }

    public String changeAvatar(String senderId , String imageUrl){
        String result = jedis.set(senderId + "_avatar" , imageUrl);
        return result;
    }




//    public static void main(String[] args) {
//        testRedis t = new testRedis();
//
//        ArrayList<Message> chatRecord = t.getChatRecord("a3", "a2");
//        for (Message message: chatRecord) {
//            System.out.println(message);
//        }
////        System.out.println();
//    }


}