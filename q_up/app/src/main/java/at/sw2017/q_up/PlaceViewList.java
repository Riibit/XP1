package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by fanconng on 05/04/2017.
 */

public class PlaceViewList extends Activity{


    public void mapsGoDetails(String id) {
        Intent intent2 = new Intent(PlaceViewList.this, PlaceDetails.class);
        intent2.putExtra("id", id);
        startActivity(intent2);
    }

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


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str_id = id +"";
                mapsGoDetails(str_id);

            }
        });


    }


}