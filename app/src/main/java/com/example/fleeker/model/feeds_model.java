package com.example.fleeker.model;

public class feeds_model {

    private String feedName, feedProfile, postDescription, postImage, feedPostID;
    private int likesCount, commentCount;
    private long timedate;

    public feeds_model(){}

    public feeds_model(String feedName, String feedProfile, String postDescription, String postImage) {
        this.feedName = feedName;
        this.feedProfile = feedProfile;
        this.postDescription = postDescription;
        this.postImage = postImage;
    }

    public String getFeedPostID() {
        return feedPostID;
    }

    public void setFeedPostID(String feedPostID) {
        this.feedPostID = feedPostID;
    }

    public String getFeedName() {
        return feedName;
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

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedProfile() {
        return feedProfile;
    }

    public void setFeedProfile(String feedProfile) {
        this.feedProfile = feedProfile;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public long getTimedate() {
        return timedate;
    }

    public void setTimedate(long timedate) {
        this.timedate = timedate;
    }
}
