package model;

import java.util.HashMap;
import java.util.Iterator;

public class manageClientThread {
    public static HashMap<String,SerConClientThread> clientList = new HashMap<>();

    public static void addClientThread(String clientId, SerConClientThread clientThread){
        clientList.put(clientId, clientThread);
    }

    public static SerConClientThread getClientThread(String clientId){
        return clientList.get(clientId);
    }

    public static String getAllOnlineClient(){
        Iterator iterator = clientList.keySet().iterator();
        String list = "";
        while (iterator.hasNext()){
            list += iterator.next().toString() + " ";
        }
        return list;
    }
}
