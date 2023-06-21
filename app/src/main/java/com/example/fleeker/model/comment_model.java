package com.example.fleeker.model;

public class comment_model {
    private String comment, commentedBy;
    long time;


    public comment_model(String comment, long time, String commentedBy) {
        this.comment = comment;
        this.time = time;
        this.commentedBy = commentedBy;
    }

    public comment_model() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }
}
