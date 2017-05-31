package at.sw2017.q_up;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class InfoActivity extends AppCompatActivity {

    private Bundle bundle;
    private String title;
    private String id;
    private TextView txtViewTitle;
    private TextView txtViewLongtitude;
    private TextView txtViewlatitude;
    private TextView txtviewOpening;
    private TextView txtViewAdress;
    private TextView txtViewLink;
    private DatabaseHandler db_handle;
    private Place place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bundle = getIntent().getExtras();
        title  = bundle.getString("title");
        id = bundle.getString("id");
        txtViewTitle = (TextView) findViewById(R.id.txtView_title_info);
        txtViewLongtitude = (TextView) findViewById(R.id.txtView_longtitude);
        txtViewlatitude = (TextView) findViewById(R.id.txtViewlatitude);
        txtviewOpening = (TextView) findViewById(R.id.txtview_opening);
        txtViewAdress = (TextView) findViewById(R.id.txtview_address);
        txtViewLink = (TextView) findViewById(R.id.txt_homepage);


        txtViewTitle.setText(title);

        db_handle = QUpApp.getInstance().getDBHandler();
        place = db_handle.getPlaceFromId(id);
        if (place != null)
        {
            txtViewLongtitude.setText(place.longitude);
            txtViewlatitude.setText(place.latitude);
            txtviewOpening.setText(place.opening_hours);
            txtViewAdress.setText(place.address);
            txtViewLink.setText(place.link);
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
        title  =  bundle.getString("title");
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


