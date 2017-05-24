package at.sw2017.q_up;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class PlaceDetails extends Activity implements OnClickListener {

    static String id, title;
    static String user_id;
    static String place_id;

    private DatabaseHandler db_handle;
    private String outplace;
    private boolean decision ;
    private Button ButtonLike;
    private Button ButtonDislike;
    TextView peopleInQueue;
    boolean QdUP;
    Calendar cal;
    int start;
    int end;
    int time;
    TextView time_1;




    ToggleButton ButtonQ;




    public void LikeDislike()
    {

        ButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_handle.votePlacePositive(id);
                ButtonLike.setEnabled(false);
                ButtonDislike.setEnabled(false);
                //ButtonLike.setVisibility(View.INVISIBLE);

            }
        });


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
                                Place place = null;
                                db_handle.placesLock();
                                for (Place p : db_handle.getPlacesList()) {
                                    if (p.placeId.equals(id)) {
                                        place = p;
                                        break;
                                    }
                                }
                                db_handle.placesUnlock();

                                if (place != null)
                                {
                                    TextView txtViewtitle = (TextView) findViewById(R.id.txtview_title);
                                    TextView txtViewlike = (TextView) findViewById(R.id.txt_like);
                                    TextView txtViewdislike = (TextView) findViewById(R.id.txt_dislike);
                                    txtViewtitle.setText(place.placeName);
                                    txtViewlike.setText(place.ratingPos);
                                    txtViewdislike.setText(place.ratingNeg);
                                    LikeDislike();
                                    getNumberOfUsers();
                                    if(QdUP == true) {
                                        TextView txtViewNumberQUP = (TextView) findViewById(R.id.txtView_numberqup);
                                        txtViewNumberQUP.setText("You are queued up");
                                    }
                                    else
                                        NumberQUP(db_handle.getQueuedUserCountFromPlace(place.placeId));
                                    title = place.placeName;
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
        QUpApp.getInstance().setCurrentActivity(this);

        ButtonQ = (ToggleButton) findViewById(R.id.btn_qup);
        peopleInQueue = (TextView)findViewById(R.id.UserNr);
        time_1 = (TextView) findViewById(R.id.time8);
        ButtonQ.setOnClickListener(this);
        time_1.setText("");
        decision = false;
       // ButtonQ.setChecked(getDefaults("togglekey",this));
        //setDefaults("togglekey",ButtonQ.isChecked(),this);
        ButtonLike = (Button) findViewById(R.id.buttonlike);
        ButtonDislike = (Button) findViewById(R.id.buttondislike);
        ButtonLike.setEnabled(false);
        ButtonDislike.setEnabled(false);
        start = 0;
        end = 0;
        time = 0;

        EvaluationOnTime();
        InfoButton();
        getNumberOfUsers();

    }

    public  void getNumberOfUsers()
    {
        Bundle bundle = getIntent().getExtras();
        place_id  = bundle.getString("id");
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        peopleInQueue.setText(Integer.toString(db_handle.getQueuedUserCountFromPlace(place_id)));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
/*
    public static void setDefaults(String key,Boolean value,Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key,value);
        editor.apply();
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
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        String Username = (MainActivity.currentUser.userName);
        db_handle.usersLock();
        for (Place p : db_handle.getPlacesList()) {
            p.placeId
                Toast.makeText(getApplicationContext(),
                        p.idCheckInPlace, Toast.LENGTH_SHORT).show();
        }
        db_handle.usersUnlock();
    }
    @Override
    public void onStop(){
        super.onStop();
        setDefaults("togglekey",ButtonQ.isChecked(),this);
    }
*/
    @Override
    public void onClick(View v) {
        final String CHECKED_IN_MSG = "User checked in..";
        final String CHECKED_OUT_MSG = "User checked out..";
        ToggleButton clicked = (ToggleButton)v;
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        String Username = (MainActivity.currentUser.userName);
        Bundle bundle = getIntent().getExtras();
        place_id  = bundle.getString("id");
        //String text = (String) clicked.getText();


        if(clicked.isChecked()) {
            db_handle.usersLock();
            for (User u : db_handle.getUsersList()) {
                if (u.userName.equals(Username)) {
                    user_id = u.userId;
                }
            }
            db_handle.usersUnlock();
            db_handle.checkUserIntoPlace(user_id, place_id);
            QdUP = true;
            decision = true;
            cal = Calendar.getInstance();
            start = cal.get(Calendar.SECOND);
            time_1.setText("");

        }
        else {
            db_handle.checkOutOfPlace(user_id);
            QdUP = false;
            decision = false;
            ButtonLike.setEnabled(true);
            ButtonDislike.setEnabled(true);
                cal = Calendar.getInstance();
                end = cal.get(Calendar.SECOND);

                if(end < start)
                {
                    end += 60;
                    time = end - start;
                    if(time < 0)
                    time = time * (-1);

                    time_1.setText(Integer.toString(time));

                }
                else
                {
                    time = start - end;
                    if(time < 0)
                        time = time * (-1);

                    time_1.setText(Integer.toString(time));

                }

        }
    }
    @Override
    public void onBackPressed() {

        if (decision) {
            Toast.makeText(getApplicationContext(),
                    "You have to exit the queue first !", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed(); // Process Back key  default behavior.
        }
    }
}
