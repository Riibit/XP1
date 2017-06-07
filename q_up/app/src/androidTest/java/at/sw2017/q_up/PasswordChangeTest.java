package at.sw2017.q_up;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import junit.framework.Assert;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

/**
 * Created by PS on 07.06.17.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class PasswordChangeTest {

    private SimpleIdlingResource placesIdlingResource;
    private SimpleIdlingResource usersIdlingResource;

    private UiDevice device;

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
    public void registerIntentServiceIdlingResource() throws UiObjectNotFoundException {
        Log.d("TestCP", "Before");

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
        Assert.assertEquals(true, this.test_utils.beforeEachTest());

        // log in
        Intents.init();
        onView( withId(R.id.inputName)).perform(click());
        onView( withId(R.id.inputName)).perform(typeText(TestHelperUtils.TESTUSER_NAME));
        Espresso.closeSoftKeyboard();

        onView( withId(R.id.editTextPasswort)).perform(click());
        onView( withId(R.id.editTextPasswort)).perform(typeText(TestHelperUtils.TESTUSER_PW));
        Espresso.closeSoftKeyboard();

        onView( withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
        Intents.release();

        // go to my profile
        Intents.init();
        device.findObject(new UiSelector().text("My Profile")).click();
        intended(hasComponent(ProfileActivity.class.getName()));
        Intents.release();

        // go to password change activity
        Intents.init();
        onView( withId(R.id.buttonChangePassword)).perform(click());
        //device.findObject(new UiSelector().text("Change password")).click();
        intended(hasComponent(ChangePasswordActivity.class.getName()));
        Intents.release();
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Log.d("TestCP", "After");

        Assert.assertEquals(true, test_utils.afterEachTest());

        Espresso.unregisterIdlingResources(placesIdlingResource);
        Espresso.unregisterIdlingResources(usersIdlingResource);
    }

    @Test
    public void testButtonConfirmOK() throws Exception {

        onView( withId(R.id.PWchange_newPassword)).perform(click());
        onView( withId(R.id.PWchange_newPassword)).perform(typeText(TestHelperUtils.TESTUSER_PW_NEW));
        Espresso.closeSoftKeyboard();

        onView( withId(R.id.PWchange_confirmPassword)).perform(click());
        onView( withId(R.id.PWchange_confirmPassword)).perform(typeText(TestHelperUtils.TESTUSER_PW_NEW));
        Espresso.closeSoftKeyboard();

        Intents.init();
        device.findObject(new UiSelector().text("CONFIRM")).click();
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testCancelButton() throws Exception {

        Espresso.closeSoftKeyboard();
        Intents.init();
        onView( withId(R.id.PWchange_cancelPassButton)).perform(click());
        intended(hasComponent(ProfileActivity.class.getName()));
        Intents.release();
    }
}