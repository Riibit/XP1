package at.sw2017.q_up;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import at.sw2017.q_up.PlaceDetails.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    //Default locations which have to be changed into original coordinates
    private static final LatLng MCDONALDS = new LatLng(47.055496, 15.448409);
    private static final LatLng DAVINCI = new LatLng(47.054160, 15.444241);
    private static final LatLng HOFER = new LatLng(47.055717, 15.441392);
    int people_in_queue = 12;
    int people_in_queue1 = 7;
    int people_in_queue2 = 4;

    private Marker mMcdonalds;
    private Marker mDavinci;
    private Marker mHofer;

    public void mapsGoBack() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    //Function go to list
    public void mapsGoList() {
        Intent intent1 = new Intent(MapsActivity.this, PlaceViewList.class);
        startActivity(intent1);
    }

    public void mapsGoDetails(String title) {
        Intent intent2 = new Intent(MapsActivity.this, PlaceDetails.class);
        intent2.putExtra("title", title);
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
            mMap.addMarker(new MarkerOptions().position(ll).title(p.placeName)).setTag(p);
        }

        // Add a marker in Graz and move the camera
        googleMap.setOnInfoWindowClickListener(this);

        mMcdonalds = mMap.addMarker(new MarkerOptions().position(MCDONALDS).title("Marker in Mcdonalds"));
        mMcdonalds.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MCDONALDS));
        if (people_in_queue2 <= 5) {
            mMcdonalds.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (people_in_queue2 <= 10) {
            mMcdonalds.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        } else if (people_in_queue2 > 10) {
            mMcdonalds.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }


        mDavinci = mMap.addMarker(new MarkerOptions().position(DAVINCI).title("Marker in Davinci"));
        mDavinci.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(DAVINCI));
        if (people_in_queue <= 5) {
            mDavinci.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (people_in_queue <= 10) {
            mDavinci.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        } else if (people_in_queue > 10) {
            mDavinci.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        mHofer = mMap.addMarker(new MarkerOptions().position(HOFER).title("Marker in Hofer"));
        mHofer.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(HOFER));
        if (people_in_queue1 <= 5) {
            mHofer.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (people_in_queue1 <= 10) {
            mHofer.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        } else if (people_in_queue1 > 10) {
            mHofer.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Add a marker in Graz and move the camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.0707, 15.4395), 14));

        // Set a listener for marker click.
        //     mMap.setOnMarkerClickListener(this);


    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        if (marker.equals(mMcdonalds)) {
            mapsGoDetails(marker.getTitle());
        } else if (marker.equals(mDavinci)) {
            mapsGoDetails(marker.getTitle());
        } else if (marker.equals(mHofer)) {
            mapsGoDetails(marker.getTitle());
        }

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
}

