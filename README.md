# 🍔 FunFood Android App v1.0

[![Android](https://img.shields.io/badge/Android-24+-green.svg)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![Material Design](https://img.shields.io/badge/Material%20Design-3-blue.svg)](https://m3.material.io/)

Ứng dụng đặt đồ ăn FunFood cho Android. Được xây dựng với kiến trúc MVVM, Material Design 3, Retrofit, và tích hợp đầy đủ với FunFood Backend API.

---

## 📋 Mục lục

- [Tính năng](#-tính-năng)
- [Công nghệ](#-công-nghệ)
- [Cài đặt](#-cài-đặt)
- [Cấu trúc dự án](#-cấu-trúc-dự-án)
- [Kiến trúc](#-kiến-trúc)
- [Màn hình chính](#-màn-hình-chính)
- [API Integration](#-api-integration)
- [Testing](#-testing)

---

## ✨ Tính năng

### 🔐 Authentication & Authorization
- Đăng ký, đăng nhập với JWT
- Auto-login với token saved
- Quản lý session (30 ngày)
- Change password
- Role-based UI (customer/admin)
- Profile management

### 🏪 Quản lý nhà hàng
- Danh sách nhà hàng với pagination
- **GPS-based nearby search** - Tìm nhà hàng gần nhất
- Filter theo category, rating, status
- Tìm kiếm full-text
- Chi tiết nhà hàng với menu
- Rating & reviews display
- Open/Close status
- Call restaurant

### 🍕 Quản lý sản phẩm
- Danh sách sản phẩm theo nhà hàng
- Featured products (sản phẩm nổi bật)
- Filter theo category, price, discount
- Chi tiết sản phẩm với hình ảnh
- Discount badge hiển thị
- Available/Unavailable status
- Add to cart functionality

### 🛒 Giỏ hàng
- Thêm/Xóa/Cập nhật items
- Group by restaurant
- Real-time total calculation
- Cart sync với server
- Clear cart by restaurant
- Persistent cart (saved on server)

### 📦 Đơn hàng
- Tạo đơn với GPS location
- Order history với filter
- Track order status (6 trạng thái)
- Cancel order (pending/confirmed only)
- Order details với items
- **Distance & delivery fee** tự động
- Payment method selection

### ❤️ Yêu thích
- Add/Remove favorites
- Toggle favorite
- Favorites list
- Quick access favorites

### ⭐ Đánh giá
- Rating 1-5 sao
- Comment/Review
- View restaurant reviews
- Edit own reviews

### 🎟️ Khuyến mãi
- Danh sách promotions
- Active promotions display
- Apply promotion code
- Validate promotion
- Discount calculation

### 📍 Địa chỉ giao hàng
- Quản lý nhiều địa chỉ
- **GPS coordinates** support
- Set default address
- Add/Edit/Delete addresses
- Recipient info management

### 🔔 Thông báo
- Real-time notifications
- Order status updates
- Promotion announcements
- Read/Unread status
- Mark all as read
- Delete notifications

### 🎨 UI/UX Features
- Material Design 3
- Dark mode ready
- Smooth animations
- Pull-to-refresh
- Infinite scroll
- Shimmer loading effects
- Error handling UI
- Network state handling
- Empty states
- Search with debounce

---

## 🛠 Công nghệ

### Core
- **Language**: Java 11
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 15)
- **Build System**: Gradle 8.13

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **Architecture Components**:
  - LiveData
  - ViewModel
  - Navigation Component

### Networking
- **Retrofit**: 2.11.0 - REST API client
- **OkHttp**: 4.12.0 - HTTP client với logging
- **Gson**: 2.11.0 - JSON serialization

### UI
- **Material Design**: 3
- **View Binding**: Enabled
- **Glide**: 4.16.0 - Image loading
- **CircleImageView**: 3.1.0 - Avatar
- **RecyclerView**: 1.3.2
- **SwipeRefreshLayout**: 1.1.0
- **CardView**: 1.0.0

### Storage
- **SharedPreferences**: User session
- **Room Database**: 2.6.1 (for offline cache)

### Location
- **Google Play Services**:
  - Location: 21.3.0
  - Maps: 19.0.0

### Others
- **Navigation Component**: 2.9.5
- **Lifecycle**: 2.8.7
- **Dagger**: (planned for DI)

---

## 🚀 Cài đặt

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 24+
- Git

### Installation Steps

```bash
# 1. Clone repository
git clone <your-repo-url>
cd FunFood-Android

# 2. Open in Android Studio
# File → Open → Select project folder

# 3. Sync Gradle
# Android Studio sẽ tự động sync

# 4. Configure API URL (if needed)
# File: app/build.gradle
buildConfigField "String", "BASE_URL", "\"https://your-api-url.com/api/\""

# 5. Run on device/emulator
# Click Run button or Shift+F10
```

### Configuration

**gradle.properties:**
```properties
API_BASE_URL="https://funfood-backend-67v4.onrender.com/api/"
```

**Google Maps API Key:**
```xml
<!-- app/src/main/AndroidManifest.xml -->
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />
```

---

## 📁 Cấu trúc dự án

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/funfood/
│   │   │   ├── data/                    # Data layer
│   │   │   │   ├── local/              # Local database (Room)
│   │   │   │   │   ├── dao/           # Data Access Objects
│   │   │   │   │   └── entity/        # Database entities
│   │   │   │   ├── remote/             # Remote data source
│   │   │   │   │   ├── api/           # API interfaces
│   │   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   │   ├── AuthInterceptor.java
│   │   │   │   │   └── RetrofitClient.java
│   │   │   │   ├── preferences/        # SharedPreferences
│   │   │   │   └── repository/         # Repository pattern
│   │   │   │
│   │   │   ├── domain/                  # Domain layer
│   │   │   │   ├── model/              # Domain models
│   │   │   │   └── usecase/            # Business logic
│   │   │   │
│   │   │   ├── presentation/            # Presentation layer
│   │   │   │   ├── auth/               # Login, Register
│   │   │   │   ├── main/               # Main activity + fragments
│   │   │   │   │   ├── home/          # Home screen
│   │   │   │   │   ├── orders/        # Orders screen
│   │   │   │   │   ├── favorite/      # Favorites screen
│   │   │   │   │   └── profile/       # Profile screen
│   │   │   │   ├── restaurant/         # Restaurant screens
│   │   │   │   │   ├── detail/        # Restaurant detail
│   │   │   │   │   └── list/          # Restaurant list
│   │   │   │   ├── product/            # Product screens
│   │   │   │   ├── cart/               # Cart screen
│   │   │   │   ├── checkout/           # Checkout flow
│   │   │   │   ├── order/              # Order detail/tracking
│   │   │   │   ├── notification/       # Notifications
│   │   │   │   ├── address/            # Address management
│   │   │   │   ├── splash/             # Splash screen
│   │   │   │   └── base/               # Base classes
│   │   │   │       ├── BaseActivity.java
│   │   │   │       ├── BaseFragment.java
│   │   │   │       ├── BaseAdapter.java
│   │   │   │       └── BaseViewModel.java
│   │   │   │
│   │   │   ├── util/                    # Utilities
│   │   │   │   ├── Constants.java
│   │   │   │   ├── Resource.java       # Data wrapper
│   │   │   │   ├── NetworkUtil.java
│   │   │   │   ├── ImageUtil.java
│   │   │   │   ├── CurrencyUtil.java
│   │   │   │   ├── DateUtil.java
│   │   │   │   ├── DistanceUtil.java   # GPS calculations
│   │   │   │   └── ValidationUtil.java
│   │   │   │
│   │   │   └── FunFoodApplication.java  # Application class
│   │   │
│   │   ├── res/                         # Resources
│   │   │   ├── layout/                 # XML layouts
│   │   │   ├── drawable/               # Images, icons
│   │   │   ├── values/                 # Colors, strings, themes
│   │   │   ├── menu/                   # Menu XML
│   │   │   └── navigation/             # Navigation graphs
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── test/                            # Unit tests
│
├── build.gradle                         # App-level Gradle
└── proguard-rules.pro                  # ProGuard config
```

---

## 🏗 Kiến trúc

### MVVM Pattern

```
┌─────────────────────────────────────────────┐
│              View (Activity/Fragment)        │
│  - UI Logic                                  │
│  - ViewBinding                               │
│  - Observe LiveData                          │
└─────────────────┬───────────────────────────┘
                  │
                  │ observes
                  ▼
┌─────────────────────────────────────────────┐
│              ViewModel                       │
│  - UI State                                  │
│  - Business Logic                            │
│  - LiveData                                  │
└─────────────────┬───────────────────────────┘
                  │
                  │ calls
                  ▼
┌─────────────────────────────────────────────┐
│              Repository                      │
│  - Data abstraction                          │
│  - Single source of truth                    │
└─────────────────┬───────────────────────────┘
                  │
                  ├──────────────┬─────────────┐
                  ▼              ▼             ▼
          ┌──────────────┐  ┌──────────┐  ┌──────────┐
          │ Remote API   │  │  Local   │  │  Prefs   │
          │  (Retrofit)  │  │  (Room)  │  │  (SP)    │
          └──────────────┘  └──────────┘  └──────────┘
```

### Data Flow

**Success Flow:**
```
User Action → View → ViewModel → Repository → API
                ↑                                 ↓
                └─────── LiveData ← Resource ←────┘
```

**Error Handling:**
```
API Error → Repository → Resource.error()
                            ↓
            ViewModel → LiveData → View → Show Error UI
```

### Key Components

#### 1. BaseActivity
```java
public abstract class BaseActivity<VB extends ViewBinding> {
    protected abstract VB getViewBinding();
    protected abstract void setupViews();
    protected abstract void observeData();
    
    // Common methods
    protected void showToast(String message);
    protected void showLoading();
    protected void hideLoading();
    protected boolean isNetworkAvailable();
}
```

#### 2. Resource Wrapper
```java
public class Resource<T> {
    enum Status { SUCCESS, ERROR, LOADING }
    
    private Status status;
    private T data;
    private String message;
}
```

#### 3. Repository Pattern
```java
public class RestaurantRepository {
    private final RestaurantApi api;
    
    public LiveData<Resource<List<Restaurant>>> getRestaurants(int page) {
        MutableLiveData<Resource<...>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        
        api.getRestaurants(page, limit).enqueue(new Callback<>() {
            @Override
            public void onResponse(...) {
                result.setValue(Resource.success(data));
            }
            
            @Override
            public void onFailure(...) {
                result.setValue(Resource.error(message, null));
            }
        });
        
        return result;
    }
}
```

---

## 📱 Màn hình chính

### 1. Authentication Flow
```
SplashActivity → LoginActivity → MainActivity
                      ↓
                RegisterActivity
```

**Features:**
- Auto-login với saved token
- Form validation
- Error handling
- Loading states

### 2. Home Screen
**Layout:** Fragment với RecyclerViews
- Search bar với debounce
- Horizontal Categories RecyclerView
- Horizontal Promotions RecyclerView
- Featured Products Grid (2 columns)
- Vertical Restaurants RecyclerView (infinite scroll)

**Features:**
- Pull-to-refresh
- Search restaurants
- Filter by category
- GPS nearby search
- Pagination

### 3. Restaurant Detail
**Layout:** CollapsingToolbarLayout
- Hero image với parallax
- Restaurant info card
- Menu products grid

**Features:**
- Call restaurant
- View on map
- Add products to cart
- Rating display

### 4. Product Detail
**Layout:** CollapsingToolbarLayout
- Product image
- Name, description
- Price với discount
- Add to cart button

### 5. Cart Screen
**Layout:** RecyclerView + Bottom Summary
- Items grouped by restaurant
- Quantity controls
- Delete items
- Price summary

**Features:**
- Update quantities
- Remove items
- Apply promotion
- Proceed to checkout

### 6. Checkout Flow
```
Cart → Select Address → Payment Method → Confirm → Order Success
```

### 7. Orders Screen
**Layout:** Tabs + RecyclerView
- Tabs: All, Pending, Delivering, Completed
- Order cards với status
- Filter, sort options

### 8. Profile Screen
**Layout:** ScrollView với Cards
- Profile header
- Menu items (Orders, Addresses, Settings)
- Logout button

**Menu Items:**
- My Orders
- Addresses
- Favorites
- Change Password
- Notifications
- Settings
- Logout

### 9. Notifications
**Layout:** RecyclerView
- Notification items với icons
- Read/Unread indicator
- Delete button
- Mark all as read

---

## 🔌 API Integration

### Retrofit Configuration

```java
// RetrofitClient.java
public class RetrofitClient {
    private static final String BASE_URL = BuildConfig.BASE_URL;
    
    private Retrofit retrofit;
    
    private RetrofitClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        AuthInterceptor authInterceptor = new AuthInterceptor(context);
        
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
        
        retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}
```

### API Interface Example

```java
public interface RestaurantApi {
    @GET("restaurants")
    Call<ApiResponse<List<Restaurant>>> getRestaurants(
        @Query("_page") int page,
        @Query("_limit") int limit
    );
    
    @GET("restaurants/nearby")
    Call<ApiResponse<List<Restaurant>>> getNearbyRestaurants(
        @Query("latitude") double latitude,
        @Query("longitude") double longitude,
        @Query("radius") double radius
    );
    
    @GET("restaurants/{id}")
    Call<ApiResponse<Restaurant>> getRestaurantById(
        @Path("id") int id
    );
}
```

### API Response Model

```java
public class ApiResponse<T> {
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private T data;
    
    @SerializedName("pagination")
    private Pagination pagination;
}
```

### Supported Endpoints

Total: **80 endpoints** từ FunFood Backend

**Main Modules:**
- ✅ Authentication (5 endpoints)
- ✅ Users (9 endpoints)
- ✅ Categories (5 endpoints)
- ✅ Restaurants (8 endpoints)
- ✅ Products (6 endpoints)
- ✅ Cart (7 endpoints)
- ✅ Orders (6 endpoints)
- ✅ Favorites (7 endpoints)
- ✅ Reviews (6 endpoints)
- ✅ Promotions (8 endpoints)
- ✅ Addresses (8 endpoints)
- ✅ Notifications (5 endpoints)

**Xem chi tiết:** [API_ENDPOINTS.md](https://github.com/Tunhoclaptrinh/FunFood-Backend/blob/main/API_ENDPOINTS.md)

---

## 🧪 Testing

### Manual Testing

```bash
# 1. Test Authentication
- Register new account
- Login with credentials
- Auto-login on app restart
- Logout

# 2. Test Home Screen
- Load categories
- Load promotions
- Load featured products
- Load restaurants
- Search restaurants
- Filter by category
- Pull to refresh
- Infinite scroll

# 3. Test Restaurant Detail
- View restaurant info
- View menu products
- Call restaurant
- Add product to cart

# 4. Test Cart
- Add/Remove items
- Update quantities
- Apply promotion
- Proceed to checkout

# 5. Test Orders
- Create order
- View order history
- Filter orders
- Cancel order

# 6. Test Profile
- Edit profile
- Change password
- Manage addresses
- View notifications
```

### Test Accounts

**Backend Test Users:**
```
Admin:
Email: admin@funfood.com
Password: 123456

Customer:
Email: user@funfood.com
Password: 123456
```

### Network Testing

```java
// NetworkUtil.java
public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager cm = (ConnectivityManager) 
        context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
}
```

**Test Cases:**
- ✅ Online mode
- ✅ Offline mode (show error)
- ✅ Slow network (loading indicators)
- ✅ Network switch (reconnect)

---

## 🎨 UI Patterns

### Loading States

```java
// Show shimmer effect while loading
binding.shimmerLayout.startShimmer();

// Show ProgressBar
binding.progressBar.setVisibility(View.VISIBLE);

// Hide loading
binding.shimmerLayout.stopShimmer();
binding.progressBar.setVisibility(View.GONE);
```

### Error Handling

```java
protected void handleError(String message) {
    if (message != null && !message.isEmpty()) {
        showSnackbar(message);
    } else {
        showSnackbar("An error occurred");
    }
}
```

### Empty States

```xml
<TextView
    android:id="@+id/tv_empty"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:text="Chưa có dữ liệu"
    android:visibility="gone" />
```

### Pull-to-Refresh

```java
binding.swipeRefresh.setOnRefreshListener(() -> {
    refreshData();
});
```

### Infinite Scroll

```java
recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(RecyclerView rv, int dx, int dy) {
        if (!isLoading && viewModel.canLoadMore()) {
            if (isAtBottom()) {
                loadMore();
            }
        }
    }
});
```

---

## 🗺️ GPS Features

### Location Permission

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### Get Current Location

```java
FusedLocationProviderClient fusedLocationClient;

fusedLocationClient.getLastLocation()
    .addOnSuccessListener(location -> {
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            searchNearbyRestaurants(lat, lon, 5.0);
        }
    });
```

### Distance Calculation

```java
// DistanceUtil.java
public static double calculateDistance(
    double lat1, double lon1, 
    double lat2, double lon2
) {
    // Haversine formula
    double R = 6371; // Earth radius in km
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    
    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * 
               Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLon/2) * Math.sin(dLon/2);
    
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
}
```

---

## 📦 Build & Release

### Debug Build

```bash
./gradlew assembleDebug
```

**Output:** `app/build/outputs/apk/debug/app-debug.apk`

### Release Build

```bash
# 1. Create keystore (first time only)
keytool -genkey -v -keystore funfood-release.keystore \
    -alias funfood -keyalg RSA -keysize 2048 -validity 10000

# 2. Configure signing in app/build.gradle
android {
    signingConfigs {
        release {
            storeFile file("funfood-release.keystore")
            storePassword "your-password"
            keyAlias "funfood"
            keyPassword "your-password"
        }
    }
}

# 3. Build release
./gradlew assembleRelease
```

**Output:** `app/build/outputs/apk/release/app-release.apk`

### ProGuard

```proguard
# app/proguard-rules.pro

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }

# Gson
-keep class com.example.funfood.data.remote.dto.** { *; }
-keep class com.example.funfood.domain.model.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
```

---

## 🚀 Future Enhancements

### Planned Features
- [ ] Push Notifications (FCM)
- [ ] In-app chat support
- [ ] Payment gateway integration
- [ ] Order tracking với real-time map
- [ ] Social login (Google, Facebook)
- [ ] Multi-language support
- [ ] Dark mode implementation
- [ ] Offline mode với Room cache
- [ ] Analytics (Firebase Analytics)
- [ ] Crash reporting (Firebase Crashlytics)

### Technical Improvements
- [ ] Migrate to Kotlin
- [ ] Implement Dagger/Hilt for DI
- [ ] Add Unit Tests (JUnit)
- [ ] Add UI Tests (Espresso)
- [ ] CI/CD pipeline
- [ ] Code coverage reports
- [ ] Performance monitoring

---

## 📄 License

This project is proprietary software for FunFood.

---

## 👥 Team

**Developed by:** FunFood Development Team  
**Version:** 1.0.0  
**Last Updated:** November 2024

---

## 📞 Support

- **Email:** support@funfood.com
- **Documentation:** [Backend API Docs](https://github.com/Tunhoclaptrinh/FunFood-Backend)

[//]: # (- **Issues:** [GitHub Issues]&#40;https://github.com/your-repo/issues&#41;)

---

**Made with ❤️ for FunFood App** | Version 1.0.0 | Android 7.0+
