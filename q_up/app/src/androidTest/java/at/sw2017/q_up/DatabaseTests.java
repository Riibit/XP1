package at.sw2017.q_up;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by chibby on 29.03.17.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTests {

    DatabaseHandler db_handle = null;

    @Test
    public void loadDatabaseHandler() {
        db_handle = new DatabaseHandler();

        assertNotNull(db_handle);
    }

    @Test
    public void connectDatabase() {
        if (db_handle == null)
            loadDatabaseHandler();
        assertNotNull(db_handle);

        int result = db_handle.login();
        assertEquals(0, result);
    }

    @Test
    public void loadPlaces() {
        if (db_handle == null)
            loadDatabaseHandler();
        assertNotNull(db_handle);

        int result = db_handle.readPlacesFromDB();
        assertEquals(0, result);

        List<Place> places = db_handle.getPlacesList();
        assertTrue(!places.isEmpty());
    }

    @Test
    public void loadUsers() {
        if (db_handle == null)
            loadDatabaseHandler();
        assertNotNull(db_handle);

        int result = db_handle.readUsersFromDB();
        assertEquals(0, result);

        List<User> users = db_handle.getUsersList();
        assertTrue(!users.isEmpty());
    }
}
