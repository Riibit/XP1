package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class RegisterActivity extends Activity implements View.OnClickListener {

    Button loginNavigationButton;
    Button registerButton;
    EditText inputPassword;
    EditText confirmPassword;
    EditText inputUsername;
    EditText inputEmail;
    List<User> userList;



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
        inputUsername = (EditText) findViewById(R.id.inputPassword);
        inputEmail = (EditText) findViewById(R.id.confirmPassword);
    }

    public void switchLoginRegister() {
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;

        // request db
        // get data db
        // list<user> is here



        switch (clickedButton.getId()) {
            case R.id.registerButton:
                // check texfield is already in db
                // if not add data
                // check confirm password is the same as password
                break;
            case R.id.loginNavigationButton:
                switchLoginRegister();
                break;
            default:
                break;
        }
    }
}
