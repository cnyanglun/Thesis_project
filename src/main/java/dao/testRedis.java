package dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;
import redis.clients.jedis.Jedis;
import util.User;

import java.util.ArrayList;
import java.util.List;
public class testRedis {
    private Jedis jedis;

    private String account;

    private String password;

    private String email;

    private String id;

    public void setJedis(){
        jedis = new Jedis("127.0.0.1", 6379);
    }


    public testRedis() {
        setJedis();
        init();
    }

    public testRedis(String account, String password, String email) {
        this.account = account;
        this.password = password;
        this.email = email;
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
        jedis.sadd(account,account + "_password",email,account + "_friendList");
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



//    public static void main(String[] args) {
//        testRedis t = new testRedis();
//        User user = t.returnUserInfo("a1");
//        System.out.println(user.getFriendList());
//    }


}
