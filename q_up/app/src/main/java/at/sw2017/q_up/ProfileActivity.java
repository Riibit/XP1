package at.sw2017.q_up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView usernameText = (TextView) findViewById(R.id.textView);
        usernameText.setText(MainActivity.getUsername());

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
                goToMain();
            }
        });

        buttonMap.setRotation(90);
    }

    public void goToMaps() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
