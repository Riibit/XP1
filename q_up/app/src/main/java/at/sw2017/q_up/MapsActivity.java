package at.sw2017.q_up;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private List<Marker> marker_list = new ArrayList<Marker>();

    public void mapsGoBack() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    //Function go to list
    public void mapsGoList() {
        Intent intent1 = new Intent(MapsActivity.this, PlaceViewList.class);
        startActivity(intent1);
    }

    public void mapsGoDetails() {
        Intent intent2 = new Intent(MapsActivity.this, PlaceDetails.class);
        startActivity(intent2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //creation of button go to list- ADDED !
        Button goToList =(Button) findViewById(R.id.buttonList);
        goToList.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                mapsGoList();
            }
        });


        //This was already here !
        Button buttonMapsProfile = (Button) findViewById(R.id.buttonMapsBack);
        buttonMapsProfile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                mapsGoBack();
            }
        });

        buttonMapsProfile.setRotation(90);
        goToList.setRotation(270);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // load places from DB
        DatabaseHandler db_handle = QUpApp.getInstance().getDBHandler();
        for (Place p : db_handle.getPlacesList()) {
            LatLng ll = new LatLng(Double.parseDouble(p.latitude), Double.parseDouble(p.longitude));

            // add marker for place
            Marker m = mMap.addMarker(new MarkerOptions().position(ll).title(p.placeName));
            m.setTag(p);

            // count the number of people in the queue
            int people_in_queue = 0;
            for (User u : db_handle.getUsersList()) {
                if (u.idCheckInPlace.equals(p.placeId))
                    people_in_queue += 1;
            }

            // set color of marker
            if (people_in_queue <= 5)
                m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            else if (people_in_queue <= 10)
                m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            else if (people_in_queue > 10)
                m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            // save marker in list
            marker_list.add(m);
        }

        googleMap.setOnInfoWindowClickListener(this);

        // move the camera to Graz
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.0707, 15.4395), 14));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        mapsGoDetails();
    }
}