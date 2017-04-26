package at.sw2017.q_up;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by fanconng on 05/04/2017.
 */

public class PlaceViewList extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_list_places);

      String[] stores = new String[]{"McDonalds", "Davinci", "Hofer", "Billa"};
      ListAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stores);
      ListView list = (ListView) findViewById(R.id.list);
      list.setAdapter(myAdapter);

   }
}