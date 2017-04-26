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

//        int result = QUpApp.getInstance().getDBHandler().readUsersFromDB();
//        QUpApp.getInstance().getDBHandler().waitUsersComplete(20);
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

/* // disabled - spamming :)
    @Test
    public void addUser() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        int result = db_handle.addUser("testuser", "password");
        assertEquals(0, result);
    }
*/

/* // disabled - spamming :)
    @Test
    public void addTestPlaces() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        if (db_handle.getPlacesList().isEmpty()) {

            int result = db_handle.readPlacesFromDB();
            assertEquals(0, result);
            db_handle.waitPlacesComplete(20);
        }

        int result = db_handle.addPlace("bar", "47.06", "15.4639", "0.0", "10");
        assertEquals(0, result);
        result = db_handle.addPlace("market", "47.0608", "15.4682", "0.0", "15");
        assertEquals(0, result);
    }
*/

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
        assertNotEquals(id, "");

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

    @Test
    public void getUserAttribute() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        int result = db_handle.readUsersFromDB();
        assertEquals(0, result);
        db_handle.waitUsersComplete(10);

        String id = "";
        for (User u : db_handle.getUsersList()) {
            if (u.userName.equals("franz"));
            id = u.userId;
        }
        assertNotEquals(id, "");

        String getvalue = db_handle.getUserAttribute(id, "password");
        assertEquals(getvalue, "password");
    }
}
