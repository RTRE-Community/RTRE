package com.example.demo.Object;

public class Notification {

    private Long postId;
    private boolean read;
    private Long userId;

    public Notification(Long postId, boolean read, Long userId) {
        this.postId = postId;
        this.read = read;
        this.userId = userId;
    }

    public Notification() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
