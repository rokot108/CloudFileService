package AuthService;

import java.io.Serializable;

public class UserAccount implements Serializable {

    private String username;
    private int passHash;

    public UserAccount(String username, String pass) {
        this.username = username;
        this.passHash = pass.hashCode();
    }

    public UserAccount(String username, int passHash) {
        this.username = username;
        this.passHash = passHash;
    }

    public String getUsername() {
        return username;
    }

    public int getPassHash() {
        return passHash;
    }
}
