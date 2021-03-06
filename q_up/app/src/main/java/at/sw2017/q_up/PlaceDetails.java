package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Calendar;

public class PlaceDetails extends Activity implements OnClickListener {
    static String id, title;
    static String user_id;
    static String place_id;
    static Place current_place;
    private Bundle bundle;

    private int positionInQ;
    private int Minutes,Seconds;
    private DatabaseHandler db_handle;
    private String peopleinQ,Qtime;
    private boolean decision ;
    private Button ButtonLike;
    private Button ButtonDislike;
    private RatingBar ratingbar;
    TextView peopleInQueue;
    boolean QdUP;
    TextView time_1;
    private Intent intent;
    ToggleButton ButtonQ;

    public static Place getCurrentPlace() {
        return current_place;
    }

    private void LikeDislike()
    {
        ButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_handle.votePlacePositive(id);
                ButtonLike.setEnabled(false);
                ButtonDislike.setEnabled(false);
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

    private void Chat()
    {
        Button ButtonChat = (Button) findViewById(R.id.btn_chat);
        ButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaceDetails.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }


    private void InfoButton()
    {
        Button ButtonInfo = (Button) findViewById(R.id.buttoninfo);
        ButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PlaceDetails.this, InfoActivity.class);
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
                                bundle = getIntent().getExtras();
                                db_handle = QUpApp.getInstance().getDBHandler();
                                Place place = db_handle.getPlaceFromId(id);
                                if (place != null)
                                {
                                    TextView txtViewtitle = (TextView) findViewById(R.id.txtview_title);
                                    TextView txtViewlike = (TextView) findViewById(R.id.txt_like);
                                    TextView txtViewdislike = (TextView) findViewById(R.id.txt_dislike);
                                    txtViewtitle.setText(place.placeName);
                                    txtViewlike.setText(place.ratingPos);
                                    txtViewdislike.setText(place.ratingNeg);
                                    LikeDislike();
                                    Chat();
                                    getNumberOfUsers();
                                    getAverageWaitingTime();
                                    if(QdUP == true) {
                                        ButtonLike.setVisibility(View.VISIBLE);
                                        ButtonDislike.setVisibility(View.VISIBLE);
                                        txtViewlike.setVisibility(View.VISIBLE);
                                        txtViewdislike.setVisibility(View.VISIBLE);
                                        ratingbar.setVisibility(View.GONE);
                                        TextView txtViewNumberQUP = (TextView) findViewById(R.id.txtView_numberqup);
                                        txtViewNumberQUP.setText("You are queued up");
                                    }
                                    else {
                                        positionInQ = db_handle.getQueuedUserCountFromPlace(place_id);
                                        int like = Integer.valueOf(txtViewlike.getText().toString());
                                        int dislike = Integer.valueOf(txtViewdislike.getText().toString());
                                        float stern = (((float)like) / ((float)like+(float)dislike))*100;
                                        ratingbar.setVisibility(View.VISIBLE);
                                        if((int)stern >= 83)
                                            ratingbar.setRating(5);
                                        else if((int)stern >= 67)
                                            ratingbar.setRating(4);
                                        else if((int)stern >= 50)
                                            ratingbar.setRating(3);
                                        else if((int)stern >= 33)
                                            ratingbar.setRating(2);
                                        else if((int)stern >= 17)
                                            ratingbar.setRating(1);
                                        else
                                            ratingbar.setRating(0);
                                        NumberQUP(db_handle.getQueuedUserCountFromPlace(id));
                                        ButtonLike.setVisibility(View.GONE);
                                        ButtonDislike.setVisibility(View.GONE);
                                        txtViewlike.setVisibility(View.GONE);
                                        txtViewdislike.setVisibility(View.GONE);
                                    }
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
        ratingbar = (RatingBar)findViewById(R.id.ratingBar);
        ratingbar.setFocusable(false);
        peopleInQueue = (TextView)findViewById(R.id.UserNr);
        time_1 = (TextView) findViewById(R.id.time8);
        ButtonQ.setOnClickListener(this);
        time_1.setText("");
        decision = false;
        ButtonLike = (Button) findViewById(R.id.buttonlike);
        ButtonDislike = (Button) findViewById(R.id.buttondislike);
        TextView txtViewlike = (TextView) findViewById(R.id.txt_like);
        TextView txtViewdislike = (TextView) findViewById(R.id.txt_dislike);
        ButtonLike.setEnabled(false);
        ButtonDislike.setEnabled(false);
        ButtonLike.setVisibility(View.GONE);
        ButtonDislike.setVisibility(View.GONE);
        txtViewlike.setVisibility(View.GONE);
        txtViewdislike.setVisibility(View.GONE);
        EvaluationOnTime();
        InfoButton();
        getNumberOfUsers();
    }

    public void getAverageWaitingTime()
    {
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        // int timee = db_handle.getQueuedUserCountFromPlace(place_id)* db_handle.getPlaceAvgProcessingSecsFromId(place_id);
        if(db_handle.getQueuedUserCountFromPlace(place_id) == 0)
        {
            Qtime = "Queue Time:\n00:00";
            time_1.setText(Qtime);
        }

        else
        {
            int timee = positionInQ* db_handle.getPlaceAvgProcessingSecsFromId(place_id);
            Minutes = timee /60;
            Seconds = timee % 60;
            Qtime = "Queue Time:\n" + String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds);
            time_1.setText(Qtime);
        }
    }
    public  void getNumberOfUsers()
    {
        bundle = getIntent().getExtras();
        place_id  = bundle.getString("id");
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        peopleinQ = "People in queue: \n" + Integer.toString(db_handle.getQueuedUserCountFromPlace(place_id));
        peopleInQueue.setText(peopleinQ);
        int timee = db_handle.getQueuedUserCountFromPlace(place_id)* db_handle.getPlaceAvgProcessingSecsFromId(place_id);
        int Minutes = timee /60;
        int Seconds = timee % 60;

        Qtime = "Queue Time: \n" + Integer.toString(Minutes) + ":" + Integer.toString(Seconds);
        time_1.setText(Qtime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bundle = getIntent().getExtras();
        id  = bundle.getString("id");
        db_handle = QUpApp.getInstance().getDBHandler();
        current_place = db_handle.getPlaceFromId(id);
        NumberQUP(db_handle.getQueuedUserCountFromPlace(id));
        title = current_place.placeName;
    }

    @Override
    public void onClick(View v) {
        ToggleButton clicked = (ToggleButton) v;
        if (!MainActivity.isConnectingToInternet(PlaceDetails.this)) {
            clicked.setChecked(false);
            Toast.makeText(getApplicationContext(), "No Internet connection detected!", Toast.LENGTH_LONG).show();
        } else {
            DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
            String Username = (MainActivity.currentUser.userName);
            bundle = getIntent().getExtras();
            place_id = bundle.getString("id");
            //String text = (String) clicked.getText();


            if (clicked.isChecked()) {
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
                ButtonLike.setEnabled(true);
                ButtonDislike.setEnabled(true);
                time_1.setText("");
                ButtonLike.setEnabled(true);
                ButtonDislike.setEnabled(true);

            } else {
                db_handle.checkOutOfPlace(user_id);
                QdUP = false;
                decision = false;
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