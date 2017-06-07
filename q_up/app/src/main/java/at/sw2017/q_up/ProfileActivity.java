package at.sw2017.q_up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    DatabaseHandler db_handler;
    User currentUser = MainActivity.getUser();
    TextView lastCheckinPlaceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        db_handler = QUpApp.getInstance().getDBHandler();

        TextView usernameText = (TextView) findViewById(R.id.Profile_username);
        usernameText.setText(currentUser.userName);

        lastCheckinPlaceText = (TextView) findViewById(R.id.Profile_lastPlace);
        lastCheckinPlaceText.setText(db_handler.getPlaceNameFromId(currentUser.idLastCheckInPlace));

        Button buttonMap =(Button) findViewById(R.id.buttonMap);
        buttonMap.setRotation(270);

        buttonMap.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                goToMaps();
            }
        });

        Button buttonLogout =(Button) findViewById(R.id.buttonLogOut);
        buttonLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                SaveSharedPreference.setUserName(QUpApp.getContext(), "");
                goToMain();
            }
        });

        Button buttonChangePW =(Button) findViewById(R.id.buttonChangePassword);
        buttonChangePW.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                goToChangePW();
            }
        });

        buttonMap.setRotation(90);
    }

    @Override
    protected void onResume() {
        super.onResume();
        QUpApp.getInstance().setCurrentActivity(this);
        lastCheckinPlaceText.setText(db_handler.getPlaceNameFromId(currentUser.idLastCheckInPlace));
    }

    public void goToMaps() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToChangePW() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}


