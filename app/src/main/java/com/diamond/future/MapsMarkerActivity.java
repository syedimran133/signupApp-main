package com.diamond.future;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.diamond.future.utility.Constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

public class MapsMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {
    View v;
    String lat, log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        lat = getIntent().getStringExtra(Constant.LAT);
        log = getIntent().getStringExtra(Constant.LOG);
    /*    SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void Back(View view) {
        onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            LatLng patna = new LatLng(Double.parseDouble(lat), Double.parseDouble(log));
            googleMap.addMarker(new MarkerOptions()
                    .position(patna)
                    .title("Marker"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(patna, 14));
        } catch (Exception e) {
        }
    }
}
