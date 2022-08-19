package util;

import java.util.ArrayList;
import java.util.List;

public class User implements java.io.Serializable{
    private String account;

    private String password;

    private String nickName;

    private List friendList = new ArrayList();

    public List getFriendList() {
        return friendList;
    }

    public void setFriendList(List friendList) {
        this.friendList = friendList;
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
