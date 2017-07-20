package mudit.com.myproject.DatabseInfo;

/**
 * Created by admin on 7/8/2017.
 */

public class Message {
    private String name;
    private String message;
    private String status;
    public Message() {
    }

    public Message(String name, String message,String status) {
        this.name = name;
        this.message = message;
        this.status=status;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }
}
