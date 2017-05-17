package at.sw2017.q_up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class PlaceDetails extends Activity implements OnClickListener {

    static String id, title;
    static String userid;
    static String placeid;
    DatabaseHandler db_handle;

    private Button ButtonLike;
    private Button ButtonDislike;

    ToggleButton ButtonQ;




    public void LikeDislike()
    {
        ButtonLike = (Button) findViewById(R.id.buttonlike);
        ButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_handle.votePlacePositive(id);
                ButtonLike.setEnabled(false);
                ButtonDislike.setEnabled(false);
                //ButtonLike.setVisibility(View.INVISIBLE);

            }
        });

        ButtonDislike = (Button) findViewById(R.id.buttondislike);
        ButtonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_handle.votePlaceNegative(id);
                ButtonLike.setEnabled(false);
                ButtonDislike.setEnabled(false);

            }
        });
    }



    public void InfoButton()
    {
           Button ButtonInfo = (Button) findViewById(R.id.buttoninfo);
        ButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceDetails.this, InfoActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });

    }
    public void EvaluationOnTime()
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

                                Bundle bundle = getIntent().getExtras();
                                id  = bundle.getString("id");
                                db_handle = QUpApp.getInstance().getDBHandler();
                                Place place = new Place();
                                db_handle.placesLock();
                                for (Place p : db_handle.getPlacesList()) {
                                    if (p.placeId.equals(id)) {
                                        place = p;
                                        break;
                                    }
                                }
                                db_handle.placesUnlock();

                                TextView txtViewtitle = (TextView) findViewById(R.id.txtview_title);
                                TextView txtViewlike = (TextView) findViewById(R.id.txt_like);
                                TextView txtViewdislike = (TextView) findViewById(R.id.txt_dislike);
                                txtViewtitle.setText(place.placeName);
                                txtViewlike.setText(place.ratingPos);
                                txtViewdislike.setText(place.ratingNeg);
                                LikeDislike();
                                NumberQUP(db_handle.getQueuedUserCountFromPlace(place.placeId));
                                title = place.placeName;



                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }



    public void NumberQUP(int number)
    {
        String text;
        switch (number)
        {

            case 0:
                text= "Be the first in the Q!";
                break;
            case 1:
                text = "Be the second in the Q!";
                break;
            case 2:
                text = "Be the third in the Q!";
                break;
            default:
                text = "Be the " + Integer.toString(number+1) + "th in the Q!";
                break;
        }
        TextView txtViewNumberQUP = (TextView) findViewById(R.id.txtView_numberqup);
        txtViewNumberQUP.setText(text);

    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButtonQ = (ToggleButton) findViewById(R.id.btn_qup);
        ButtonQ.setOnClickListener(this);
        ButtonQ.setChecked(getDefaults("togglekey",this));
        setDefaults("togglekey",ButtonQ.isChecked(),this);
        EvaluationOnTime();
        InfoButton();
    }

    public static void setDefaults(String key,Boolean value,Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public static Boolean getDefaults(String key,Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key,true);
    }
    @Override
    public void onStart(){
        super.onStart();
        ButtonQ.setChecked(getDefaults("togglekey",this));

    }
    @Override
    public void onStop(){
        super.onStop();
        setDefaults("togglekey",ButtonQ.isChecked(),this);

    }


    @Override
    public void onClick(View v) {

        ToggleButton clicked = (ToggleButton)v;
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        String Username = (MainActivity.currentUser.userName);
        Bundle bundle = getIntent().getExtras();
        placeid  = bundle.getString("id");
        String text = (String) clicked.getText();


            if(clicked.isChecked() == true) {
                db_handle.usersLock();
                for (User u : db_handle.getUsersList()) {
                    if (u.userName.equals(Username)) {
                        userid = u.userId;
                    }
                }
                db_handle.usersUnlock();
                db_handle.checkUserIntoPlace(userid, placeid);
                Toast.makeText(getApplicationContext(),
                        "User checked in..", Toast.LENGTH_SHORT).show();
            }

         else
            {
                db_handle.checkOutOfPlace(userid);
            Toast.makeText(getApplicationContext(),
                    "User checked out..", Toast.LENGTH_SHORT).show();
            }



    }
}
