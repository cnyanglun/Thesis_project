package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import model.ClientConServerThread;
import model.clientUser;
import org.tinylog.Logger;
import util.Group;
import util.Message;
import util.User;
import util.tool.manageObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


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

    /**
     * the object of ClientConServerThread.
     */
    private ClientConServerThread ccst;

    String url;
    private User userInfo;
    private String friendId1;
    private Group group1;

    /**
     * The url of avatars.
     */
    String[] imageUrl = {"/image/avatars/image1.jpg","/image/avatars/image2.jpg","/image/avatars/image3.jpg",
                        "/image/avatars/image4.jpg","/image/avatars/image5.jpg","/image/avatars/image6.jpg",
                        "/image/avatars/image7.jpg","/image/avatars/image8.jpg","/image/avatars/image9.jpg"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //get the userInfo from Server

        //Wait the initialization information come.
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
        }

        init();
        initFriendList();
        initGroupList();
        addFriendFunction();
        createGroupFunction();
        turnOffFunction();
        userInfoModify();
    }

    /**
     * Basic processing, binding user information to a fixed location.
     */
    private void init(){
        try {
            userInfo = ClientConServerThread.user;
            accountId.setText(userInfo.getAccount());
            nickName.setText(userInfo.getNickName());
            yourAvatar.setImage(new Image(userInfo.getImageUrl(),60,60,false,false));
        }catch (NullPointerException e){
            Logger.info("Your information is not perfect");
        }

        // Obtain the object of ClientConServerThread
        ClientConServerThread clientConServerThread = (ClientConServerThread) manageObject.getObject("clientConServerThread");
        ccst = clientConServerThread;

    }

    /**
     * Functions of Changing user's name and avatar.
     */
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

                //Change name Function
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
                                    Message changeNewName = Message.builder()
                                            .sender(userInfo.getAccount())
                                            .mesType("change_NewName")
                                            .con(newNameText).build();

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

                //Change avatar function
                changeAvatar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        VBox changeAvatar = new VBox();
                        changeAvatar.setSpacing(20);
                        changeAvatar.setAlignment(Pos.CENTER);
                        Button change = new Button("CHANGE");
                        Label changeResult = new Label();
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
                                if(url != null){
                                    Message change_avatars = Message.builder()
                                            .mesType("change_Avatar")
                                            .sender(userInfo.getAccount())
                                            .con(url).build();

                                    ccst.sendToServer(change_avatars);

                                    try {
                                        Thread.sleep(1000*5);
                                    } catch (InterruptedException e) {
                                        System.out.println("good");
                                    }

                                    if(isOk){
                                        changeResult.setText("Success to Change");
                                    }else
                                        changeResult.setText("Failed to Change");
                                }

                            }
                        });


                        changeAvatar.getChildren().addAll(change,imageList,changeResult);
                        win.setCenter(changeAvatar);
                    }
                });


                accountInfoScene.getChildren().addAll(changeName,changeAvatar);


                win.setCenter(accountInfoScene);
            }
        });
    }

    /**
     * Get all friends from initialization user's information.
     */
    public void initFriendList(){
        chatInterface.setVisible(false);
        var list = userInfo.getFriendList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            User friendObject = (User)iterator.next();
            String friendId = friendObject.getAccount();

            createChat(friendId);
            HBox friend = createFriend(friendObject);

            friendList.getChildren().add(friend);
            friendList.setSpacing(5);
            friendList.setPadding(new Insets(5));
        }
    }

    /**
     * Create and initialize a chat box with friends.
     * @param friendId is the account of friend.
     */
    public void createChat(String friendId){
        TextArea chat = new TextArea();
        chat.setEditable(false);
        chat.setPrefSize(537,288);

        //load chat Record
        for (User friend : userInfo.getFriendList()) {
            if(friend.getAccount().equals(friendId)){
                for (Message message : friend.getChatRecord()) {
                    chat.appendText(message.getSendTime() + " " + accountId.getText() + " say: \n" + message.getCon() + "\n" + "\n");

                }
            }
        }
//        System.out.println(userInfo.getUnreadCount());

        manageObject.addChat(friendId,chat);
    }

    /**
     * Create a friend's HBox and information and set click event.
     * @param friendObject
     * @return a HBox with click event.
     */
    private HBox createFriend(User friendObject){
        String name = friendObject.getAccount();

        Label friendAccountId = new Label("label");
        Label friendName = new Label("label");
        Label unreadCount = new Label();
        unreadCount.setText(" ");
        unreadCount.setTextFill(Color.RED);
        manageObject.addLabel(name,unreadCount);

        VBox friendInfo = new VBox();


        for (String element : userInfo.getUnreadCount().keySet()) {
            if(element.equals(name)){
                unreadCount.setText(String.valueOf(userInfo.getUnreadCount().get(element)));
                break;
            }
        }


        friendInfo.getChildren().addAll(friendAccountId,unreadCount,friendName);
        friendInfo.setPadding(new Insets(5));
        friendInfo.setSpacing(8);

        ImageView image;
        try {
            friendAccountId.setText(name);
            image = new ImageView(new Image(friendObject.getImageUrl(), 60, 60, false, false));
            friendName.setText(friendObject.getNickName());
        }catch (NullPointerException e){
            image = new ImageView(new Image("/image/image.jpg",60,60,false,false));
            friendName.setText("");
        }

        image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                win.setCenter(chatInterface);
                friendId1 = name;
                chatInterface.setVisible(true);
                speakWith.setText(userInfo.getAccount() + " speak with " + name);
                displayText = manageObject.getChat(name);
                sp.setContent(displayText);

                unreadCount.setText(" ");
                Message clear_unread = Message.builder().mesType("clear_Unread").sender(userInfo.getAccount()).getter(name).build();
                ccst.sendToServer(clear_unread);
            }
        });


        HBox friend = new HBox();
        friend.resize(200,90);

        friend.getChildren().addAll(image,friendInfo);
        return friend;
    }

    /**
     * Create and initialize a chat box with groups.
     * @param groupName is the name of Group.
     */
    private void createGroupChat(String groupName){
        TextArea chat = new TextArea();
        chat.setEditable(false);
        chat.setPrefSize(537,288);

        ArrayList<ArrayList<Message>> groups = userInfo.getGroupChatRecord();
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Message> group = groups.get(i);
            for (int j = 0; j < group.size(); j++) {
                Message message = group.get(j);
//                System.out.println(message.getCon());
                if(message.getGroup().getGroupName().equals(groupName)){
                    chat.appendText(message.getSender() + "says:\n" + message.getCon() + "\n\n");
                }

            }

        }

        manageObject.addChat(groupName,chat);
    }

    /**
     * Create the group of HBox and set click event.
     * @param group is a group object which contain group's information
     * @return a group HBox with click event.
     */
    private HBox createGroupHBox(Group group){
        String name = group.getGroupName();

//        Label friendAccountId = new Label("label");
        Label groupName = new Label("(Group:) " + group.getGroupName());
        Label unreadCount = new Label();
        unreadCount.setText(" ");
        unreadCount.setTextFill(Color.RED);
        manageObject.addLabel(name,unreadCount);

        VBox groupInfo = new VBox();


        for (String element : userInfo.getUnreadCount().keySet()) {
            if(element.equals(name)){
                unreadCount.setText(String.valueOf(userInfo.getUnreadCount().get(element)));
                break;
            }
        }


        groupInfo.getChildren().addAll(groupName,unreadCount);
        groupInfo.setPadding(new Insets(5));
        groupInfo.setSpacing(8);

        ImageView image = new ImageView();
        image.setImage(new Image("/image/image.jpg",60,60,false,false));

        image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                group1 = group;
                win.setCenter(chatInterface);
                friendId1 = name;
                chatInterface.setVisible(true);
                speakWith.setText("You are in " + name + " Group");
                displayText = manageObject.getChat(name);
                sp.setContent(displayText);

                unreadCount.setText(" ");
//                Message clear_unread = Message.builder().mesType("clear_GroupUnread").sender(userInfo.getAccount()).getter(name).build();
//                ccst.sendToServer(clear_unread);
            }
        });


        HBox groupBox = new HBox();
        groupBox.resize(200,90);

        groupBox.getChildren().addAll(image, groupInfo);
        return groupBox;
    }

    /**
     * Add friend function , it contains the function of querying and adding friends
     */
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
                            Message message = Message.builder()
                                    .sender(userInfo.getAccount())
                                    .getter(InputFriendName.getText())
                                    .mesType("search_Friend").build();
//                            Message message = new Message();
//                            message.setSender(userInfo.getAccount());
//                            message.setGetter(InputFriendName.getText());
//                            message.setMesType("search_Friend");


                            //
                            long count = displayResult.getChildren().stream().count();
                            int count1 = Integer.parseInt(String.valueOf(count));
                            if(count1 != 0){
                                displayResult.getChildren().remove(0,count1);
                            }

//                            System.out.println(displayResult.getChildren().stream().count());
                            AnchorPane friend = new AnchorPane();
//                                inquireResult = friend;
                            manageObject.addObject("inquireResult",friend);
                            friend.setMaxWidth(200);
                            friend.setPadding(new Insets(5));
                            ImageView pic = new ImageView(new Image("/image/image.jpg",80,80,false,false));
                            friend.resize(200,90);
                            Label friendIdLabel = new Label();
                            Label friendName = new Label();
                            AnchorPane.setRightAnchor(friendIdLabel,5.0);
                            AnchorPane.setTopAnchor(friendIdLabel,10.0);
                            AnchorPane.setRightAnchor(friendName,5.0);
                            AnchorPane.setBottomAnchor(friendName,15.0);
                            friend.getChildren().addAll(pic, friendIdLabel,friendName);
                            Button addbtn = new Button("add");

                            addbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                    String friendId = friendIdLabel.getText();

                                    if(!friendId.isEmpty()){
                                        Message message1 = Message.builder()
                                                .sender(userInfo.getAccount())
                                                .mesType("add_Friend")
                                                .getter(friendId).build();

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
//                                        addFriendToFriendList(friendId);
                                        }else {
                                            Logger.info("Failed to add");
                                            result.setText("Failed to add");
                                        }

                                        refresh();
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

    /**
     * Close the software and the link to the server
     */
    private void turnOffFunction(){
        turnOff.setImage(new Image("/image/exit.png",30,30,false,false));
        turnOff.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Platform.exit();
                try {
                    Message exitMessage = Message.builder()
                                    .mesType("exit_Message").build();
                    ccst.sendToServer(exitMessage);

                    clientUser.socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * The function of Creating group.
     */
    private void createGroupFunction(){
        createGroup.setImage(new Image("/image/img.png",30,30,false,false));
        createGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                VBox vBox = new VBox();

                HBox group = new HBox();
                //switch the scence to win
                win.setCenter(vBox);
                group.setSpacing(20);
                group.setPadding(new Insets(20));

                TableView table = new TableView();
                table.setMaxWidth(200);
                table.setMaxHeight(300);

                TableColumn<User,String> account = new TableColumn("account");
                TableColumn<User,String> name = new TableColumn("name");

                account.setCellValueFactory(new PropertyValueFactory<>("account"));
                name.setCellValueFactory(new PropertyValueFactory<>("nickName"));

                List<User> list = userInfo.getFriendList();

                ObservableList<User> obList = FXCollections.observableArrayList();
                obList.addAll(list);
                table.setItems(obList);


                TableView table2 = new TableView();
                table2.setMaxWidth(200);
                table2.setMaxHeight(300);

                TableColumn<User,String> account2 = new TableColumn("account");
                TableColumn<User,String> name2 = new TableColumn("name");

                account2.setCellValueFactory(new PropertyValueFactory<>("account"));
                name2.setCellValueFactory(new PropertyValueFactory<>("nickName"));



                table.setRowFactory(t -> {
                    TableRow row = new TableRow();
                    row.setOnMouseClicked(event -> {
                        if(event.getClickCount() == 2 && (! row.isEmpty())){
                            if(!table2.getItems().contains(row.getItem())){
                                User item = (User)row.getItem();
                                table2.getItems().add(item);
                            }
                        }
                    });
                    return row;
                });

                table2.setRowFactory(t -> {
                    TableRow row = new TableRow();
                    row.setOnMouseClicked(event -> {
                        if(event.getClickCount() == 2 && (! row.isEmpty())){
                            User item = (User)row.getItem();
                            table2.getItems().remove(item);
                        }
                    });
                    return row;
                });

                table.getColumns().addAll(account,name);
                table2.getColumns().addAll(account2,name2);

                VBox resultVbox = new VBox();
                resultVbox.setAlignment(Pos.TOP_CENTER);
                resultVbox.setSpacing(20);

                TextField groupName = new TextField();
                Button ok = new Button("OK");
                Label result = new Label();
                resultVbox.getChildren().addAll(groupName,ok,result);

                ArrayList<String> accounts = new ArrayList<>();
                ok.setOnMouseClicked(e -> {
                    ObservableList items = table2.getItems();

                    String groupNameText = groupName.getText();
                    if(!groupNameText.isEmpty()){
                        for (Object user:items) {
                            var user1 = (User) user;
                            accounts.add(user1.getAccount());
                        }
                        accounts.add(userInfo.getAccount());

                        TextArea chat = new TextArea();
                        chat.setEditable(false);
                        chat.setPrefSize(537,288);

                        manageObject.addChat(groupNameText + "_group",chat);

                        Group groupBuild = Group.builder().groupName(groupNameText).memberName(accounts).build();
                        Message create_group = Message.builder()
                                .mesType("create_group")
                                .sender(userInfo.getAccount())
                                .getterList(accounts)
                                .group(groupBuild).build();

                        ccst.sendToServer(create_group);

                        result.setText("Success");
                    }
                });


                group.getChildren().addAll(table,table2);
                vBox.getChildren().addAll(group,resultVbox);
            }
        });
    }

    /**
     * Get all the groups from user initialization information.
     */
    private void initGroupList(){
        ArrayList<Group> groups = userInfo.getGroups();

        for (Group group:groups) {
            createGroupChat(group.getGroupName());
            HBox groupHBox = createGroupHBox(group);
            friendList.getChildren().add(groupHBox);
        }
    }

    @FXML
    /**
     * Send the message to server , The information sent is divided into two categories,
     * common message and group message.
     */
    private void send(){

        if(!inputText.getText().isEmpty()){
            String type = speakWith.getText();
            if(type.substring(type.length()-5).equals("Group")){
                System.out.println("this is group");

                String content = inputText.getText();

                Message group_message = Message.builder()
                        .mesType("group_message")
                        .sender(userInfo.getAccount())
                        .getter(friendId1)
                        .con(content)
                        .group(group1)
                        .getterList(group1.getMemberName()).build();

                ccst.sendToServer(group_message);

                inputText.clear();

                displayText.appendText(accountId.getText() + " say: \n");
                displayText.appendText(content + "\n");
                displayText.appendText("\n");

            }else {

                String content = inputText.getText();

                Message commonMessage = Message.builder()
                        .mesType("common_Message")
                        .sender(userInfo.getAccount())
                        .getter(friendId1)
                        .con(content).build();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
                String time = sdf.format(new Date());
                commonMessage.setSendTime(time);

                ccst.sendToServer(commonMessage);


                inputText.clear();

                displayText.appendText(commonMessage.getSendTime() + " " + accountId.getText() + " say: \n");
                displayText.appendText(content + "\n");
                displayText.appendText("\n");

            }
        }

    }

    @SneakyThrows
    private void refresh(){
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/JavaFx/IndexView.fxml"));
        Parent root = Loader.load();
        IndexView IndexController = Loader.getController();
        manageObject.addObject("indexView",IndexController);
        stage = (Stage) ((Node)friendList).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setIsOk(boolean a){
        this.isOk = a;
    }

    @FXML
    /**
     * Cleaning all the characters in AreaText.
     */
    private void clearAll(){
        inputText.clear();
    }
}
