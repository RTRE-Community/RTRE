package fore.rtre.server.Object;

public class Notification {

    private Long postId;
    private boolean read;
    private String userId;

    public Notification(Long postId, boolean read, String userId) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
