package util;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class Message implements java.io.Serializable{
    private String mesType;
    private String sender;
    private ArrayList<String> getterList;
    private String getter;
    private String con;
    private String sendTime;
    private User userInfo;
    private boolean isSuccess;
    private Group group;

}
