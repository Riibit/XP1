package at.sw2017.q_up;


public class User {
    public String userId;
    public String userName;
    public String password;
    public String idCheckInPlace;

    public User() {

    }

    public User(String id, String name, String pw, String chinplace) {
        this.userId = id;
        this.userName = name;
        this.password = pw;
        this.idCheckInPlace = chinplace;
    }
}
