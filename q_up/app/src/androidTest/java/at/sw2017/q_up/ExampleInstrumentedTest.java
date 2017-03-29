package at.sw2017.q_up;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
    public void testLoginButton() throws Exception {
      onView(withText("login")).perform(click());

    }

    @Test
    public void testLoginTextfield() throws Exception {
        onView(withText("login")).perform(click()); // click on login
        onView(withId(R.id.editText2)).check(matches(withText("BLABLA")));
    }

    @Test
    public void testLoginEditfield() throws Exception {
        onView(withText("login")).perform(click());
        onView(withText("*****")).check(matches(isDisplayed()));
    }


    @Test
    public void testRegister() throws Exception {
        onView(withText("login")).perform(click());
        onView(withText("*****")).check(matches(isDisplayed()));
    }
}


