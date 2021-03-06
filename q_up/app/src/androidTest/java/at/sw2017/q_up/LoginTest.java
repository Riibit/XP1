package at.sw2017.q_up;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class LoginTest {

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
    public void registerIntentServiceIdlingResource() {
        Log.d("TestLogin", "Before");

        this.test_utils = new TestHelperUtils();

        placesIdlingResource = QUpApp.getInstance().getDBHandler().getPlacesIdlingResource();
        Espresso.registerIdlingResources(placesIdlingResource);
        usersIdlingResource = QUpApp.getInstance().getDBHandler().getUsersIdlingResource();
        Espresso.registerIdlingResources(usersIdlingResource);

        // initialize DB
        assertNotNull(this.test_utils.db_handle);
        assertEquals(true, this.test_utils.beforeEachTest());
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Log.d("TestLogin", "After");

        assertEquals(true, test_utils.afterEachTest());

        Espresso.unregisterIdlingResources(placesIdlingResource);
        Espresso.unregisterIdlingResources(usersIdlingResource);
    }

    @Test
    public void Login() throws Exception {

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
    }

    @Test
    public void LoginFail() throws Exception {

        onView( withId(R.id.inputName)).perform(click());
        onView( withId(R.id.inputName)).perform(typeText(TestHelperUtils.TESTUSER_NAME));
        Espresso.closeSoftKeyboard();

        onView( withId(R.id.editTextPasswort)).perform(click());
        onView( withId(R.id.editTextPasswort)).perform(typeText("thisisthewrongpassword"));
        Espresso.closeSoftKeyboard();

        //Espresso.closeSoftKeyboard();
        onView( withId(R.id.buttonLogin)).perform(click());

        // this checks if we are still on the register activity
        // a check with intended threw an exception - only works if you actually switch activities
        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()));
    }

}
