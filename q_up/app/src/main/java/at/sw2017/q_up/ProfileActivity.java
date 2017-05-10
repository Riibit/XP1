package at.sw2017.q_up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    DatabaseHandler db_handle;
    User currentUser = MainActivity.getUser();


    TextView usernameText;

    TextView currentPlace;


    /*
        public String setPlaceOnProfile()
        {

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(10);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO code inside which should be updated
                                    Bundle bundle = getIntent().getExtras();
                                    id  = bundle.getString("id");
                                    db_handle = QUpApp.getInstance().getDBHandler();
                                    db_handle.placesLock();
                                    String currPlace = currentUser.idCheckInPlace;

                                    for (Place p : db_handle.getPlacesList()) {
                                        if (p.placeId.equals(currPlace)) {
                                            place = p.placeName;
                                            break;
                                        }
                                    }
                                   // currentPlace.setText(place.placeName);
                                    db_handle.placesUnlock();

                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            t.start();
            return place;
        }
    */
    protected void onResume(Bundle savedInstancesState)
    {
        db_handle = QUpApp.getInstance().getDBHandler();
        db_handle.placesLock();
        for (Place p : db_handle.getPlacesList()) {
            if (p.placeId.equals(currentUser.idCheckInPlace)) {
                currentPlace.setText(p.placeName);
                break;
            }
        }
        db_handle.placesUnlock();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db_handle = QUpApp.getInstance().getDBHandler();
        usernameText = (TextView) findViewById(R.id.textView);
        usernameText.setText(currentUser.userName);
        currentPlace = (TextView) findViewById(R.id.textViewQueuePlace);
        currentPlace.setText("aa");

        //setPlaceOnProfile();

       /*
        db_handle.placesUnlock();*/
        //currentPlace.setText(setPlaceOnProfile());

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
