package at.sw2017.q_up;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class RegisterTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);


    @BeforeClass
    public static void initTestCase() {
        // wait for the sign in process to complete
        QUpApp.getInstance().getDBHandler().waitSignInComplete(20);
        QUpApp.getInstance().getDBHandler().waitPlacesComplete(10);
        QUpApp.getInstance().getDBHandler().waitUsersComplete(10);


//        int result = QUpApp.getInstance().getDBHandler().readUsersFromDB();
//        QUpApp.getInstance().getDBHandler().waitUsersComplete(20);
    }


    @Test
    public void TestRegistration() throws Exception {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        List<User> users = db_handle.getUsersList();
        int result = db_handle.readUsersFromDB();
        onView( withId(R.id.inputUsername)).perform(click());
        onView( withId(R.id.inputUsername)).perform(typeText("Testhannes"));

        onView( withId(R.id.inputPassword)).perform(click());
        onView( withId(R.id.inputPassword)).perform(typeText("password"));

        onView( withId(R.id.confirmPassword)).perform(click());
        onView( withId(R.id.confirmPassword)).perform(typeText("password"));

        Espresso.closeSoftKeyboard();
        onView( withId(R.id.registerButton)).perform(click());


        String testuser_id = "";
        for (User u : users) {
            if (u.userName.equals("Testhannes")) {
                if (u.password.equals("password")) {
                    testuser_id = u.userId;
                    break;
                }
            }
        }

        assertNotEquals("", testuser_id);
        // remove user again
        result = db_handle.removeUser(testuser_id);
        assertEquals(0, result);
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
