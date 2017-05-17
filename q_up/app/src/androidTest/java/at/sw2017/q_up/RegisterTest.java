package at.sw2017.q_up;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
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

import java.util.List;

import static android.support.test.espresso.Espresso.onData;
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

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class RegisterTest {

    private SimpleIdlingResource placesIdlingResource;
    private SimpleIdlingResource usersIdlingResource;

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);

    @BeforeClass
    public static void initTestCase() {
        // wait for the sign in process to complete
        QUpApp.getInstance().getDBHandler().waitSignInComplete(10);
        QUpApp.getInstance().getDBHandler().waitPlacesComplete(10);
        QUpApp.getInstance().getDBHandler().waitUsersComplete(10);
    }

    @Before
    public void registerIntentServiceIdlingResource() {
        Log.d("TestRegister", "Before");
        //Activity activity = activityTestRule.getActivity();

        placesIdlingResource = QUpApp.getInstance().getDBHandler().getPlacesIdlingResource();
        Espresso.registerIdlingResources(placesIdlingResource);
        usersIdlingResource = QUpApp.getInstance().getDBHandler().getUsersIdlingResource();
        Espresso.registerIdlingResources(usersIdlingResource);
    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Log.d("TestRegister", "After");
        Espresso.unregisterIdlingResources(placesIdlingResource);
        Espresso.unregisterIdlingResources(usersIdlingResource);
    }


    @Test
    public void TestRegistration() throws Exception {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();

        onView( withId(R.id.inputUsername)).perform(click());
        onView( withId(R.id.inputUsername)).perform(typeText("Testhannes"));

        onView( withId(R.id.inputPassword)).perform(click());
        onView( withId(R.id.inputPassword)).perform(typeText("password"));

        onView( withId(R.id.confirmPassword)).perform(click());
        onView( withId(R.id.confirmPassword)).perform(typeText("password"));

        Espresso.closeSoftKeyboard();
        onView( withId(R.id.registerButton)).perform(click());

        long startTime = System.currentTimeMillis(); //fetch starting time
        String testuser_id = "";
        while(testuser_id == "" && (System.currentTimeMillis()-startTime) < 5000) {
            // look for testuser in list
            db_handle.usersLock();
            for (User u : db_handle.getUsersList()) {
                if (u.userName.equals("Testhannes")) {
                    if (u.password.equals("password")) {
                        testuser_id = u.userId;
                        break;
                    }
                }
            }
            db_handle.usersUnlock();
        }
        assertNotEquals("", testuser_id);

        // remove user again
        assertEquals(0, db_handle.removeUser(testuser_id));
        db_handle.waitUsersComplete(20);
    }

    @Test
    public void SwitchActivity() throws Exception {

        Intents.init();
        onView( withId(R.id.inputUsername)).perform(click());
        onView( withId(R.id.inputUsername)).perform(typeText("Testhannes"));

        onView( withId(R.id.inputPassword)).perform(click());
        onView( withId(R.id.inputPassword)).perform(typeText("password"));

        onView( withId(R.id.confirmPassword)).perform(click());
        onView( withId(R.id.confirmPassword)).perform(typeText("password"));


        onView( withId(R.id.loginNavigationButton)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void WrongConfirm() throws Exception {

        onView( withId(R.id.inputUsername)).perform(click());
        onView( withId(R.id.inputUsername)).perform(typeText("Failhannes"));

        onView( withId(R.id.inputPassword)).perform(click());
        onView( withId(R.id.inputPassword)).perform(typeText("password"));

        onView( withId(R.id.confirmPassword)).perform(click());
        onView( withId(R.id.confirmPassword)).perform(typeText("passwrd"));

        onView( withId(R.id.registerButton)).perform(click());
    }


    @Test
    public void newFranz() throws Exception {

        onView( withId(R.id.inputUsername)).perform(click());
        onView( withId(R.id.inputUsername)).perform(typeText("franz"));

        onView( withId(R.id.inputPassword)).perform(click());
        onView( withId(R.id.inputPassword)).perform(typeText("password"));

        onView( withId(R.id.confirmPassword)).perform(click());
        onView( withId(R.id.confirmPassword)).perform(typeText("password"));

        onView( withId(R.id.registerButton)).perform(click());
    }
}
