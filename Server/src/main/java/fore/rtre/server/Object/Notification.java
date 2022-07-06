package fore.rtre.server.Object;

public class Notification {

    private Long postId;
    private boolean read;
    private String userId;
    private boolean query;
    private String queryName;
    private String queryTopic;

    public Notification(Long postId, boolean read, String userId) {
        this.postId = postId;
        this.read = read;
        this.userId = userId;
    }

    public Notification(Long postId, boolean read, String userId, boolean query, String queryName, String queryTopic) {
        this.postId = postId;
        this.read = read;
        this.userId = userId;
        this.query = query;
        this.queryName = queryName;
        this.queryTopic = queryTopic;
        
    }

    public void setQueryTopic(String queryTopic) {
        this.queryTopic = queryTopic;
    }
    public String getQueryTopic() {
        return queryTopic;
    }
    public void setQuery(boolean query) {
        this.query = query;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }
    public String getQueryName() {
        return queryName;
    }
    public boolean query() {
        return query;
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
