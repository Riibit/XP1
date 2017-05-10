package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDetails extends Activity{

    static String id;
    DatabaseHandler db_handle;

    public void LikeDislike()
    {
        Button ButtonLike = (Button) findViewById(R.id.buttonlike);
        ButtonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Like" + id, Toast.LENGTH_SHORT).show();
                db_handle.votePlacePositive(id);

            }
        });

        Button ButtonDislike = (Button) findViewById(R.id.buttondislike);
        ButtonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Dislike" + id, Toast.LENGTH_SHORT).show();
                db_handle.votePlaceNegative(id);

            }
        });
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);


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



        //TextView getlatitude = (TextView) findViewById(R.id.txtview_getlatitude);
        //TextView getlongtitude = (TextView) findViewById(R.id.txtview_getlongtitude);
        TextView txtViewtitle = (TextView) findViewById(R.id.txtview_title);
        TextView txtViewlike = (TextView) findViewById(R.id.txt_like);
        TextView txtViewdislike = (TextView) findViewById(R.id.txt_dislike);

        //getlongtitude.setText(place.longitude);
        //getlatitude.setText(place.latitude);
        txtViewtitle.setText(place.placeName);
        txtViewlike.setText(place.ratingPos);
        txtViewdislike.setText(place.ratingNeg);

        //
        LikeDislike();
    }




}
