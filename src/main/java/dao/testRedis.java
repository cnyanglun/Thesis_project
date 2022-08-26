package dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;
import redis.clients.jedis.Jedis;
import util.User;

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
        jedis.sadd(account,account + "_password",email,account + "_friendList",account + "_nickname");
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
        List friendList = new ArrayList();
        var set= jedis.smembers(account + "_friendList");
        for (String element : set) {
            friendList.add(element);
        }
        user.setFriendList(friendList);
        return user;
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



//    public static void main(String[] args) {
//        testRedis t = new testRedis();
//        t.changeName("a1","Yanglun");
//    }


}
