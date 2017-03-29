package at.sw2017.q_up;

/**
 * Created by PS on 29.03.17.
 */

import java.util.ArrayList;
import java.util.List;
import at.sw2017.q_up.Place;

public class DatabaseHandler {

    // these must be filled from config file:
    private String server_url;
    private String server_user;
    private String server_pw;
    private String server_dbname;
    private String server_table_users;
    private String server_table_places;

    // variables to cache server data
    List<Place> placesList = new ArrayList<Place>();

    public DatabaseHandler() {
        // TODO load config file
        // ...
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
    public Integer readPlaces() {
        // TODO read table

        return -1;
    }
}
