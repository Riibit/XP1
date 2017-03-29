package at.sw2017.q_up;

/**
 * Created by PS on 29.03.17.
 */

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import at.sw2017.q_up.Place;


public class DatabaseHandler {

    private DatabaseConfig db_config;
    private String server_table_users;
    private String server_table_places;

    // variables to cache server data
    List<Place> placesList = new ArrayList<Place>();
    List<User> usersList = new ArrayList<User>();

    boolean finished = false;

    /**
     * constructor
     */
    public DatabaseHandler() {
        // load config file
        db_config = new DatabaseConfig();
        try {
            db_config.loadConfigValues();
        } catch (IOException e) {
            System.out.println("Loading config failed!");
            e.printStackTrace();
        }
    }

    /**
     * @brief log into the database server
     * @return 0 = OK ; <0 = error
     */
    public Integer login() {
        // TODO login server
/*
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("scott");
        dataSource.setPassword("tiger");
        dataSource.setServerName("myDBHost.example.org");
*/
        //try {
        //} catch (ClassNotFoundException e) {
        //    e.printStackTrace();
        //}
        /*
        Connection con = null;
        Statement stmt= null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + db_config.getUrl() + "/" + db_config.getDBname();
            con = DriverManager.getConnection(url, db_config.getUser(), db_config.getPw());
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from users");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        */


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();

        DatabaseReference usersref = mDatabase.child("users");


        usersref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("hi");
                List Userlist = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(String.valueOf(dsp.getValue()));
                    System.out.println("hi");
                }
                finished = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println("ho");
                finished = true;
            }
        });

        while(!finished){

        }

        return -1;
    }

    /**
     * @brief read places table
     * @return 0 = OK ; <0 = error
     */
    public Integer readPlacesFromDB() {
        // TODO read table

        return -1;
    }

    /**
     * @brief get cached places
     * @return list of places
     */
    public List<Place> getPlacesList() {
        return placesList;
    }

    /**
     * @brief read users table
     * @return 0 = OK ; <0 = error
     */
    public Integer readUsersFromDB() {
        // TODO read table

        return -1;
    }

    /**
     * @brief get cached users
     * @return list of users
     */
    public List<User> getUsersList() {
        return usersList;
    }
}
