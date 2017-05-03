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

        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            if (!db_handle.getPlacesList().isEmpty())
                break;
        }
        assertEquals(false, db_handle.getPlacesList().isEmpty());
    }

    @Test
    public void loadUsers() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            if (!db_handle.getUsersList().isEmpty())
                break;
        }
        assertEquals(false, db_handle.getUsersList().isEmpty());
    }

    @Test
    public void addUser() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        // get existing users from db
        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            if (!db_handle.getUsersList().isEmpty())
                break;
        }
        assertEquals(false, db_handle.getUsersList().isEmpty());

        // add a user
        int result = db_handle.addUser("testuser", "testpassword");
        assertEquals(0, result);

        startTime = System.currentTimeMillis(); //fetch starting time
        String testuser_id = "";
        while(testuser_id == "" && (System.currentTimeMillis()-startTime) < 5000) {
            // look for testuser in list
            for (User u : db_handle.getUsersList()) {
                if (u.userName.equals("testuser")) {
                    if (u.password.equals("testpassword")) {
                        testuser_id = u.userId;
                        break;
                    }
                }
            }
        }
        assertNotEquals("", testuser_id);

        // remove user again
        result = db_handle.removeUser(testuser_id);
        assertEquals(0, result);

        startTime = System.currentTimeMillis(); //fetch starting time
        boolean object_found = true;
        while(object_found == true && (System.currentTimeMillis()-startTime) < 5000) {
            // look for testuser in list - it should be gone
            for (User u : db_handle.getUsersList()) {
                if (u.userName.equals("testuser")) {
                    if (u.password.equals("testpassword")) {
                        object_found = true;
                        break;
                    }
                    else
                        object_found = false;
                }
                else
                    object_found = false;
            }
        }
        assertEquals(false, object_found);
    }

    @Test
    public void addTestPlaces() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        // get existing places from db
        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            if (!db_handle.getPlacesList().isEmpty())
                break;
        }
        assertEquals(false, db_handle.getPlacesList().isEmpty());

        // add a place
        int result = db_handle.addPlace("testplace", "12.06", "34.4639", "0.0", "10");
        assertEquals(0, result);

        // look for testplace in list
        startTime = System.currentTimeMillis(); //fetch starting time
        String testplace_id = "";
        while((System.currentTimeMillis()-startTime) < 5000) {
            for (Place p : db_handle.getPlacesList()) {
                if (p.placeName.equals("testplace")) {
                    testplace_id = p.placeId;
                    break;
                }
            }
        }
        assertNotEquals("", testplace_id);

        // remove place again
        result = db_handle.removePlace(testplace_id);
        assertEquals(0, result);

        startTime = System.currentTimeMillis(); //fetch starting time
        boolean object_found = true;

        while(object_found && (System.currentTimeMillis()-startTime) < 5000) {
            // look for object in list - it should be gone
            for (Place p : db_handle.getPlacesList()) {
                if (p.placeName.equals("testplace")) {
                    object_found = true;
                    break;
                }
                else
                    object_found = false;
            }
        }
        assertEquals(false, object_found);

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

        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            if (!db_handle.getUsersList().isEmpty())
                break;
        }
        assertEquals(false, db_handle.getUsersList().isEmpty());

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

        // check new value and reset to old value - all must happen in a few seconds
        startTime = System.currentTimeMillis(); //fetch starting time
        boolean value_changed = false;

        while(!value_changed && (System.currentTimeMillis()-startTime) < 5000) {
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
