package util;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class Group implements java.io.Serializable{
    private String groupName;
    private ArrayList<String> memberName;
    private ArrayList<Message> getChatRecord;
}
