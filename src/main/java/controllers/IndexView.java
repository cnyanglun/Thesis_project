package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.ClientConServerThread;
import model.clientUser;
import org.tinylog.Logger;
import util.Message;
import util.User;
import util.manageChat;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

public class IndexView implements Initializable{

    @FXML
    private Label accountId;
    @FXML
    private Label speakWith;
    @FXML
    private VBox friendList;
    @FXML
    private BorderPane chatInterface;
    @FXML
    private TextArea inputText;
    @FXML
    private TextArea displayText;
    @FXML
    private ScrollPane sp;

    private Stage stage;

    private User userInfo;

    private String friendId1;
//    HashMap<String,TextArea> hm = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //get the userInfo from Server
        init();
        chatInterface.setVisible(false);
        initFriendList();
//        System.out.println(userInfo.getFriendList());
    }

    private void init(){
        userInfo = ClientConServerThread.user;
        accountId.setText(userInfo.getAccount());
    }

    public void initFriendList(){
        var list = userInfo.getFriendList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            String friendId = (String) iterator.next();
            TextArea chat = new TextArea();
            chat.setPrefSize(537,288);
            manageChat.addChat(friendId,chat);

            Label friendAccountId = new Label("label");
            friendAccountId.setText(friendId);
            Label friendName = new Label("label");

            VBox friendInfo = new VBox();

            friendInfo.getChildren().addAll(friendAccountId,friendName);
            friendInfo.setPadding(new Insets(5));
            friendInfo.setSpacing(30);

            ImageView image = new ImageView(new Image("/image/image.jpg",80,80,false,false));
            image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    friendId1 = friendId;
                    chatInterface.setVisible(true);
                    speakWith.setText(userInfo.getAccount() + " speak with " + friendId);
                    displayText = manageChat.getChat(friendId);
                    sp.setContent(displayText);
                }
            });


            HBox friend = new HBox();
            friend.resize(200,90);

            friend.getChildren().addAll(image,friendInfo);
            friendList.getChildren().add(friend);
            friendList.setSpacing(5);
            friendList.setPadding(new Insets(5));
        }


    }

    @FXML
    private void send(){
        if(!inputText.getText().isEmpty()){
            String content = inputText.getText();
            Message commonMessage = new Message();

            commonMessage.setMesType("common_Message");
            commonMessage.setSender(userInfo.getAccount());
            commonMessage.setGetter(friendId1);
            commonMessage.setCon(content);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
            String time = sdf.format(new Date());
            commonMessage.setSendTime(time);

            try {
                ObjectOutputStream oos = new ObjectOutputStream(clientUser.socket.getOutputStream());
                oos.writeObject(commonMessage);
                Logger.info("Send message to Server: " + commonMessage.getCon());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            inputText.clear();

            displayText.appendText(accountId.getText() + " say: \n");
            displayText.appendText(content + "\n");
            displayText.appendText("\n");
        }
    }

    @FXML
    private void clearAll(){
        inputText.clear();
    }
}
