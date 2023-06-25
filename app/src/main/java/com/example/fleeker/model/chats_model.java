package com.example.fleeker.model;

public class chats_model {
    private String profileImage, name, email, uid, username;
//    public chats_model(){}

    public chats_model(String profileImage, String name, String email, String uid, String username) {
        this.profileImage = profileImage;
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
