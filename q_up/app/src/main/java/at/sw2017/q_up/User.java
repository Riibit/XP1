package at.sw2017.q_up;

/**
 * Created by tinag on 29.3.2017..
 */

public class User {
    public String userId;
    public String userName;
    public String password;
    public String idCheckInPlace; // ID of place where user is checked in
    public String idLastCheckInPlace; // ID of place where user last checked in

    public User() {

    }

    public User(String id, String name, String pw, String chinplace) {
        this.userId = id;
        this.userName = name;
        this.password = pw;
        this.idCheckInPlace = chinplace;
        this.idLastCheckInPlace = "";
    }
}
