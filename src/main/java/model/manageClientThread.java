package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

public class manageClientThread {
    public static HashMap<String,SerConClientThread> clientList = new HashMap<>();
    public static HashMap<String , Integer> notificationList = new HashMap<>();

    public static void addNotificationList(String account){
        if(notificationList.containsKey(account)){
            Integer integer = notificationList.get(account);
            notificationList.put(account,integer + 1);
        }else {
            notificationList.put(account,1);
        }
    }

    public static int getNotificationList(String account){
        Integer count = notificationList.get(account);
        int unreadCount = Integer.parseInt(String.valueOf(count));
        return unreadCount;
    }

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

    public static boolean isClientOnline(String accountId){
        for (String element: clientList.keySet()) {
            if(element.equals(accountId))
                return true;
        }
        return false;
    }

    public static void delClientThread(String account){
        clientList.remove(account);
    }

}
