package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.ClientConServerThread;
import model.clientUser;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;
import util.Message;
import util.User;
import util.manageChat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @FXML
    private ImageView turnOff;
    @FXML
    private ImageView addFriend;
    @FXML
    private ImageView createGroup;
    @FXML
    private AnchorPane functionBar;
    @FXML
    private AnchorPane acp;
    @FXML
    private BorderPane win;
    public static AnchorPane inquireResult;

    private Stage stage;

    boolean isVisible = true;
    private User userInfo;
    private String friendId1;
//    HashMap<String,TextArea> hm = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //get the userInfo from Server

        init();
        initFriendList();
        addFriendFunction();
        createGroupFunction();
        turnOffFunction();
//        System.out.println(userInfo.getFriendList());
    }

    private void init(){
        userInfo = ClientConServerThread.user;
        accountId.setText(userInfo.getAccount());
    }

    public void initFriendList(){
        chatInterface.setVisible(false);
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
            friendInfo.setSpacing(20);

            ImageView image = new ImageView(new Image("/image/image.jpg",60,60,false,false));
            image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    win.setCenter(chatInterface);
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

    private void addFriendFunction(){
        addFriend.setImage(new Image("/image/addfriend.png", 30, 30, false, false));
        addFriend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                VBox addFriendScene = new VBox();
                addFriendScene.setAlignment(Pos.TOP_CENTER);
                addFriendScene.setSpacing(20);
                addFriendScene.setPadding(new Insets(20));
                Label friendAccount = new Label("Please Input user's name");
                TextField InputFriendName = new TextField();
                InputFriendName.setMaxWidth(200);
                Button inquire = new Button("Inquire");

                VBox displayResult = new VBox();
                displayResult.setAlignment(Pos.CENTER);
                displayResult.setSpacing(40);

                inquire.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (!InputFriendName.getText().isEmpty()){
                            Message message = new Message();
                            message.setSender(userInfo.getAccount());
                            message.setGetter(InputFriendName.getText());
                            message.setMesType("add_Friend");


                            try {
                                long count = displayResult.getChildren().stream().count();
                                int count1 = Integer.parseInt(String.valueOf(count));
                                if(count1 != 0){
                                    displayResult.getChildren().remove(0,count1);
                                }

                                System.out.println(displayResult.getChildren().stream().count());
                                AnchorPane friend = new AnchorPane();
                                //
                                inquireResult = friend;
                                friend.setMaxWidth(200);
                                friend.setPadding(new Insets(5));
                                ImageView pic = new ImageView(new Image("/image/image.jpg",80,80,false,false));
                                friend.resize(200,90);
                                Label friendId = new Label();
                                Label friendName = new Label();
                                AnchorPane.setRightAnchor(friendId,5.0);
                                AnchorPane.setTopAnchor(friendId,10.0);
                                AnchorPane.setRightAnchor(friendName,5.0);
                                AnchorPane.setBottomAnchor(friendName,15.0);
                                friend.getChildren().addAll(pic,friendId,friendName);
                                Button asd = new Button("add");
                                displayResult.getChildren().addAll(asd,friend);


                                ObjectOutputStream oos = new ObjectOutputStream(clientUser.socket.getOutputStream());
                                oos.writeObject(message);
                                Logger.info("Send add friend request to Server:" + userInfo.getAccount() + " want to add " + InputFriendName.getText());

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else
                            Logger.info("The text is null , please input the user you want to add!");
                    }
                });



                addFriendScene.getChildren().addAll(friendAccount,InputFriendName,inquire,displayResult);

                win.setCenter(addFriendScene);
            }
        });
    }

    private void turnOffFunction(){
        turnOff.setImage(new Image("/image/exit.png",30,30,false,false));
        turnOff.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });
    }

    private void createGroupFunction(){
        createGroup.setImage(new Image("/image/img.png",30,30,false,false));
        createGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });
    }

//    private void visible(){
//        if (isVisible) {
//            chatInterface.setVisible(false);
//            isVisible = false;
//        } else {
//            chatInterface.setVisible(true);
//            isVisible = true;
//        }
//    }

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
