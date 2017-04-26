package at.sw2017.q_up;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initTestCase() {
        // wait for the sign in process to complete
        QUpApp.getInstance().getDBHandler().waitSignInComplete(20);
        QUpApp.getInstance().getDBHandler().waitPlacesComplete(10);
        QUpApp.getInstance().getDBHandler().waitUsersComplete(10);
        Intents.init();

//        int result = QUpApp.getInstance().getDBHandler().readUsersFromDB();
//        QUpApp.getInstance().getDBHandler().waitUsersComplete(20);
    }

    @Test
    public void Login() throws Exception {

       // DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
       // db_handle.waitPlacesComplete(2);
        onView( withId(R.id.inputName)).perform(click());
        onView( withId(R.id.inputName)).perform(typeText("hans"));


        onView( withId(R.id.editTextPasswort)).perform(click());
        onView( withId(R.id.editTextPasswort)).perform(typeText("password"));

        //Espresso.closeSoftKeyboard();


        onView( withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();






    }

//This testcase should faile
    @Test
    public void LoginFail() throws Exception {


        onView( withId(R.id.inputName)).perform(click());
        onView( withId(R.id.inputName)).perform(typeText("admin"));

        onView( withId(R.id.editTextPasswort)).perform(click());
        onView( withId(R.id.editTextPasswort)).perform(typeText("password"));

        Espresso.closeSoftKeyboard();
        onView( withId(R.id.buttonLogin)).perform(click());

        intended(hasComponent(MapsActivity.class.getName()));
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }


}
