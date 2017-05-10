package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDetails extends Activity{

    String id;
    DatabaseHandler db_handle;

    public void LikeDislike()
    {
        Button ButtonLike = (Button) findViewById(R.id.buttonlike);
        ButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_handle.votePlacePositive(id);

            }
        });

        Button ButtonDislike = (Button) findViewById(R.id.buttondislike);
        ButtonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_handle.votePlaceNegative(id);

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
                intent.putExtra("title", id);
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
                                for (Place p : db_handle.getPlacesList()) {
                                    if (p.placeId.equals(id)) {
                                        place = p;
                                        break;
                                    }
                                }
                                TextView txtViewtitle = (TextView) findViewById(R.id.txtview_title);
                                TextView txtViewlike = (TextView) findViewById(R.id.txt_like);
                                TextView txtViewdislike = (TextView) findViewById(R.id.txt_dislike);
                                txtViewtitle.setText(place.placeName);
                                txtViewlike.setText(place.ratingPos);
                                txtViewdislike.setText(place.ratingNeg);
                                LikeDislike();
                                NumberQUP(db_handle.getQueuedUserCountFromPlace(place.placeId));
                                id = place.placeName;
                                InfoButton();

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
        EvaluationOnTime();





    }




}
