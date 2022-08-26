package controllers;

import javafx.application.Platform;
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
import org.tinylog.Logger;
import util.Message;
import util.User;
import util.tool.manageObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;


public class IndexView implements Initializable{

    @FXML
    private Label accountId;
    @FXML
    private Label nickName;
    @FXML
    private ImageView yourAvatar;
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
    @FXML
    private AnchorPane accountInfo;

    private Stage stage;

    boolean isOk = true;

    private ClientConServerThread ccst;

    String url;
    private User userInfo;
    private String friendId1;
//    HashMap<String,TextArea> hm = new HashMap<>();

    String[] imageUrl = {"/image/avatars/image1.jpg","/image/avatars/image2.jpg","/image/avatars/image3.jpg",
                        "/image/avatars/image4.jpg","/image/avatars/image5.jpg","/image/avatars/image6.jpg",
                        "/image/avatars/image7.jpg","/image/avatars/image8.jpg","/image/avatars/image9.jpg"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //get the userInfo from Server

        init();
        initFriendList();
        addFriendFunction();
        createGroupFunction();
        turnOffFunction();
        userInfoModify();
    }

    private void init(){
        userInfo = ClientConServerThread.user;
        accountId.setText(userInfo.getAccount());

        ClientConServerThread clientConServerThread = (ClientConServerThread) manageObject.getObject("clientConServerThread");
        ccst = clientConServerThread;

    }

    private void userInfoModify(){
        accountInfo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                VBox accountInfoScene = new VBox();
                accountInfoScene.setAlignment(Pos.TOP_CENTER);
                accountInfoScene.setSpacing(20);
                accountInfoScene.setPadding(new Insets(50));

                Label changeName = new Label("CHANGE NAME");
                Label changeAvatar = new Label("CHANGE AVATAR");

                changeName.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        VBox changeNameScene = new VBox();
                        changeNameScene.setSpacing(20);
                        changeNameScene.setAlignment(Pos.CENTER);

                        Label changeTip = new Label("Please input a new Name");
                        Button change = new Button("CHANGE");
                        TextField newName = new TextField();
                        newName.setMaxWidth(200);
                        changeNameScene.getChildren().addAll(changeTip,newName,change);

                        win.setCenter(changeNameScene);

                        change.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                String newNameText = newName.getText();

                                try {
                                    Message changeNewName = new Message();
                                    changeNewName.setSender(userInfo.getAccount());
                                    changeNewName.setMesType("change_NewName");
                                    changeNewName.setCon(newNameText);

                                    ccst.sendToServer(changeNewName);

                                    Thread.sleep(3000);

                                } catch (InterruptedException e) {
                                    if(isOk){
                                        Logger.info("Success to Change");
                                        nickName.setText(newNameText);
                                    }else {
                                        Logger.info("Failed to Change");
                                    }
                                }
                            }
                        });
                    }
                });

                changeAvatar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        VBox changeAvatar = new VBox();
                        changeAvatar.setSpacing(20);
                        changeAvatar.setAlignment(Pos.CENTER);
                        Button change = new Button("CHANGE");
                        FlowPane imageList = new FlowPane();
                        imageList.resize(300,450);
                        imageList.setMaxWidth(300);

                        imageList.setHgap(10);
                        imageList.setVgap(10);


                        for (int i = 0; i < Arrays.stream(imageUrl).count(); i++) {
                            Image image = new Image(imageUrl[i], 60, 60, false, false);
                            ImageView imageView = new ImageView(image);
                            int finalI = i;
                            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    yourAvatar.setImage(image);
                                    url = imageUrl[finalI];
                                }
                            });
                            imageList.getChildren().add(imageView);
                        }

                        change.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                System.out.println(url);
                            }
                        });

                        changeAvatar.getChildren().addAll(change,imageList);
                        win.setCenter(changeAvatar);
                    }
                });


                accountInfoScene.getChildren().addAll(changeName,changeAvatar);


                win.setCenter(accountInfoScene);
            }
        });
    }

    public void initFriendList(){
        chatInterface.setVisible(false);
        var list = userInfo.getFriendList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            String friendId = (String) iterator.next();

            createChat(friendId);
            HBox friend = createFriend(friendId);

            friendList.getChildren().add(friend);
            friendList.setSpacing(5);
            friendList.setPadding(new Insets(5));
        }
    }

    public void createChat(String friendId){
        TextArea chat = new TextArea();
        chat.setPrefSize(537,288);
        manageObject.addChat(friendId,chat);
    }

    private HBox createFriend(String friendId){
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
                displayText = manageObject.getChat(friendId);
                sp.setContent(displayText);
            }
        });


        HBox friend = new HBox();
        friend.resize(200,90);

        friend.getChildren().addAll(image,friendInfo);
        return friend;
    }

    private void addFriendToFriendList(String friendId){
        createChat(friendId);
        HBox friend = createFriend(friendId);
        friendList.getChildren().add(friend);
    }

    private void addFriendFunction(){
        addFriend.setImage(new Image("/image/addfriend.png", 30, 30, false, false));
        addFriend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //create a new Scene which use to search Friend
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
                            message.setMesType("search_Friend");



                            long count = displayResult.getChildren().stream().count();
                            int count1 = Integer.parseInt(String.valueOf(count));
                            if(count1 != 0){
                                displayResult.getChildren().remove(0,count1);
                            }

                            System.out.println(displayResult.getChildren().stream().count());
                            AnchorPane friend = new AnchorPane();
//                                inquireResult = friend;
                            manageObject.addObject("inquireResult",friend);
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
                            Button addbtn = new Button("add");

                            addbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    Message message1 = new Message();
                                    message1.setMesType("add_Friend");
                                    message1.setSender(userInfo.getAccount());
                                    String friendId = InputFriendName.getText();
                                    message1.setGetter(friendId);

                                    ccst.sendToServer(message1);

                                    Label result = new Label();
                                    displayResult.getChildren().add(result);

                                    try {
                                        Thread.sleep(1000*60);
                                    } catch (InterruptedException e) {
//                                            throw new RuntimeException(e);
                                    }

                                    if(isOk){
                                        Logger.info("Success to Add");
                                        result.setText("Success to Add");

                                        //Create new friend and put it into friendList after adding friend
                                        addFriendToFriendList(friendId);
                                    }else {
                                        Logger.info("Failed to add");
                                        result.setText("Failed to add");
                                    }



                                    }
                                });

                                displayResult.getChildren().addAll(addbtn,friend);

                                ccst.sendToServer(message);
                                Logger.info("Send add friend request to Server:" + userInfo.getAccount() + " want to add " + InputFriendName.getText());

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
                Platform.exit();
//                var ccst = (ClientConServerThread)manageObject.getObject("clientConServerThread");
//                ccst.setThreadSwitch(false);
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

            ccst.sendToServer(commonMessage);


            inputText.clear();

            displayText.appendText(accountId.getText() + " say: \n");
            displayText.appendText(content + "\n");
            displayText.appendText("\n");
        }
    }

    public void setIsOk(boolean a){
        this.isOk = a;
    }
    @FXML
    private void clearAll(){
        inputText.clear();
    }
}
