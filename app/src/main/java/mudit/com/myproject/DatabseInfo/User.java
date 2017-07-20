package mudit.com.myproject.DatabseInfo;

/**
 * Created by admin on 7/7/2017.
 */

public class User {
    String username;
    String email;

    public User(){

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}
