package at.sw2017.q_up;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
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

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
 
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class UserProfileTest {

    private SimpleIdlingResource placesIdlingResource;
    private SimpleIdlingResource usersIdlingResource;

    private UiDevice device;

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
        Log.d("TestUP", "Before");

        // prepare UiAutomator
        this.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        placesIdlingResource = QUpApp.getInstance().getDBHandler().getPlacesIdlingResource();
        Espresso.registerIdlingResources(placesIdlingResource);
        usersIdlingResource = QUpApp.getInstance().getDBHandler().getUsersIdlingResource();
        Espresso.registerIdlingResources(usersIdlingResource);

        Intents.init();
        onView( withId(R.id.inputName)).perform(click());
        onView( withId(R.id.inputName)).perform(typeText("hans"));

        onView( withId(R.id.editTextPasswort)).perform(click());
        onView( withId(R.id.editTextPasswort)).perform(typeText("password"));

        onView( withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));

        Intents.release();

        Intents.init();
//        onView( withText("My Profile")).perform(click());
//        onView( withId(R.id.buttonMapsBack)).perform(click());
        device.findObject(new UiSelector().text("My Profile")).click();
        intended(hasComponent(ProfileActivity.class.getName()));
        Intents.release();
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Log.d("TestUP", "After");
        Espresso.unregisterIdlingResources(placesIdlingResource);
        Espresso.unregisterIdlingResources(usersIdlingResource);
    }

    @Test
    public void testButtonLogout() throws Exception {
        Intents.init();
        onView(withText("Log Out")).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testLogoutSwitch() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);

        onView(withText("Log Out")).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity .finish();

    }

    @Test
    public void testToggleButton() throws Exception {
        onView(withText("Show My Places")).perform(click());

    }

    @Test
    public void testMapButton() throws Exception {
        onView(withText("Map")).perform(click());

    }

    @Test
    public void testMapSwitch() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(MapsActivity.class.getName(), null, false);

        onView(withText("Map")).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity .finish();

    }

}


