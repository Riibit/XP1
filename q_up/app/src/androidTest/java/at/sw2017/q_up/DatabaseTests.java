package at.sw2017.q_up;


import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.IntegerRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by PS on 29.03.17.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class DatabaseTests {


    private SimpleIdlingResource placesIdlingResource;
    private SimpleIdlingResource usersIdlingResource;

    //@Rule
    //public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initTestCase() {
        Log.d("TestDB", "BeforeClass");
        // wait for the sign in process to complete
        QUpApp.getInstance().getDBHandler().waitSignInComplete(10);
        QUpApp.getInstance().getDBHandler().waitPlacesComplete(10);
        QUpApp.getInstance().getDBHandler().waitUsersComplete(10);
    }

    @Before
    public void registerIntentServiceIdlingResource() {
        Log.d("TestDB", "Before");
        //Activity activity = activityTestRule.getActivity();

        placesIdlingResource = QUpApp.getInstance().getDBHandler().getPlacesIdlingResource();
        Espresso.registerIdlingResources(placesIdlingResource);
        usersIdlingResource = QUpApp.getInstance().getDBHandler().getUsersIdlingResource();
        Espresso.registerIdlingResources(usersIdlingResource);
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Log.d("TestDB", "After");
        Espresso.unregisterIdlingResources(placesIdlingResource);
        Espresso.unregisterIdlingResources(usersIdlingResource);
    }

    @Test
    public void loadDatabaseHandler() {
        Log.d("TestDB", "  loadDatabaseHandler()");

        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);
    }

    @Test
    public void loadPlaces() {
        Log.d("TestDB", "  loadPlaces()");
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
        Log.d("TestDB", "  loadUsers()");
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
        Log.d("TestDB", "  addUser()");
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
            db_handle.usersLock();
            for (User u : db_handle.getUsersList()) {
                if (u.userName.equals("testuser")) {
                    if (u.password.equals("testpassword")) {
                        testuser_id = u.userId;
                        break;
                    }
                }
            }
            db_handle.usersUnlock();
        }
        assertNotEquals("", testuser_id);

        // remove user again
        result = db_handle.removeUser(testuser_id);
        assertEquals(0, result);

        startTime = System.currentTimeMillis(); //fetch starting time
        boolean object_found = true;
        while(object_found == true && (System.currentTimeMillis()-startTime) < 5000) {
            // look for testuser in list - it should be gone
            db_handle.usersLock();
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
            db_handle.usersUnlock();
        }
        assertEquals(false, object_found);
    }

    @Test
    public void addRemoveTestPlace() {
        Log.d("TestDB", "  addRemoveTestPlace()");
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        // get existing places from db
        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 10000) {
            if (!db_handle.getPlacesList().isEmpty())
                break;
        }
        assertEquals(false, db_handle.getPlacesList().isEmpty());

        // add a place
        int result = db_handle.addPlace("testplace", "12.06", "34.4639", "0", "0", "10");
        assertEquals(0, result);

        // look for testplace in list
        startTime = System.currentTimeMillis(); //fetch starting time
        String testplace_id = "";
        while((System.currentTimeMillis()-startTime) < 5000) {
            db_handle.placesLock();
            for (Place p : db_handle.getPlacesList()) {
                if (p.placeName.equals("testplace")) {
                    testplace_id = p.placeId;
                    break;
                }
            }
            db_handle.placesUnlock();
        }
        assertNotEquals("", testplace_id);

        // remove place again
        result = db_handle.removePlace(testplace_id);
        assertEquals(0, result);

        startTime = System.currentTimeMillis(); //fetch starting time
        boolean object_found = true;

        while(object_found && (System.currentTimeMillis()-startTime) < 5000) {
            // look for object in list - it should be gone
            db_handle.placesLock();
            for (Place p : db_handle.getPlacesList()) {
                if (p.placeName.equals("testplace")) {
                    object_found = true;
                    break;
                }
                else
                    object_found = false;
            }
            db_handle.placesUnlock();
        }
        assertEquals(false, object_found);
    }

    @Test
    public void addTestPlaces() {
        Log.d("TestDB", "  addTestPlaces()");
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        // try to remove all testplaces
        List<String> oldtestplaces_to_remove = new ArrayList<String>();
        db_handle.placesLock();
        for (Place p : db_handle.getPlacesList()) {
            if (p.placeName.equals("testplace")) {
                oldtestplaces_to_remove.add(p.placeId);
            }
        }
        db_handle.placesUnlock();
        for (String id : oldtestplaces_to_remove) {
            db_handle.removePlace(id);
        }

        long startTime = System.currentTimeMillis(); //fetch starting time
        boolean finished = true;
        while(!finished && (System.currentTimeMillis()-startTime) < 5000) {
            db_handle.placesLock();
            for (Place p : db_handle.getPlacesList()) {
                if (p.placeName.equals("testplace"))
                    finished = false;
            }
            db_handle.placesUnlock();
        }
        assertEquals(true, finished);

        // add a dummy place
        int result = db_handle.addPlace("testplace", "12.06", "34.4639", "0", "0", "10");
        assertEquals(0, result);

        // wait for the creation
        startTime = System.currentTimeMillis(); //fetch starting time
        finished = false;
        while(!finished && (System.currentTimeMillis()-startTime) < 5000) {
            db_handle.placesLock();
            for (Place p : db_handle.getPlacesList()) {
                if (p.placeName.equals("testplace"))
                    finished = true;
            }
            db_handle.placesUnlock();
        }
        assertEquals(true, finished);

        // clear out existing testplaces from db
        startTime = System.currentTimeMillis(); //fetch starting time
        finished = false;
        List<String> testplaces_to_remove = new ArrayList<String>();
        while(!finished && (System.currentTimeMillis()-startTime) < 10000) {
            db_handle.placesLock();
            for (Place p : db_handle.getPlacesList()) {
                if (p.placeName.equals("testplace")) {
                    finished = true;
                    testplaces_to_remove.add(p.placeId);
                }
            }
            db_handle.placesUnlock();
            for (String id : testplaces_to_remove) {
                db_handle.removePlace(id);
            }
        }
        assertEquals(true, finished);

        //=== ADD TESTPLACES FOR DATABASE HERE =====================================================

        List<Place> testplaces = new ArrayList<Place>();
        testplaces.add(new Place("", "Pail Coffee", "47.06", "15.4639", "0", "0", "10"));
        testplaces.add(new Place("", "Spar Market", "47.0608", "15.4682", "0", "0", "15"));
        testplaces.add(new Place("", "McDonalds", "47.055496", "15.448409", "0", "0", "15"));
        testplaces.add(new Place("", "Davinci", "47.054160", "15.444241", "0", "0", "15"));
        testplaces.add(new Place("", "Hofer", "47.055717", "15.441392", "0", "0", "15"));

        //==========================================================================================

        // go through all places in the database
        List<String> places_to_remove = new ArrayList<String>();
        db_handle.placesLock();
        for (Place dbP : db_handle.getPlacesList()) {
            // for each place in the database - look through our local testplaces
            for (Place localP : testplaces) {
                // find duplicates and remove them
                if (localP.placeName.equals(dbP.placeName)) {
                    places_to_remove.add(dbP.placeId);
                    // do not break here - find duplicate entries!
                }
            }
        }
        db_handle.placesUnlock();

        // actually remove the places after we are finished iterating the list
        for (String id : places_to_remove) {
            db_handle.removePlace(id);
        }

        // add our testplaces
        for (Place p : testplaces) {
            db_handle.addPlace(p.placeName, p.latitude, p.longitude, p.ratingPos, p.ratingNeg, p.avgProcessingSecs);
        }

        startTime = System.currentTimeMillis(); //fetch starting time
        while(!testplaces.isEmpty() && (System.currentTimeMillis()-startTime) < 10000) {
            for (Place p : db_handle.getPlacesList()) {
                for (Iterator<Place> it = testplaces.iterator(); it.hasNext(); ){
                    //    Place tp : testplaces) {
                    Place tp = it.next();
                    if (p.placeName.equals(tp.placeName)) {
                        it.remove();
                        break;
                    }
                }
            }
        }
        assertEquals(true, testplaces.isEmpty());
    }

    @Test
    public void queueUser() {
        Log.d("TestDB", "  queueUser()");
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

        db_handle.usersLock();
        for (User u : db_handle.getUsersList()) {
            if (u.userName.equals("franz")) {
                id = u.userId;
                old_idCheckInPlace = u.idCheckInPlace;
                break;
            }
        }
        db_handle.usersUnlock();

        assertNotEquals("", id);

        String new_idCheckInPlace = "test";
        int result = db_handle.modifyUserAttribute(id, "idCheckInPlace", new_idCheckInPlace);

        // check new value and reset to old value - all must happen in a few seconds
        startTime = System.currentTimeMillis(); //fetch starting time
        boolean value_changed = false;

        while(!value_changed && (System.currentTimeMillis()-startTime) < 5000) {
            db_handle.usersLock();
            for (User u : db_handle.getUsersList()) {
                if (u.userName.equals("franz")) {
                    if (u.idCheckInPlace.equals(new_idCheckInPlace)) {
                        value_changed = true;
                        db_handle.modifyUserAttribute(id, "idCheckInPlace", old_idCheckInPlace);
                    }
                    break;
                }
            }
            db_handle.usersUnlock();
        }
        assertEquals(0, result);
        assertEquals(value_changed, true);
    }
}
