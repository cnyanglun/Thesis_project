package util.tool;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.*;

public class manageObject {

    //manage friend chat Interface
    private static HashMap<String, TextArea> TextAreahm = new HashMap<>();

    //manage unread Message
    private static HashMap<String, Label> labelHM = new HashMap<>();

    //manage various Object
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


    public static void addLabel(String accountId , Label label){
        labelHM.put(accountId,label);
    }
    public static Label getLabel(String accountId){
        return labelHM.get(accountId);
    }

}
