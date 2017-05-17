package at.sw2017.q_up;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import at.sw2017.q_up.PlaceDetails.*;

import com.google.android.gms.maps.CameraUpdate;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener  {

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

    public void mapsGoDetails(String id) {
        Intent intent2 = new Intent(MapsActivity.this, PlaceDetails.class);
        intent2.putExtra("id", id);
        startActivity(intent2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        QUpApp.getInstance().setCurrentActivity(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //creation of button go to list- ADDED !
        Button goToList = (Button) findViewById(R.id.buttonList);
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

        buttonMapsProfile.setRotation(270);
        goToList.setRotation(90);

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        QUpApp.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = QUpApp.getInstance().getCurrentActivity();
        if (this.equals(currActivity))
            QUpApp.getInstance().setCurrentActivity(null);
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
        db_handle.placesLock();
        for (Place p : db_handle.getPlacesList()) {
            LatLng ll = new LatLng(Double.parseDouble(p.latitude), Double.parseDouble(p.longitude));

            // add marker for place
            Marker m = mMap.addMarker(new MarkerOptions().position(ll).title(p.placeName));
            m.setTag(p);

            // count the number of people in the queue
            int people_in_queue = db_handle.getQueuedUserCountFromPlace(p.placeId);

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
        db_handle.placesUnlock();

        googleMap.setOnInfoWindowClickListener(this);

        // move the camera to Graz
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.0707, 15.4395), 14));
    }
/*
    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }
*/

    @Override
    public void onInfoWindowClick(Marker marker) {
        Place p = (Place)marker.getTag();
        if (p != null)
            mapsGoDetails(p.placeId);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getPosition() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();


        }


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;

    }
/*
    public void zoomToPosition(double lat, double lng) {
        if (mMap != null) {
            LatLng coord = new LatLng(lat, lng);
            CameraUpdate camupd = CameraUpdateFactory.newLatLngZoom(coord, 14);
            mMap.animateCamera(camupd, 4000, null);
        }
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14));
    }
*/
}

