package at.sw2017.q_up;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PlaceDetails extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        Place place = new Place();
        for (Place p : db_handle.getPlacesList()) {
            if (p.placeId.equals(id)) {
                place = p;
                break;
            }
        }

        TextView getlatitude = (TextView) findViewById(R.id.txtview_getlatitude);
        TextView getlongtitude = (TextView) findViewById(R.id.txtview_getlongtitude);
        TextView txtViewtitle = (TextView) findViewById(R.id.txtview_title);

        getlongtitude.setText(place.longitude);
        getlatitude.setText(place.latitude);
        txtViewtitle.setText(place.placeName);
    }


}
