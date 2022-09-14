package dao;

import org.springframework.stereotype.Repository;
import org.tinylog.Logger;
import redis.clients.jedis.Jedis;
import util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
public class testRedis {
    private Jedis jedis;

    public void setJedis(){
//        jedis = new Jedis("127.0.0.1", 6379);
        jedis = new Jedis("redis-11368.c238.us-central1-2.gce.cloud.redislabs.com",11368);
        jedis.auth("s8e4xFzAYuntHPUeQFrvFliB8gkiRwSe");
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
                account + "_chatRecord",account + "_unreadMessage",account + "_groupList",account + "_groupChatRecord");
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

    public void storeChatRecord(String sender, String getter , byte[] messageObject){
//        String chatRecordName = commonUtil.compareStrings(sender,getter);
        String chatRecordName = commonUtil.compareStrings(sender,getter) + "_chatRecord";

        jedis.sadd(sender + "_chatRecord" , chatRecordName);

        jedis.sadd(getter + "_chatRecord" , chatRecordName);

        jedis.rpush(chatRecordName.getBytes(), messageObject);
    }

    public void storeGroup(String accountId ,String groupName){
        jedis.sadd(accountId + "_groupChatRecord" , groupName + "_group");
    }
    public void storeGroupChatRecord(String groupName , byte[] messageObject){
        jedis.rpush((groupName + "_group").getBytes() , messageObject);
    }

    public ArrayList<ArrayList<Message>> getGroupChatRecord(String account){
        Set<String> smembers = jedis.smembers(account + "_groupChatRecord");

        ArrayList<ArrayList<Message>> groups = new ArrayList<>();
        for (String member:smembers) {
            List<byte[]> group = jedis.lrange(member.getBytes(), 0, -1);

            ArrayList<Message> groupMessages = new ArrayList<>();
            for (int i = 0; i < group.size(); i++) {
                Message message = (Message) SerializeUtil.unSerialize(group.get(i));
                groupMessages.add(message);
            }
            groups.add(groupMessages);
        }
        return groups;
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

    public void storeUnread(String getter, String sender){
        if(jedis.sismember(getter + "_unreadMessage", sender + "_count")){
            jedis.incr(sender + "_count");
        }else {
            jedis.sadd(getter + "_unreadMessage",sender + "_count");
            jedis.set(sender + "_count","1");
        }
    }

    public HashMap<String, Integer> getUnread(String account){

        HashMap<String , Integer> unreadCount = new HashMap<>();
        for (String element : jedis.smembers(account + "_unreadMessage")) {
            int count = Integer.parseInt(jedis.get(element));
            element = element.substring(0,element.length()-6);
            unreadCount.put(element,count);
        }
//        System.out.println(unreadCount);
        return unreadCount;
    }

    public void storeGroup(String account, Group group){
        byte[] serialize = SerializeUtil.serialize(group);
        jedis.rpush((account + "_groupList").getBytes(),serialize);
    }

    public ArrayList<Group> getGroup(String account){
        var smembers = jedis.lrange((account + "_groupList").getBytes(),0,-1);

        ArrayList<Group> groupList = new ArrayList<>();
        for (byte[] element:smembers) {
            Group group = (Group) SerializeUtil.unSerialize(element);
            groupList.add(group);
        }
        return groupList;
    }

    public void clearUnread(String sender , String getter){
        jedis.srem(sender + "_unreadMessage", getter + "_count");
        jedis.del(getter + "_count");
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
        user.setUnreadCount(getUnread(account));
        user.setGroups(getGroup(account));
        user.setGroupChatRecord(getGroupChatRecord(account));

        return user;
    }



    public static void main(String[] args) {
        testRedis t = new testRedis();


        t.getGroupChatRecord("a1");
    }


}
