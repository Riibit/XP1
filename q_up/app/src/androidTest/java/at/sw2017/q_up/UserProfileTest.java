package at.sw2017.q_up;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
 
@RunWith(AndroidJUnit4.class)
public class UserProfileTest {

    @Rule
    public ActivityTestRule<ProfileActivity> mActivityRule = new ActivityTestRule<ProfileActivity>(ProfileActivity.class);

    @Test
    public void testButtonLogout() throws Exception {
        onView(withText("Log Out")).perform(click());

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


