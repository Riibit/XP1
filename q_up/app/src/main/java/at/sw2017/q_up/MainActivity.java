package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    Button buttonLogin;
    Button registerNavigationButton;
    Button loginNavigationButton;
    EditText editTextUsername;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button btnSwitchScreens = (Button) findViewById(R.id.btnSwitchScreens);

       /* btnSwitchScreens.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                switchActivities();
            }
        });*/

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
        registerNavigationButton = (Button) findViewById(R.id.registerNavigationButton);
        registerNavigationButton.setOnClickListener(this);
        loginNavigationButton = (Button) findViewById(R.id.loginNavigationButton);
        editTextUsername = (EditText) findViewById(R.id.inputName);
        editTextUsername.setHint("Username");
        editTextPassword = (EditText) findViewById(R.id.editTextPasswort);
        editTextPassword.setHint("Password");
    }

    @Override
    public void onStart() {
        super.onStart();
        QUpApp.getInstance().getDBHandler().addAuthStListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        QUpApp.getInstance().getDBHandler().removeAuthStListener();
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

        // db request
        // db wait
        // list<user> is fetched from db

        switch (clickedButton.getId()) {
            case R.id.buttonLogin:
                if (editTextUsername.getText().toString().equals("admin")
                        && editTextPassword.getText().toString().equals("1234")) {

                    switchActivities();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "ohoh", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.registerNavigationButton:
                switchLoginRegister();
            default:
                break;
        }
    }
}
