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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Default locations which have to be changed into original coordinates
    private static final LatLng MCDONALDS = new LatLng(47.055496, 15.448409);
    private static final LatLng DAVINCI = new LatLng(47.054160, 15.444241);
    private static final LatLng HOFER = new LatLng(47.055717, 15.441392);

    private Marker mMcdonalds;
    private Marker mDavinci;
    private Marker mHofer;

    public void mapsGoBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Function go to list
    public void mapsGoList() {
        Intent intent1 = new Intent(MapsActivity.this, PlaceViewList.class);
        startActivity(intent1);
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
        Button buttonMapsBack = (Button) findViewById(R.id.buttonMapsBack);
        buttonMapsBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                mapsGoBack();
            }
        });

        buttonMapsBack.setRotation(90);
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

        // Add a marker in Graz and move the camera

        mMcdonalds=mMap.addMarker(new MarkerOptions().position(MCDONALDS).title("Marker in Mcdonalds"));
        mMcdonalds.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MCDONALDS));

        mDavinci=mMap.addMarker(new MarkerOptions().position(DAVINCI).title("Marker in Davinci"));
        mDavinci.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(DAVINCI));

        mHofer=mMap.addMarker(new MarkerOptions().position(HOFER).title("Marker in Hofer"));
        mHofer.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(HOFER));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.0707, 15.4395),14));


    }
}