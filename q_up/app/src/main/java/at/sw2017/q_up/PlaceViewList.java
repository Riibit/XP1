package at.sw2017.q_up;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by fanconng on 05/04/2017.
 */

public class PlaceViewList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_places);

        List<String> places_to_show = new ArrayList<String>();
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();

        // fill list with places from DB
        for (Place p : db_handle.getPlacesList()) {
            places_to_show.add(p.placeName);
        }

        ListAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places_to_show);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(myAdapter);
    }
}