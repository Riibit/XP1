package at.sw2017.q_up;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle bundle = getIntent().getExtras();
        String title  =  "Information of " + bundle.getString("title");
        String id = bundle.getString("id");
        TextView txtViewTitle = (TextView) findViewById(R.id.txtView_title_info);
        TextView txtViewLongtitude = (TextView) findViewById(R.id.txtView_longtitude);
        TextView txtViewlatitude = (TextView) findViewById(R.id.txtViewlatitude);

        txtViewTitle.setText(title);

        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        db_handle.placesLock();
        for (Place p : db_handle.getPlacesList()) {
            if (p.placeId.equals(id)) {
                txtViewLongtitude.setText(p.longitude);
                txtViewlatitude.setText(p.latitude);
                break;
            }
        }
        db_handle.placesUnlock();
    }
}


