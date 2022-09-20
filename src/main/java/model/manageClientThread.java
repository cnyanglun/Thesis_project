package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * To manage all the users' information and unread information.
 */
public class manageClientThread {
    /**
     * Store each online user's Thread
     */
    public static HashMap<String,SerConClientThread> clientList = new HashMap<>();

    /**
     * Store the unread information of user
     */
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

    /**
     * Add client information to client hashMap.
     * @param clientId user accountId.
     * @param clientThread is the user's Thread.
     */
    public static void addClientThread(String clientId, SerConClientThread clientThread){
        clientList.put(clientId, clientThread);
    }

    /**
     * Get user's Thread.
     * @param clientId user accountID.
     * @return user's Thread.
     */
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

    /**
     * To determine whether the user online.
     * @param accountId
     * @return
     */
    public static boolean isClientOnline(String accountId){
        for (String element: clientList.keySet()) {
            if(element.equals(accountId))
                return true;
        }
        return false;
    }

    /**
     * When the user offline , the server will remove the user's thread from clientList.
     * @param account
     */
    public static void delClientThread(String account){
        clientList.remove(account);
    }

}
