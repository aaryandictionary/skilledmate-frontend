package com.app.miniproject.Models;

public class ChatModel {

    private String userProfile,userName,userLastMessage,userLastSeen;

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastMessage() {
        return userLastMessage;
    }

    public void setUserLastMessage(String userLastMessage) {
        this.userLastMessage = userLastMessage;
    }

    public String getUserLastSeen() {
        return userLastSeen;
    }

    public void setUserLastSeen(String userLastSeen) {
        this.userLastSeen = userLastSeen;
    }
}
