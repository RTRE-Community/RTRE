package com.example.demo.Object;

public class Notification {

    private String postId;
    private boolean read;
    private String userId;

    public Notification(String postId, boolean read, String userId) {
        this.postId = postId;
        this.read = read;
        this.userId = userId;
    }

    public Notification() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
