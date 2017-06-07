package at.sw2017.q_up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    DatabaseHandler db_handler;
    EditText newPassword;
    EditText newPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        db_handler = QUpApp.getInstance().getDBHandler();

        newPassword = (EditText) findViewById(R.id.PWchange_newPassword);
        newPasswordConfirm = (EditText) findViewById(R.id.PWchange_confirmPassword);

        Button buttonChangePW = (Button) findViewById(R.id.PWchange_confirmPassButton);
        buttonChangePW.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                // check if passwords match
                if(newPasswordConfirm.getText().toString().equals("") || newPassword.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),
                            "Enter your Password!", Toast.LENGTH_SHORT).show();
                }
                else if (newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())) {
                    db_handler.modifyUserAttribute(MainActivity.getUser().userId, "password", newPassword.getText().toString());
                    SaveSharedPreference.setUserName(QUpApp.getContext(), "");
                    goToMain();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Passwords don't match / are too weak!", Toast.LENGTH_SHORT).show();
                    newPassword.setText("");
                    newPasswordConfirm.setText("");
                }
            }
        });

        Button buttonCancel = (Button) findViewById(R.id.PWchange_cancelPassButton);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                goToProfile();
            }
        });
    }

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
