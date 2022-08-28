package util;

import lombok.Builder;

@Builder
public class Message implements java.io.Serializable{
    private String mesType;
    private String sender;
    private String getter;
    private String con;
    private String sendTime;
    private User userInfo;
    private boolean isSuccess;
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mesType='" + mesType + '\'' +
                ", sender='" + sender + '\'' +
                ", getter='" + getter + '\'' +
                ", con='" + con + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", userInfo=" + userInfo +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
