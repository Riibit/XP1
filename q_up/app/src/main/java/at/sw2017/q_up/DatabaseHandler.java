package at.sw2017.q_up;

/**
 * Created by PS on 29.03.17.
 */

import java.io.IOException;
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

    public DatabaseHandler() {
        // TODO load config file
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
