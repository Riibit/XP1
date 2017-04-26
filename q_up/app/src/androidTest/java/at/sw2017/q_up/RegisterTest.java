package at.sw2017.q_up;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        Intents.init();

//        int result = QUpApp.getInstance().getDBHandler().readUsersFromDB();
//        QUpApp.getInstance().getDBHandler().waitUsersComplete(20);
    }


    @Test
    public void TestRegistration() throws Exception {

        onView( withId(R.id.inputUsername)).perform(click());
        onView( withId(R.id.inputUsername)).perform(typeText("Testuser"));

        onView( withId(R.id.inputPassword)).perform(click());
        onView( withId(R.id.inputPassword)).perform(typeText("password"));

        onView( withId(R.id.confirmPassword)).perform(click());
        onView( withId(R.id.confirmPassword)).perform(typeText("password"));

        Espresso.closeSoftKeyboard();
      //  onView( withId(R.id.btn_save)).perform(click());
    }




}
