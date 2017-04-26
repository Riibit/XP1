package at.sw2017.q_up;


import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.IntegerRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by chibby on 29.03.17.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTests {

    @BeforeClass
    public static void initTestCase() {
        // wait for the sign in process to complete
        QUpApp.getInstance().getDBHandler().waitSignInComplete(20);
        QUpApp.getInstance().getDBHandler().waitPlacesComplete(10);
        QUpApp.getInstance().getDBHandler().waitUsersComplete(10);
    }

    @Test
    public void loadDatabaseHandler() {

        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);
    }

    @Test
    public void loadPlaces() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        if (db_handle.getPlacesList().isEmpty()) {

            int result = db_handle.readPlacesFromDB();
            assertEquals(0, result);
            db_handle.waitPlacesComplete(20);
        }

        List<Place> places = db_handle.getPlacesList();
        assertTrue(!places.isEmpty());
    }

    @Test
    public void loadUsers() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        if (db_handle.getUsersList().isEmpty()) {

            int result = db_handle.readUsersFromDB();
            assertEquals(0, result);
            db_handle.waitUsersComplete(20);
        }
        /* example of accessing users:

        List<User> users = db_handle.getUsersList();
        for (User u : users) {
            if (u.userName.equals("franz"))
                checkpassword
        }
        assertTrue(!users.isEmpty()); */
    }

    @Test
    public void addUser() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        // get existing users from db
        int result = db_handle.readUsersFromDB();
        assertEquals(0, result);
        db_handle.waitUsersComplete(20);

        // add a user
        result = db_handle.addUser("testuser", "testpassword");
        assertEquals(0, result);
        db_handle.waitUsersComplete(20);

        // verify that list is not empty
        List<User> users = db_handle.getUsersList();
        assert(!users.isEmpty());

        // look for testuser in list
        String testuser_id = "";
        for (User u : users) {
            if (u.userName.equals("testuser")) {
                if (u.password.equals("testpassword")) {
                    testuser_id = u.userId;
                    break;
                }
            }
        }
        assertNotEquals("", testuser_id);

        // remove user again
        result = db_handle.removeUser(testuser_id);
        assertEquals(0, result);
        db_handle.waitUsersComplete(20);

        users = db_handle.getUsersList();

        // look for testuser in list - it should be gone
        boolean user_found = false;
        for (User u : users) {
            if (u.userName.equals("testuser")) {
                if (u.password.equals("testpassword")) {
                    user_found = true;
                    break;
                }
            }
        }
        assertEquals(false, user_found);
    }

    @Test
    public void addTestPlaces() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        // get existing places from db
        int result = db_handle.readPlacesFromDB();
        assertEquals(0, result);
        db_handle.waitPlacesComplete(20);

        // add a place
        result = db_handle.addPlace("testplace", "12.06", "34.4639", "0.0", "10");
        assertEquals(0, result);
        db_handle.waitPlacesComplete(20);

        // verify that list is not empty
        List<Place> places = db_handle.getPlacesList();
        assert(!places.isEmpty());

        // look for testplace in list
        String testplace_id = "";
        for (Place p : places) {
            if (p.placeName.equals("testplace")) {
                testplace_id = p.placeId;
                break;
            }
        }
        assertNotEquals("", testplace_id);

        // remove place again
        result = db_handle.removePlace(testplace_id);
        assertEquals(0, result);
        db_handle.waitPlacesComplete(20);

        places = db_handle.getPlacesList();

        // look for testuser in list - it should be gone
        boolean place_found = false;
        for (Place p : places) {
            if (p.placeName.equals("testplace")) {
                place_found = true;
                break;
            }
        }
        assertEquals(false, place_found);

/*
        int result = db_handle.addPlace("bar", "47.06", "15.4639", "0.0", "10");
        assertEquals(0, result);
        result = db_handle.addPlace("market", "47.0608", "15.4682", "0.0", "15");
        assertEquals(0, result);
*/
    }

    @Test
    public void queueUser() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        if (db_handle.getUsersList().isEmpty()) {

            int result = db_handle.readUsersFromDB();
            assertEquals(0, result);
            db_handle.waitUsersComplete(20);
        }

        String id = "";
        String old_idCheckInPlace = "";
        List<User> userlist = db_handle.getUsersList();

        for (User u : userlist) {
            if (u.userName.equals("franz")) {
                id = u.userId;
                old_idCheckInPlace = u.idCheckInPlace;
                break;
            }
        }
        assertNotEquals("", id);

        String new_idCheckInPlace = String.valueOf(Integer.parseInt(old_idCheckInPlace) + 1);
        int result = db_handle.modifyUserAttribute(id, "idCheckInPlace", new_idCheckInPlace);

        // check new value and reset to old value - all must happen in 10s
        long startTime = System.currentTimeMillis(); //fetch starting time
        boolean value_changed = false;

        while(!value_changed || (System.currentTimeMillis()-startTime) < 10000) {
            userlist = db_handle.getUsersList();
            for (User u : userlist) {
                if (u.userName.equals("franz")) {
                    if (u.idCheckInPlace.equals(new_idCheckInPlace)) {
                        value_changed = true;
                        db_handle.modifyUserAttribute(id, "idCheckInPlace", old_idCheckInPlace);
                    }
                    break;
                }
            }
        }
        assertEquals(0, result);
        assertEquals(value_changed, true);
    }
}
