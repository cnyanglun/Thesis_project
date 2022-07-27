package redis;

import org.tinylog.Logger;
import redis.clients.jedis.Jedis;

import java.util.Scanner;

public class testRedis {
    private Jedis jedis;

    private String account;

    private String password;

    private String email;

    private String id;

    public void setJedis(){
        jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("Success to bind!!!");
    }

    public void testJedis(){
        jedis.set("n1","yanglun");
        var n1 = jedis.get("n1");
        System.out.println(n1);
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

    public boolean isAccountExist(){
//        Logger.info("Determine if the account existed.......");
        if(jedis.sismember("Accounts",account)){
//            Logger.info("The account has been existed!!");
            return true;
        }
        else{
//            Logger.info("The account can be used!!");
            return false;
        }
    }

    public void accountRegister(){
        if(jedis.exists("ID")){
            if(!isAccountExist()){
                var numOfUsers = Integer.parseInt(String.valueOf(jedis.scard("ID")));
                id = String.valueOf(numOfUsers);
                jedis.hset(account,account,password);
                jedis.sadd("ID",id);
                jedis.sadd(id,account,email);

                jedis.sadd("Accounts",account);
                Logger.info("The account has been added to database!");
            }
        }
        else {
            jedis.sadd("ID","0");
            accountRegister();
        }
    }

    public boolean accountLogin(String account ,String password){
        if(jedis.hget(account,account).equals(password)){
            return true;
        }
        else{
            return false;
        }
    }

    public static void main(String[] args) {
        testRedis test = new testRedis("a3","123","7245@qq.com");
        test.init();
//        test.accountRegister();
        System.out.println(test.accountLogin("a3", "123"));
    }


}
