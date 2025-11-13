package com.example.funfood.presentation.restaurant.map;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.funfood.R;
import com.example.funfood.databinding.ActivityRestaurantMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityRestaurantMapBinding binding;
    private GoogleMap mMap;
    private String restaurantName;
    private double latitude;
    private double longitude;

    public static final String EXTRA_NAME = "RESTAURANT_NAME";
    public static final String EXTRA_LAT = "RESTAURANT_LAT";
    public static final String EXTRA_LNG = "RESTAURANT_LNG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy dữ liệu từ Intent
        restaurantName = getIntent().getStringExtra(EXTRA_NAME);
        latitude = getIntent().getDoubleExtra(EXTRA_LAT, 0);
        longitude = getIntent().getDoubleExtra(EXTRA_LNG, 0);

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Vị trí: " + restaurantName);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Load bản đồ
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Kiểm tra tọa độ hợp lệ
        if (latitude != 0 || longitude != 0) {
            // Tạo vị trí
            LatLng location = new LatLng(latitude, longitude);

            // Thêm Marker
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(restaurantName));

            // Di chuyển camera và zoom
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f)); // 15f là mức zoom
        }
    }
}