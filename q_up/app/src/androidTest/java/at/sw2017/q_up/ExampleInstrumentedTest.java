package at.sw2017.q_up;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.PendingIntent.getActivity;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
 
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("at.sw2017.q_up", appContext.getPackageName());
    }

    @Test
    public void testButtonLogin() throws Exception {
        onView(withText("login")).perform(click());

    }

    @Test
    public void testLoginToRegisterSwitch() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(RegisterActivity.class.getName(), null, false);

        onView(withText("Register")).perform(click());

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(nextActivity);
        nextActivity .finish();

    }

    @Test
    public void testRegisterNavigationButton() throws Exception {
        onView(withText("Register")).perform(click());

    }

    @Test
    public void testRegisterLogin() throws Exception {
        // perform register on main activity


        // fill textfield

        // fill up dummylist

       // User test_user_1();
        //test_user_1.


        // check result is correct

    }

    @Rule
    public ActivityTestRule<RegisterActivity> rActivityRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);


    @Test
    public void testLoginNavigationButton() throws Exception {
        onView(withText("Login")).perform(click());

    }

    @Test
    public void testButtonRegister() throws Exception {
        onView(withText("Register")).perform(click());

    }

}


