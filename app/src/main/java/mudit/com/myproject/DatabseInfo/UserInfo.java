package mudit.com.myproject.DatabseInfo;

/**
 * Created by admin on 7/7/2017.
 */

public class UserInfo {
    String username;
    String email;
    String password;

    public UserInfo() {
    }

    public UserInfo(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
