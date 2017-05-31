package at.sw2017.q_up;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by PS on 31.05.17.
 */


public class TestHelperUtils {

    public static final String TESTUSER_NAME = "testCaseUser";
    public static final String TESTUSER_PW = "lalala";

    public static final String TESTPLACE_NAME= "testplace";

    private String testplace_id = "";
    private String testuser_id = "";

    public DatabaseHandler db_handle;

    public TestHelperUtils() {
        // get DB handler
        this.db_handle = QUpApp.getInstance().getDBHandler();
    }

    public boolean removeAllTestplaces() {

        List<String> oldtestplaces_to_remove = new ArrayList<>();

        this.db_handle.placesLock();
        for (Place p : this.db_handle.getPlacesList()) {
            if (p.placeName.equals(TESTPLACE_NAME)) {
                oldtestplaces_to_remove.add(p.placeId);
            }
        }
        this.db_handle.placesUnlock();

        for (String id : oldtestplaces_to_remove) {
            this.db_handle.removePlace(id);
        }

        return true;
    }

    public boolean removeAllTestUsers() {

        List<String> oldtestusers_to_remove = new ArrayList<>();

        this.db_handle.usersLock();
        for (User u : this.db_handle.getUsersList()) {
            if (u.userName.equals(TESTUSER_NAME)) {
                oldtestusers_to_remove.add(u.userId);
            }
        }
        this.db_handle.usersUnlock();

        for (String id : oldtestusers_to_remove) {
            this.db_handle.removeUser(id);
        }

        return true;
    }

    // returns true if everything worked correctly
    public boolean beforeEachTest() {
        // forget about logged in users
        SaveSharedPreference.setUserName(QUpApp.getContext(), "");

        // get existing places from db
        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            if (!this.db_handle.isPlacesListEmpty())
                break;
        }
        if (this.db_handle.isPlacesListEmpty())
            return false;

        // try to remove all old testplaces
        removeAllTestplaces();

        // create new testplace

        // add a place at center of camera: 47.0707, 15.4395
        this.db_handle.addPlace(TESTPLACE_NAME, "47.0707", "15.4395", "0", "0", "10", "www.testplace.at", "0-2", "testplaceStreet1");

        // look for testplace in list
        startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            this.db_handle.placesLock();
            for (Place p : this.db_handle.getPlacesList()) {
                if (p.placeName.equals(TESTPLACE_NAME)) {
                    this.testplace_id = p.placeId;
                    break;
                }
            }
            this.db_handle.placesUnlock();
        }
        if (this.testplace_id.equals(""))
            return false;

        // try to remove all old testusers
        removeAllTestUsers();

        // create testuser
        this.db_handle.addUser(TESTUSER_NAME, TESTUSER_PW);

        startTime = System.currentTimeMillis(); //fetch starting time
        boolean finished = false;
        while(!finished && (System.currentTimeMillis()-startTime) < 5000) {
            this.db_handle.usersLock();
            for (User u : this.db_handle.getUsersList()) {
                if (u.userName.equals(TESTUSER_NAME)) {
                    finished = true;
                    this.testuser_id = u.userId;
                    break;
                }
            }
            this.db_handle.usersUnlock();
        }
        if (!finished) {
            return false;
        }

        return true;
    }

    public boolean afterEachTest() {

        this.db_handle.removePlace(this.testplace_id);
        this.db_handle.removeUser(this.testuser_id);

        // dismiss saved user
        SaveSharedPreference.setUserName(QUpApp.getContext(), "");

        return true;
    }
}
