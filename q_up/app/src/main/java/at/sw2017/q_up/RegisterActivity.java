package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import android.view.View.OnKeyListener;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class RegisterActivity extends Activity implements View.OnClickListener {

    Button loginNavigationButton;
    Button registerButton;
    EditText inputPassword;
    EditText confirmPassword;
    EditText inputUsername;


    OnKeyListener myKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View arg0, int actionID, KeyEvent event) {
            // TODO: do what you got to do
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (actionID == KeyEvent.KEYCODE_ENTER)) {
                Button click = (Button)findViewById(R.id.registerButton);
                click.performClick();


            }
            return false;
        }
    };


    public void switchLoginRegister()
    {
        Button ButtonLogin = (Button) findViewById(R.id.loginNavigationButton);
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginNavigationButton = (Button) findViewById(R.id.loginNavigationButton);
        loginNavigationButton.setOnClickListener(this);
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        confirmPassword.setOnKeyListener(myKeyListener);
        switchLoginRegister();
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

        while(!data_ready && (System.currentTimeMillis()-startTime) < timeout) {
            users = db_handle.getUsersList();
            if (!users.isEmpty())
                data_ready = true;
        }
        if (data_ready != true) {
            Toast.makeText(getApplicationContext(),
                    "Server timeout!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean user_already_in_list = false;

        switch (clickedButton.getId()) {
            case R.id.registerButton:

                if (!inputPassword.getText().toString().equals(confirmPassword.getText().toString()) ||
                        inputPassword.getText().toString() == "")
                {
                    Toast.makeText(getApplicationContext(),
                            "Passwords don't match / are too weak!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(inputUsername.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter a username for registration", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(inputPassword.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter a password for registration", Toast.LENGTH_SHORT).show();
                    break;
                }

                // check if user already exists
                for (User u : users) {
                    if (u.userName.equals(inputUsername.getText().toString())) {
                        // username is already in list
                        Toast.makeText(getApplicationContext(),
                                "This user already exists!", Toast.LENGTH_SHORT).show();
                        user_already_in_list = true;
                        break;
                    }
                }

                // check if user is not in list of existing users and create the user
                if (!user_already_in_list) {
                    db_handle.addUser(inputUsername.getText().toString(), inputPassword.getText().toString());
                    Toast.makeText(getApplicationContext(),
                            "User created..", Toast.LENGTH_SHORT).show();
                    clearText();
                }

                // check texfield is already in db
                // if not add data
                // check confirm password is the same as password
                break;
            default:
                break;
        }
    }

    public void clearText() {
        inputUsername.setText("");
        inputPassword.setText("");
        confirmPassword.setText("");
    }
}
