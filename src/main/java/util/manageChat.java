package util;

import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.util.*;

public class manageChat {
    private static HashMap<String, TextArea> hm = new HashMap<>();

    public static void addChat(String accountId, TextArea textArea)
    {
        hm.put(accountId, textArea);
    }
    //取出
    public static TextArea getChat(String accountId)
    {
        return (TextArea) hm.get(accountId);
    }

}
