package at.sw2017.agmapstest;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Default locations which have to be changed into original coordinates
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);

    private Marker mPerth;
    private Marker mSydney;
    private Marker mBrisbane;

    public void mapsGoBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button buttonMapsBack = (Button) findViewById(R.id.buttonMapsBack);

        buttonMapsBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                mapsGoBack();
            }

        });


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

        // Add a marker in Graz and move the camera

        mBrisbane=mMap.addMarker(new MarkerOptions().position(BRISBANE).title("Marker in BRISBANE"));
        mBrisbane.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(BRISBANE));

        mPerth=mMap.addMarker(new MarkerOptions().position(PERTH).title("Marker in Perth"));
        mPerth.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PERTH));

        mSydney=mMap.addMarker(new MarkerOptions().position(SYDNEY).title("Marker in Sydney"));
        mSydney.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));


    }
}
