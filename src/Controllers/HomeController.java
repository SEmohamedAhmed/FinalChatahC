package Controllers;

import cells.UserListCell;
import chatPack.App;
import chatPack.ChatRoom;
import chatPack.Message;
import chatPack.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import static Controllers.Main.app;
import static Controllers.Main.utilities;

public class HomeController implements Initializable
{
//    public Stage stage;
//    public Scene scene;
//    public Parent root;
    public static User HomeUser;
    public HomeController() throws SQLException {
        if(LoginController.uLogin !=null)
            HomeUser=LoginController.uLogin;
        else if(SignUpController.uSignup!=null)
            HomeUser=SignUpController.uSignup;
    }
    @FXML
    Label chatRoomName;
    @FXML
    public ListView<UserListCell>usersListView;

    @FXML
    public ListView<Message>messagesListView;

    @FXML
    public TextField messageTextField;

    public ObservableList<UserListCell> usersList = FXCollections.observableArrayList();

   //public ObservableList<Message> messageList = FXCollections.observableArrayList();
    private UserListCell currentSelectedChatRoom;           //current open chatroom

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            prepareHomeScene();
            prepareHomeListsGui();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    public void sendMessage(ActionEvent actionEvent) throws SQLException
    {
        String msgText=messageTextField.getText();
        if(!msgText.equals(""))
        {
        app.sendMessage(HomeUser.getId(),currentSelectedChatRoom.getChatRoom().getId(), msgText);
        ArrayList<Message> messageArrayList=currentSelectedChatRoom.getChatRoom().getMessageList();
        messageTextField.clear();
        }
    }
    public void prepareHomeScene() throws SQLException
    {
        int userId = app.getUserIdFromUsername(HomeUser.getUsername());
        ArrayList<ChatRoom> chatRooms = app.expandConnectionChats(userId);
        ArrayList<UserListCell>allUserListCell=new ArrayList<>();
        for (int i=0;i<chatRooms.size();++i)
        {
            ChatRoom tempChatRoom=chatRooms.get(i);
            //prepare chatroom & all userListCells
            UserListCell userListCell=new UserListCell(tempChatRoom.getName(),tempChatRoom.getLastMessageSent().getMessageText(), tempChatRoom.getLastMessageSent().getTime(),tempChatRoom);
            ArrayList<Message> arrayList=tempChatRoom.getMessageList();
            ObservableList<Message> messageList = FXCollections.observableArrayList(arrayList);   //must be local not global
            userListCell.setMessagesList(messageList);
            allUserListCell.add(userListCell);
        }
        try
        {
            usersList.addAll(allUserListCell);
            usersListView.setItems(usersList);
            prepareHomeListsGui();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
    private void prepareHomeListsGui()
    {
        usersListView.setCellFactory(lv -> new UserCustomCellController() {
            {
                prefWidthProperty().bind(usersListView.widthProperty().subtract(0));
            }
        });
        messagesListView.setCellFactory(lv -> new MessageCustomCellController() {
            {
                prefWidthProperty().bind(messagesListView.widthProperty().subtract(0));
                lv.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getClickCount() == 2) {
                        Message message=lv.getSelectionModel().getSelectedItem();
                        try {
                            utilities.doubleClickEvent(mouseEvent,message);     //message status
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        usersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            messageTextField.setVisible(true);
            UserListCell currentlySelectedUser = usersListView.getSelectionModel().getSelectedItem();
            currentSelectedChatRoom=currentlySelectedUser;
            messagesListView.setItems(currentlySelectedUser.getMessagesList());
            messagesListView.scrollTo(currentlySelectedUser.getMessagesList().size());
            chatRoomName.setText(currentlySelectedUser.userName);
        });
    }
    public void gotoStoryPage(MouseEvent mouseEvent) throws IOException
    {
        utilities.gotoHere("../UI/story_page.fxml",mouseEvent);
    }
    public void gotoOptions(MouseEvent mouseEvent) throws IOException
    {
        utilities.gotoHere("../UI/Options _change_Profile_Description_Scene.fxml",mouseEvent);
        OptionsController optionsController=new OptionsController();
    }
    public void gotoProfileDesc(MouseEvent mouseEvent) throws IOException
    {
        utilities.gotoHere("../UI/Profile_Description_Subscene.fxml", mouseEvent);
    }
}
