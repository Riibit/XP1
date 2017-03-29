package at.sw2017.agmapstest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSwitchScreens = (Button) findViewById(R.id.btnSwitchScreens);

        btnSwitchScreens.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                switchActivities();
            }
            });
    }

    public void switchActivities() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


}


