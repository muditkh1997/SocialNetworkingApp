package mudit.com.myproject.DatabseInfo;

/**
 * Created by admin on 7/7/2017.
 */

public class Post {
    String uid;
    String body;
    String author;
    int starCount;
    String uri;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String body,String author, int starCount,String uri) {
        this.uid = uid;
        this.body = body;
        this.author=author;
        this.starCount = starCount;
        this.uri=uri;
    }

    public String getAuthor() {
        return author;
    }

    public String getUid() {
        return uid;
    }

    public String getBody() {
        return body;
    }

    public int getStarCount() {
        return starCount;
    }

    public String getUri() {
        return uri;
    }
}
