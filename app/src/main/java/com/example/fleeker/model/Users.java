package com.example.fleeker.model;

public class Users {
    private String user_name, user_profilePic, user_email,user_username,user_password,user_lastmessage, user_usernameReal;
    private int postCount, linkCount;
    private String verified;

    public Users(){}

    public Users(String name, String imageuri, String emailId, String user_id, String newPassword, String user_usernameReal, String verified){
        this.user_name = name;
        this.user_profilePic = imageuri;
        this.user_email = emailId;
        this.user_username = user_id;
        this.user_password = newPassword;
        this.user_usernameReal = user_usernameReal;
        this.verified = verified;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profilePic() {
        return user_profilePic;
    }

    public void setUser_profilePic(String user_profilePic) {
        this.user_profilePic = user_profilePic;
    }

    public String getUser_email() {
        return user_email;
    }


    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }


    public String getUser_username() {
        return user_username;
    }
    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }


    public String getUser_password() {
        return user_password;
    }
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }



    public String getUser_lastmessage() {
        return user_lastmessage;
    }

    public void setUser_lastmessage(String user_lastmessage) {
        this.user_lastmessage = user_lastmessage;
    }


    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public String getUser_usernameReal() {
        return user_usernameReal;
    }

    public void setUser_usernameReal(String user_usernameReal) {
        this.user_usernameReal = user_usernameReal;
    }

    public int getLinkCount() {
        return linkCount;
    }

    public void setLinkCount(int linkCount) {
        this.linkCount = linkCount;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
}
