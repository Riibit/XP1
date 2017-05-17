package at.sw2017.q_up;

import android.app.Activity;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
    String testplace_id = "";

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

        // create testplace
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        assertNotNull(db_handle);

        // get existing places from db
        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime) < 5000) {
            if (!db_handle.isPlacesListEmpty())
                break;
        }
        assertEquals(false, db_handle.isPlacesListEmpty());

        // add a place at center of camera: 47.0707, 15.4395
        db_handle.addPlace("testplace", "47.0707", "15.4395", "0", "0", "10");

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

        // log in
        Intents.init();
        onView( withId(R.id.inputName)).perform(click());
        onView( withId(R.id.inputName)).perform(typeText("hans"));

        onView( withId(R.id.editTextPasswort)).perform(click());
        onView( withId(R.id.editTextPasswort)).perform(typeText("password"));

        onView( withId(R.id.buttonLogin)).perform(click());
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

        intended(hasComponent(PlaceDetails.class.getName()));
        Intents.release();
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Log.d("TestPD", "After");
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        db_handle.removePlace(testplace_id);
        Espresso.unregisterIdlingResources(placesIdlingResource);
        Espresso.unregisterIdlingResources(usersIdlingResource);
    }

    @Test
    public void testClickMarker() {

    }
}
