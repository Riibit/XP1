package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.View.OnKeyListener;
import android.view.View;
import android.view.KeyEvent;


import java.util.List;

import static junit.framework.Assert.assertEquals;


public class MainActivity extends Activity implements View.OnClickListener {

    ViewPager viewPager;
    Button buttonLogin;
    Button registerNavigationButton;
    Button loginNavigationButton;
    EditText editTextUsername;
    EditText editTextPassword;




    OnKeyListener myKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View arg0, int actionID, KeyEvent event) {
            // TODO: do what you got to do
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (actionID == KeyEvent.KEYCODE_ENTER)) {
                Button click = (Button)findViewById(R.id.buttonLogin);
                click.performClick();


            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        registerNavigationButton = (Button) findViewById(R.id.registerNavigationButton);
        registerNavigationButton.setOnClickListener(this);
        loginNavigationButton = (Button) findViewById(R.id.loginNavigationButton);
        editTextUsername = (EditText) findViewById(R.id.inputName);
        editTextUsername.requestFocus();
        editTextPassword = (EditText) findViewById(R.id.editTextPasswort);

        editTextPassword.setOnKeyListener(myKeyListener);
        editTextUsername.requestFocus();


    }

    @Override
    public void onStart() {
        super.onStart();
        //QUpApp.getInstance().getDBHandler().addAuthStListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        //QUpApp.getInstance().getDBHandler().removeAuthStListener();
    }


    public void switchActivities() {


        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }

    public void switchLoginRegister() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;

        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        List<User> users = db_handle.getUsersList();

        // check for DB timeout
        int timeout = 4 * 1000;
        long startTime = System.currentTimeMillis(); //fetch starting time
        boolean data_ready = false;

        while (!data_ready && (System.currentTimeMillis() - startTime) < timeout) {
            users = db_handle.getUsersList();
            if (!users.isEmpty())
                data_ready = true;
        }
        if (data_ready != true) {
            Toast.makeText(getApplicationContext(),
                    "Server timeout!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (clickedButton.getId()) {
            case R.id.buttonLogin:

                // look for username in our list
                for (User u : users) {
                    if (u.userName.equals(editTextUsername.getText().toString())) {
                        // found entered username in database
                        // now check if password is correct
                        if (u.password.equals(editTextPassword.getText().toString())) {
                            // given password matches entry in database

                            Toast.makeText(getApplicationContext(),
                                    "Login successful!", Toast.LENGTH_SHORT).show();

                            switchActivities();
                            return;
                        }
                        break;
                    }
                }
                Toast.makeText(getApplicationContext(),
                        "Wrong username or password!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.registerNavigationButton:
                switchLoginRegister();
            default:
                break;
        }
    }
}
