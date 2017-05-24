package at.sw2017.q_up;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private Bundle bundle;
    private String title;
    private String id;
    private TextView txtViewTitle;
    private TextView txtViewLongtitude;
    private TextView txtViewlatitude;
    private DatabaseHandler db_handle;
    private Place place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bundle = getIntent().getExtras();
        title  =  "Information of " + bundle.getString("title");
        id = bundle.getString("id");
        txtViewTitle = (TextView) findViewById(R.id.txtView_title_info);
        txtViewLongtitude = (TextView) findViewById(R.id.txtView_longtitude);
        txtViewlatitude = (TextView) findViewById(R.id.txtViewlatitude);

        txtViewTitle.setText(title);

        db_handle = QUpApp.getInstance().getDBHandler();
        place = db_handle.getPlaceFromId(id);
        if (place != null)
        {
            txtViewLongtitude.setText(place.longitude);
            txtViewlatitude.setText(place.latitude);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        Toast.makeText(getApplicationContext(),
                "onRestart", Toast.LENGTH_SHORT).show();

        bundle = getIntent().getExtras();
        title  =  "Information of " + bundle.getString("title");
        id = bundle.getString("id");
        txtViewTitle.setText(title);
        place = db_handle.getPlaceFromId(id);
        if (place != null)
        {
            txtViewLongtitude.setText(place.longitude);
            txtViewlatitude.setText(place.latitude);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        onBackPressed();
        return true;
    }

}


