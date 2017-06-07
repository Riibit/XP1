package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class PlaceViewList extends Activity {

   /* public void mapsGoDetails(String id) {
        Intent intent2 = new Intent(PlaceViewList.this, PlaceDetails.class);
        intent2.putExtra("id", id);
        startActivity(intent2);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_places);


        final List<Place> places_to_show = new ArrayList<>();
        final List<String> place_names = new ArrayList<>();
        final List<String> place_ids = new ArrayList<>();
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();

        // fill list with places from DB
        db_handle.placesLock();
        for (Place p : db_handle.getPlacesList()) {
            places_to_show.add(p);
        }
        db_handle.placesUnlock();
        Collections.sort(places_to_show);

        for (Place p : places_to_show) {
            place_names.add(p.placeName + " " + p.address);
            place_ids.add(p.placeId);
        }

        ListAdapter myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, place_names);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(myAdapter);

        list.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p = place_ids.get(position);
                goToPlace(p);
            }
        });
    }

    public void goToPlace(String id) {

        Intent intent = new Intent(PlaceViewList.this, PlaceDetails.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }


}