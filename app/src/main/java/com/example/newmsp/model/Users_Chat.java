package com.example.newmsp.model;

public class Users_Chat {


    String username, imageURL, id, status;

    public Users_Chat(String username, String imageURL, String id, String status) {
        this.username = username;
        this.imageURL = imageURL;
        this.id = id;
        this.status = status;
    }

    public Users_Chat() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
