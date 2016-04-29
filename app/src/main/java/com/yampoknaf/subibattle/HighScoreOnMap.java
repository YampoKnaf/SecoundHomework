package com.yampoknaf.subibattle;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class HighScoreOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        HighScoreDbHelper dbHelper = new HighScoreDbHelper(getApplicationContext());
        ArrayList<DataBaseRowData> allHighScore = dbHelper.getUsers(null);

        if(allHighScore == null)
            return;

        int x = 0;
        int y = 0;

        for(DataBaseRowData rowsOfData : allHighScore){
            mMap.addMarker(new MarkerOptions().position(new LatLng(rowsOfData.getmLat() , rowsOfData.getmLag())).title(rowsOfData.getmName() + " score: " + rowsOfData.getmScore() + " difficult: " + rowsOfData.getmDifficult() ));
            x += rowsOfData.getmLat();
            y += rowsOfData.getmLag();
        }

        x /= allHighScore.size();
        y /= allHighScore.size();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(x , y)));
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
}
