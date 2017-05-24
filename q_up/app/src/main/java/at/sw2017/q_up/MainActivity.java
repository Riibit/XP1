package at.sw2017.q_up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.VISIBLE;


public class MainActivity extends Activity implements View.OnClickListener {

    Button buttonLogin;
    Button registerNavigationButton;
    Button loginNavigationButton;
    EditText editTextUsername;
    EditText editTextPassword;
    RelativeLayout waitingAnimation;
    TextView waitingText;
    Handler mainHandler;

    static User currentUser;
    private static MainActivity instance;

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

    public void serverReady() {
        //if(SaveSharedPreference.getUserName(QUpApp.getContext()).length() != 0)
        //{
        //    DatabaseHandler db_handler = QUpApp.getInstance().getDBHandler();
        //    currentUser = db_handler.getUserFromName(SaveSharedPreference.getUserName(QUpApp.getContext()));
        //    waitingAnimation = (RelativeLayout)findViewById(R.id.waitingAnimation);
        //    waitingAnimation.setVisibility(RelativeLayout.GONE);
        //    waitingText = (TextView)findViewById(R.id.waitingText);
        //    waitingText.setVisibility(TextView.GONE);
        //    switchActivities();
        //}
        //else
        {
            //setContentView(R.layout.activity_main);
            waitingAnimation = (RelativeLayout)findViewById(R.id.waitingAnimation);
            waitingAnimation.setVisibility(RelativeLayout.GONE);
            waitingText = (TextView)findViewById(R.id.waitingText);
            waitingText.setVisibility(TextView.GONE);
            buttonLogin.setVisibility(Button.VISIBLE);
            registerNavigationButton.setVisibility(Button.VISIBLE);
            loginNavigationButton.setVisibility(Button.VISIBLE);
            editTextUsername.setVisibility(EditText.VISIBLE);
            editTextPassword.setVisibility(EditText.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        registerNavigationButton = (Button) findViewById(R.id.registerNavigationButton);
        registerNavigationButton.setOnClickListener(this);
        loginNavigationButton = (Button) findViewById(R.id.button3);
        //loginNavigationButton.setOnClickListener(this); // not needed
        editTextUsername = (EditText) findViewById(R.id.inputName);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswort);
        editTextPassword.setOnKeyListener(myKeyListener);
        editTextUsername.requestFocus();

        EvaluationOnTime();
    }

    public void EvaluationOnTime()
    {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
                                if (db_handle.getInitDone())
                                {
                                    findViewById(R.id.waitingAnimation).post(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            serverReady();
                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onStart() {
        super.onStart();

        //QUpApp.getInstance().getDBHandler().addAuthStListener();
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
        //QUpApp.getInstance().getDBHandler().removeAuthStListener();
    }

    public static Context getContext(){
        //return instance;
        return instance.getApplicationContext();
    }

    public void switchActivities() {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }

    public void switchLoginRegister() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public static User getUser() {
        return currentUser;
    }

    @Override
    public void onClick(View v) {
        final int DATABASE_TIMEOUT_TIME_IN_MS = 4000;
        final String SERVER_TIMEOUT_ERROR = "Server timeout!";
        final String LOGIN_SUCCESSFUL_MESSAGE = "Login successful!";
        final String WRONG_LOGIN_DATA_ERROR = "Wrong username or password!";

        Button clickedButton = (Button) v;
        editTextUsername.requestFocus();
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        // check for DB timeout
        long startTime = System.currentTimeMillis(); //fetch starting time
        boolean data_ready = false;

        while (!data_ready && (System.currentTimeMillis() - startTime) < DATABASE_TIMEOUT_TIME_IN_MS) {
            if (!db_handle.isUsersListEmpty())
                data_ready = true;
        }
        if (!data_ready) {
            Toast.makeText(getApplicationContext(),
                    SERVER_TIMEOUT_ERROR, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (clickedButton.getId()) {
            case R.id.buttonLogin:
                // look for username in our list
                db_handle.usersLock();
                for (User u : db_handle.getUsersList()) {
                    if (u.userName.equals(editTextUsername.getText().toString())) {
                        // found entered username in database
                        // now check if password is correct
                        if (u.password.equals(editTextPassword.getText().toString())) {
                            // given password matches entry in database
                            db_handle.usersUnlock();
                            Toast.makeText(getApplicationContext(),
                                    LOGIN_SUCCESSFUL_MESSAGE, Toast.LENGTH_SHORT).show();
                            currentUser = u;
                            SaveSharedPreference.setUserName(QUpApp.getContext(), u.userName);
                            switchActivities();
                            return;
                        }
                        break;
                    }
                }
                db_handle.usersUnlock();

                Toast.makeText(getApplicationContext(),
                        WRONG_LOGIN_DATA_ERROR, Toast.LENGTH_SHORT).show();
                editTextUsername.setText("");
                editTextPassword.setText("");
                editTextUsername.requestFocus();
                break;
            case R.id.registerNavigationButton:
                switchLoginRegister();
            default:
                break;
        }
    }
}