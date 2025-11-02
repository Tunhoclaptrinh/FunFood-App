package com.example.funfood.util;

import com.example.funfood.BuildConfig;

public class AppConfig {

    // App Info
    public static final String APP_NAME = "FunFood";
    public static final String APP_VERSION = BuildConfig.VERSION_NAME;

    // API Configuration
    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final boolean IS_DEBUG = BuildConfig.DEBUG;

    // Network Configuration
    public static final int CONNECT_TIMEOUT = 30; // seconds
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;

    // Pagination
    public static final int PAGE_SIZE = 10;
    public static final int INITIAL_PAGE = 1;
    public static final int PREFETCH_DISTANCE = 5;

    // Cache
    public static final int CACHE_SIZE_MB = 50;
    public static final int CACHE_MAX_AGE_MINUTES = 5;

    // Image Loading
    public static final int IMAGE_CACHE_SIZE_MB = 100;
    public static final int IMAGE_MEMORY_CACHE_SIZE = 20; // MB

    // Debounce Times
    public static final long CLICK_DEBOUNCE_MS = 500;
    public static final long SEARCH_DEBOUNCE_MS = 300;

    // Order Status
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_PREPARING = "preparing";
    public static final String STATUS_DELIVERING = "delivering";
    public static final String STATUS_DELIVERED = "delivered";
    public static final String STATUS_CANCELLED = "cancelled";

    // Payment Methods
    public static final String PAYMENT_CASH = "cash";
    public static final String PAYMENT_CARD = "card";
    public static final String PAYMENT_MOMO = "momo";
    public static final String PAYMENT_ZALOPAY = "zalopay";

    // Notification Types
    public static final String NOTIFICATION_ORDER = "order";
    public static final String NOTIFICATION_PROMOTION = "promotion";
    public static final String NOTIFICATION_FAVORITE = "favorite";
    public static final String NOTIFICATION_SYSTEM = "system";

    // Intent Keys
    public static final String EXTRA_RESTAURANT_ID = "restaurant_id";
    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final String EXTRA_ORDER_ID = "order_id";
    public static final String EXTRA_CATEGORY_ID = "category_id";
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    // Request Codes
    public static final int REQUEST_LOCATION = 1001;
    public static final int REQUEST_STORAGE = 1002;
    public static final int REQUEST_CAMERA = 1003;
    public static final int REQUEST_LOGIN = 2001;
    public static final int REQUEST_SELECT_ADDRESS = 2002;

    // Date Formats
    public static final String DATE_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_DISPLAY = "dd/MM/yyyy HH:mm";
    public static final String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    public static final String DATE_FORMAT_TIME = "HH:mm";

    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_QUANTITY = 99;

    // Map
    public static final float DEFAULT_ZOOM = 15f;
    public static final double DEFAULT_LATITUDE = 10.7756;  // Ho Chi Minh City
    public static final double DEFAULT_LONGITUDE = 106.7019;
    public static final double NEARBY_RADIUS_KM = 5.0;

    // Splash
    public static final long SPLASH_DELAY_MS = 2000;

    // Auto Refresh
    public static final long AUTO_REFRESH_INTERVAL_MS = 30000; // 30 seconds

    private AppConfig() {
        // Private constructor to prevent instantiation
    }
}