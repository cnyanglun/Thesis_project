package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements java.io.Serializable{
    private String account;
    private String password;
    private String nickName;
    private String imageUrl;
    //    private int unreadCount;
    private ArrayList<Message> chatRecord;
    private List<User> friendList = new ArrayList();
    private HashMap<String,Integer> unreadCount = new HashMap<>();
    private ArrayList<Group> groups;

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public HashMap<String, Integer> getUnreadCount() {
        return unreadCount;
    }
    public void setUnreadCount(HashMap<String, Integer> unreadCount) {
        this.unreadCount = unreadCount;
    }

    public List<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<Message> getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(ArrayList<Message> chatRecord) {
        this.chatRecord = chatRecord;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
