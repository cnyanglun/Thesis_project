package util.tool;

import javafx.scene.control.TextArea;

import java.util.*;

public class manageObject {
    private static HashMap<String, TextArea> TextAreahm = new HashMap<>();
    private static HashMap<String, Object> objectHashMap = new HashMap<>();

    public static void addChat(String accountId, TextArea textArea)
    {
        TextAreahm.put(accountId, textArea);
    }
    //取出
    public static TextArea getChat(String accountId)
    {
        return (TextArea) TextAreahm.get(accountId);
    }

    public static void addObject(String objectName, Object object){
        objectHashMap.put(objectName,object);
    }

    public static Object getObject(String objectName){
        return objectHashMap.get(objectName);
    }

}
