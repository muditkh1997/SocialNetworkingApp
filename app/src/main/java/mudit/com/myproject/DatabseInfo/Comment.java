package mudit.com.myproject.DatabseInfo;

/**
 * Created by admin on 7/9/2017.
 */

public class Comment {

    private String uid;
    private String author;
    private String text;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text) {
        this.uid = uid;
        this.author = author;
        this.text = text;
    }

    public String getUid() {
        return uid;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }
}