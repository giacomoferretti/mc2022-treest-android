package com.giacomoferretti.mobilecomputing2022.treest.android.ui.mapdetails;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.giacomoferretti.mobilecomputing2022.treest.android.R;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Station;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.ActivityMapViewBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

public class MapDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PERMISSION_REQUEST = 0;
    public static final String EXTRA_DIRECTION_ID = "DIRECTION_ID";

    private GoogleMap mMap;
    private ActivityMapViewBinding mBinding;
    private MapDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMapViewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(MapDetailsViewModel.class);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(mBinding.map.getId());
        mapFragment.getMapAsync(this);

        mBinding.goBack.setOnClickListener(view -> {
            finish();
        });

        mBinding.findPosition.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                showUserLocation(mMap);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Permission denied for the first time, show an educational ui and ask again
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST);
            } else {
                // Permission denied, show an educational ui
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST);
            }
        });

        mViewModel.setDirectionId(getIntent().getStringExtra(EXTRA_DIRECTION_ID));
    }

    @SuppressLint("MissingPermission")
    private void showUserLocation(@NonNull GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted
            showUserLocation(mMap);
        }

        mViewModel.getStations().observe(this, stations -> {
            // Add markers
            for (Station station : stations) {
                LatLng position = new LatLng(station.getLatitude(), station.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(station.getStationName()));
            }

            // Build polyline
            PolylineOptions polylineOptions = new PolylineOptions();
            for (Station station : stations) {
                LatLng position = new LatLng(station.getLatitude(), station.getLongitude());
                polylineOptions.add(position);
            }
            Polyline polyline = mMap.addPolyline(polylineOptions);
            polyline.setColor(getResources().getColor(R.color.md_theme_light_primary));

            // Calculate zoom
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Station station : stations) {
                builder.include(new LatLng(station.getLatitude(), station.getLongitude()));
            }
            LatLngBounds bounds = builder.build();

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 400));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showUserLocation(mMap);
            } else {
                // Denied
            }
        }
    }
}
