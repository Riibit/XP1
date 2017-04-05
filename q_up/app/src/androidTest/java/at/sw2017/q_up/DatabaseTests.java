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
        int bla = 0;
        bla += 1;
        while (QUpApp.getInstance().getDBHandler().getSignInComplete() != true)
        {
            bla += 1;
        }
        bla += 1;
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

        List<Place> places = db_handle.getPlacesList();
        assertTrue(!places.isEmpty());
    }

    @Test
    public void loadUsers() {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        int result = db_handle.readUsersFromDB();
        assertEquals(0, result);

        try {
            db_handle.getGetUsersLatch().await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //e.printStackTrace();
            assertEquals(0, 1);
        }

        List<User> users = db_handle.getUsersList();
        assertTrue(!users.isEmpty());
    }
}
