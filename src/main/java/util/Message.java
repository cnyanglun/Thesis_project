package util;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Message implements java.io.Serializable{
    private String mesType;
    private String sender;
    private String getter;
    private String con;
    private String sendTime;
    private User userInfo;
    private boolean isSuccess;

}
