package at.sw2017.q_up;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
        QUpApp.getInstance().getDBHandler().waitSignInComplete(10);
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

        int result = db_handle.readPlacesFromDB();
        assertEquals(0, result);

        db_handle.waitPlacesComplete(10);

        List<Place> places = db_handle.getPlacesList();
        assertTrue(!places.isEmpty());
    }

    @Test
    public void loadUsers() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        int result = db_handle.readUsersFromDB();
        assertEquals(0, result);

        db_handle.waitUsersComplete(10);

        List<User> users = db_handle.getUsersList();
        assertTrue(!users.isEmpty());
    }

    @Test
    public void addUser() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        int result = db_handle.addUser("testuser", "password");
        assertEquals(0, result);
    }

//    @Test
//    public void addPlace() {
//        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
//        assertNotNull(db_handle);
//
//        int result = db_handle.addPlace("testplace", 12.34, 23.45, 0.0, 10);
//        assertEquals(0, result);
//    }

    @Test
    public void queueUser() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        Integer id = 1;
        String attribute = "id_q_place";
        String value = "5";

        int result = db_handle.modifyUserAttribute(id, attribute, value);
        assertEquals(0, result);
    }
}
