package at.sw2017.q_up;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


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
    }


    @Test
    public void LoginFail() throws Exception {


        onView( withId(R.id.inputName)).perform(click());
        onView( withId(R.id.inputName)).perform(typeText("admin"));

        Espresso.closeSoftKeyboard();
        onView( withId(R.id.buttonLogin)).perform(click());
    }


}
