package com.example.chatapps.Models;

public class Users {
    String imageURL, userName,email,password,userId,lastMessage,status;
    String search;

    public Users(String imageURL, String userName, String email, String password, String userId, String lastMessage,String status,String search) {
        this.imageURL = imageURL;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.status = status;
        this.search = search;
    }
    public Users()
    {

    }
    public Users(String userName, String email, String password,String imageURL,String userId,String status,String search)
    {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.imageURL = imageURL;
        this.userId = userId;
        this.status = status;
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getimageURL() {
        return imageURL;
    }

    public void setimageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

}

