package at.sw2017.q_up;

import android.graphics.Rect;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
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
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by PS on 17.05.17.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class PlaceDetailsTest {

    private SimpleIdlingResource placesIdlingResource;
    private SimpleIdlingResource usersIdlingResource;

    private UiDevice device;
    private String testplace_id = "";
    private String testuser_id = "";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initTestCase() {
        // wait for the sign in process to complete
        QUpApp.getInstance().getDBHandler().waitSignInComplete(10);
        QUpApp.getInstance().getDBHandler().waitPlacesComplete(10);
        QUpApp.getInstance().getDBHandler().waitUsersComplete(10);
    }

    @Before
    public void registerIntentServiceIdlingResource() throws UiObjectNotFoundException {
        Log.d("TestPD", "Before");

        // prepare UiAutomator
        this.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // set up idling resources
        placesIdlingResource = QUpApp.getInstance().getDBHandler().getPlacesIdlingResource();
        Espresso.registerIdlingResources(placesIdlingResource);
        usersIdlingResource = QUpApp.getInstance().getDBHandler().getUsersIdlingResource();
        Espresso.registerIdlingResources(usersIdlingResource);

        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        // get existing places from db
        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            if (!db_handle.isPlacesListEmpty())
                break;
        }
        assertEquals(false, db_handle.isPlacesListEmpty());

        // try to remove all old testplaces
        List<String> oldtestplaces_to_remove = new ArrayList<>();
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

        // create new testplace

        // add a place at center of camera: 47.0707, 15.4395
        db_handle.addPlace("testplace", "12.06", "34.4639", "0", "0", "10", "www.testplace.at", "0-2", "testplaceStreet1");

        // look for testplace in list
        startTime = System.currentTimeMillis(); //fetch starting time
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

        // try to remove all old testusers
        List<String> oldtestusers_to_remove = new ArrayList<>();
        db_handle.usersLock();
        for (User u : db_handle.getUsersList()) {
            if (u.userName.equals("testCaseUser")) {
                oldtestusers_to_remove.add(u.userId);
            }
        }
        db_handle.usersUnlock();
        for (String id : oldtestusers_to_remove) {
            db_handle.removeUser(id);
        }

        // create testuser
        db_handle.addUser("testCaseUser", "lalala");

        startTime = System.currentTimeMillis(); //fetch starting time
        boolean finished = false;
        while(!finished && (System.currentTimeMillis()-startTime) < 5000) {
            db_handle.usersLock();
            for (User u : db_handle.getUsersList()) {
                if (u.userName.equals("testCaseUser")) {
                    finished = true;
                    testuser_id = u.userId;
                    break;
                }
            }
            db_handle.usersUnlock();
        }
        assertEquals(true, finished);

        // log in
        Intents.init();
        onView(withId(R.id.inputName)).perform(click());
        onView(withId(R.id.inputName)).perform(typeText("testCaseUser"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.editTextPasswort)).perform(click());
        onView(withId(R.id.editTextPasswort)).perform(typeText("lalala"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
        Intents.release();

        // zoom map to test position
        /*
        MapsActivity mAct = (MapsActivity) QUpApp.getInstance().getCurrentActivity();
        mAct.zoomToPosition(12.06, 34.4639);
        */

        // go to place
        Intents.init();
        device.waitForIdle(10000);
        SystemClock.sleep(500);
        UiObject marker = device.findObject(new UiSelector().descriptionContains("testplace"));
        marker.click();

        SystemClock.sleep(500);
        Rect bounds = marker.getBounds();
        int clickX = bounds.centerX();
        int clickY = bounds.centerY();
        clickY -= 114;
        device.click(clickX, clickY);

        SystemClock.sleep(500);
        intended(hasComponent(PlaceDetails.class.getName()));
        Intents.release();
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Log.d("TestPD", "After");
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        db_handle.removePlace(testplace_id);
        db_handle.removeUser(testuser_id);
        Espresso.unregisterIdlingResources(placesIdlingResource);
        Espresso.unregisterIdlingResources(usersIdlingResource);
    }

    @Test
    public void a_testClickInfo() throws Exception {
        Log.d("TestPD", "testClickInfo");
        Intents.init();
        onView(withId(R.id.buttoninfo)).perform(click());
        //String resName = QUpApp.getInstance().getCurrentActivity().getResources().getResourceName(R.id.buttoninfo);
        //device.findObject(new UiSelector().resourceId(resName)).click();
        intended(hasComponent(InfoActivity.class.getName()));
        Intents.release();
    }
/*
    @Test
    public void b_testVotePositiveWithoutQueue() throws Exception {
        Log.d("TestPD", "testVotePositiveWithoutQueue");
        onView(withId(R.id.txt_like)).check(matches(withText("0")));
        onView(withId(R.id.buttonlike)).perform(click());
        onView(withId(R.id.txt_like)).check(matches(withText("0")));
    }

    @Test
    public void b_testVoteNegativeWithoutQueue() throws Exception {
        Log.d("TestPD", "testVoteNegativeWithoutQueue");
        //String resName;
        // get count before click
        //resName = QUpApp.getInstance().getCurrentActivity().getResources().getResourceName(R.id.txt_dislike);
        //String oldnum = onView( withId(R.id.txt_dislike)).toString();

        onView(withId(R.id.txt_dislike)).check(matches(withText("0")));

        //device.findObject(new UiSelector().resourceId(resName)).getText();
        //assertNotEquals(oldnum, "");

        // click rating
        //resName = QUpApp.getInstance().getCurrentActivity().getResources().getResourceName(R.id.buttondislike);
        //device.findObject(new UiSelector().resourceId(resName)).click();

        onView(withId(R.id.buttondislike)).perform(click());

        //SystemClock.sleep(1000);
        // check count again
        //resName = QUpApp.getInstance().getCurrentActivity().getResources().getResourceName(R.id.txt_dislike);
        //String newnum = device.findObject(new UiSelector().resourceId(resName)).getText();
        //String newnum = onView( withId(R.id.txt_dislike)).toString();

        onView(withId(R.id.txt_dislike)).check(matches(withText("0")));
        //assertNotEquals(newnum, "");

        // number should stay the same without queuing up
        //assertEquals(Integer.parseInt(oldnum), Integer.parseInt(newnum));
    }
*/
    @Test
    public void g_testQUpButton() throws Exception {
        Log.d("TestPD", "testQUpButton");
        onView(withId(R.id.btn_qup)).check(matches(withText("Q UP!")));
        onView(withId(R.id.btn_qup)).perform(click());
        onView(withId(R.id.btn_qup)).check(matches(withText("EXIT Q")));
    }

    @Test
    public void y_testVotePositive() throws Exception {
        Log.d("TestPD", "testVotePositive");

        // queue
        onView(withId(R.id.btn_qup)).check(matches(withText("Q UP!")));
        onView(withId(R.id.btn_qup)).perform(click());
        onView(withId(R.id.btn_qup)).check(matches(withText("EXIT Q")));
        SystemClock.sleep(100);

        // check count before click
        onView(withId(R.id.txt_like)).check(matches(withText("0")));

        // click rating
        onView(withId(R.id.buttonlike)).perform(click());
        SystemClock.sleep(1500);

        // check count again
        onView(withId(R.id.txt_like)).check(matches(withText("1")));

        // exit queue
        onView(withId(R.id.btn_qup)).perform(click());
        onView(withId(R.id.btn_qup)).check(matches(withText("Q UP!")));
    }

    @Test
    public void z_testVoteNegative() throws Exception {
        Log.d("TestPD", "testVoteNegative");

        // queue
        onView(withId(R.id.btn_qup)).check(matches(withText("Q UP!")));
        onView(withId(R.id.btn_qup)).perform(click());
        onView(withId(R.id.btn_qup)).check(matches(withText("EXIT Q")));
        SystemClock.sleep(100);

        // check count before click
        onView(withId(R.id.txt_dislike)).check(matches(withText("0")));

        // click rating
        onView(withId(R.id.buttondislike)).perform(click());
        SystemClock.sleep(1500);

        // check count again
        onView(withId(R.id.txt_dislike)).check(matches(withText("1")));

        // exit queue
        onView(withId(R.id.btn_qup)).perform(click());
        onView(withId(R.id.btn_qup)).check(matches(withText("Q UP!")));
    }
}
