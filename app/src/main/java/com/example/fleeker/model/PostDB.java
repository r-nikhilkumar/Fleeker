package com.example.fleeker.model;

public class PostDB {
    String createpostImage, createpostDescription, postedByID, postID;
    long postdatetime;
    int likesCount, commentCount;
    public PostDB(){}

    public PostDB(String createpostImage, String createpostDescription, String postedByID,long postdatetime,int likesCount, int commentCount ) {
        this.createpostImage = createpostImage;
        this.createpostDescription = createpostDescription;
        this.postedByID = postedByID;
        this.postdatetime=postdatetime;
        this.likesCount = likesCount;
        this.commentCount = commentCount;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getCreatepostImage() {
        return createpostImage;
    }

    public void setCreatepostImage(String createpostImage) {
        this.createpostImage = createpostImage;
    }

    public String getCreatepostDescription() {
        return createpostDescription;
    }

    public void setCreatepostDescription(String createpostDescription) {
        this.createpostDescription = createpostDescription;
    }

    public String getPostedByID() {
        return postedByID;
    }

    public void setPostedByID(String postedByID) {
        this.postedByID = postedByID;
    }

    public long getPostdatetime() {
        return postdatetime;
    }

    public void setPostdatetime(long postdatetime) {
        this.postdatetime = postdatetime;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
