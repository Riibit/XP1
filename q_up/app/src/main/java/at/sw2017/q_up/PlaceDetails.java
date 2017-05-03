package at.sw2017.q_up;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PlaceDetails extends Activity {





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        Bundle bundle = getIntent().getExtras();
       String title = bundle.getString("title");
        TextView  getlatitude = (TextView)findViewById(R.id.txtview_getlatitude);
        TextView getlongtitude = (TextView)findViewById(R.id.txtview_getlongtitude);
        TextView txtViewtitle = (TextView)findViewById(R.id.txtview_title);

        //getlongtitude.setText(longtitude);
        //getlatitude.setText(latitude);
        txtViewtitle.setText(title);




    }


}
