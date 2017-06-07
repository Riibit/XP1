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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class ChatTest {
    private UiDevice device;
    private SimpleIdlingResource placesIdlingResource;
    private SimpleIdlingResource usersIdlingResource;

    private TestHelperUtils test_utils;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initTestCase() {
        // forget about logged in users
        SaveSharedPreference.setUserName(QUpApp.getContext(), "");

        // wait for the sign in process to complete
        QUpApp.getInstance().getDBHandler().waitSignInComplete(10);
        QUpApp.getInstance().getDBHandler().waitPlacesComplete(10);
        QUpApp.getInstance().getDBHandler().waitUsersComplete(10);
    }

    @Before
    public void beforeEachTest() throws UiObjectNotFoundException {
        Log.d("TestPD", "Before");

        this.test_utils = new TestHelperUtils();

        // prepare UiAutomator
        this.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // set up idling resources
        placesIdlingResource = QUpApp.getInstance().getDBHandler().getPlacesIdlingResource();
        Espresso.registerIdlingResources(placesIdlingResource);
        usersIdlingResource = QUpApp.getInstance().getDBHandler().getUsersIdlingResource();
        Espresso.registerIdlingResources(usersIdlingResource);

        // initialize DB
        assertNotNull(this.test_utils.db_handle);
        assertEquals(true, this.test_utils.beforeEachTest());

        // log in
        Intents.init();
        onView(withId(R.id.inputName)).perform(click());
        onView(withId(R.id.inputName)).perform(typeText(TestHelperUtils.TESTUSER_NAME));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.editTextPasswort)).perform(click());
        onView(withId(R.id.editTextPasswort)).perform(typeText(TestHelperUtils.TESTUSER_PW));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
        Intents.release();

        // go to place
        Intents.init();
        device.waitForIdle(10000);
        SystemClock.sleep(500);
        UiObject marker = device.findObject(new UiSelector().descriptionContains(TestHelperUtils.TESTPLACE_NAME));
        marker.click();

        // click on the place's label
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
    public void afterEachTest() {
        Log.d("TestPD", "After");

        assertEquals(true, test_utils.afterEachTest());

        Espresso.unregisterIdlingResources(placesIdlingResource);
        Espresso.unregisterIdlingResources(usersIdlingResource);
    }



    @Test
    public void testChatInput() throws Exception {
        Log.d("TestPD", "testChat");

        // queue
        onView(withId(R.id.btn_chat)).check(matches(withText("Chat!")));
        onView(withId(R.id.btn_chat)).perform(click());

        //UiObject marker = device.findObject(new UiSelector().text("CHAT!"));
        //String lala = marker.getText();
        //boolean boo = marker.exists();
        //marker.click();

        //SystemClock.sleep(10);

       // onView( withId(R.id.input)).perform(click());
        onView( withId(R.id.input)).perform(typeText("msg"));
        Espresso.closeSoftKeyboard();


        onView(withId(R.id.fab)).perform(click());

    }


}
