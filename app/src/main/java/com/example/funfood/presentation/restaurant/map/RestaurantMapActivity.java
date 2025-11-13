package com.example.funfood.presentation.restaurant.map;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.funfood.databinding.ActivityRestaurantMapBinding; // Cần đảm bảo binding này khớp với layout
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView; // THAY ĐỔI: Import MapView
import com.google.android.gms.maps.OnMapReadyCallback;
// import com.google.android.gms.maps.SupportMapFragment; // BỎ
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityRestaurantMapBinding binding;
    private GoogleMap mMap;
    private String restaurantName;
    private double latitude;
    private double longitude;

    // THÊM MỚI: Khai báo MapView
    private MapView mapView;

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

        // Setup Toolbar (Binding đã có toolbar nên dùng trực tiếp)
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Vị trí: " + restaurantName);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // THAY ĐỔI: Logic khởi tạo MapView

        // 1. Tìm MapView bằng ID
        mapView = binding.map; // Giả sử ID trong binding là 'map'

        // 2. GỌI MapView.onCreate()
        // Đây là bước CỰC KỲ QUAN TRỌNG
        mapView.onCreate(savedInstanceState);

        // 3. Load bản đồ
        mapView.getMapAsync(this);
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

    // --- THÊM MỚI: QUẢN LÝ VÒNG ĐỜI CHO MAPVIEW ---
    // Bạn BẮT BUỘC phải thêm tất cả các phương thức này
    // và gọi đến phương thức tương ứng của MapView.

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }
}