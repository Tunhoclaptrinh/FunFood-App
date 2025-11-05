package com.example.funfood.util;

public class Constants {

    // API
    public static final String BASE_URL = "https://funfood-backend-67v4.onrender.com/api/";

    // SharedPreferences
    public static final String PREF_NAME = "FunFoodPrefs";
    public static final String KEY_TOKEN = "jwt_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_ROLE = "user_role";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // Request Codes
    public static final int REQUEST_LOCATION_PERMISSION = 1001;
    public static final int REQUEST_STORAGE_PERMISSION = 1002;

    // Bundle Keys
    public static final String KEY_RESTAURANT_ID = "restaurant_id";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_CATEGORY_ID = "category_id";

    // Network
    public static final int CONNECT_TIMEOUT = 30; // seconds
    public static final int READ_TIMEOUT = 30; // seconds
    public static final int WRITE_TIMEOUT = 30; // seconds

    // Pagination
    public static final int PAGE_SIZE = 10;
    public static final int INITIAL_PAGE = 1;

    // Order Status
    public static final String ORDER_STATUS_PENDING = "pending";
    public static final String ORDER_STATUS_CONFIRMED = "confirmed";
    public static final String ORDER_STATUS_PREPARING = "preparing";
    public static final String ORDER_STATUS_DELIVERING = "delivering";
    public static final String ORDER_STATUS_DELIVERED = "delivered";
    public static final String ORDER_STATUS_CANCELLED = "cancelled";

    // Payment Methods
    public static final String PAYMENT_CASH = "cash";
    public static final String PAYMENT_CARD = "card";
    public static final String PAYMENT_MOMO = "momo";
    public static final String PAYMENT_ZALOPAY = "zalopay";

    // Discount Types
    public static final String DISCOUNT_PERCENTAGE = "percentage";
    public static final String DISCOUNT_FIXED = "fixed";
    public static final String DISCOUNT_DELIVERY = "delivery";

    // Intent Actions
    public static final String ACTION_ORDER_UPDATED = "com.example.funfood.ORDER_UPDATED";
    public static final String ACTION_CART_UPDATED = "com.example.funfood.CART_UPDATED";

    // Error Messages
    public static final String ERROR_NETWORK = "Network error. Please check your connection.";
    public static final String ERROR_SERVER = "Server error. Please try again later.";
    public static final String ERROR_UNAUTHORIZED = "Please login to continue.";
    public static final String ERROR_NOT_FOUND = "Item not found.";
    public static final String ERROR_VALIDATION = "Please check your input.";

    // Success Messages
    public static final String SUCCESS_LOGIN = "Login successful!";
    public static final String SUCCESS_REGISTER = "Registration successful!";
    public static final String SUCCESS_ORDER_PLACED = "Order placed successfully!";
    public static final String SUCCESS_ADDED_TO_CART = "Added to cart!";
    public static final String SUCCESS_ADDED_TO_FAVORITES = "Added to favorites!";

    // Date Format
    public static final String DATE_FORMAT_DEFAULT = "dd/MM/yyyy HH:mm";
    public static final String DATE_FORMAT_ORDER = "dd MMM yyyy, HH:mm";

    // Currency
    public static final String CURRENCY_SYMBOL = "đ";
    public static final String CURRENCY_FORMAT = "%,d đ";


    private Constants() {
        // Private constructor to prevent instantiation
    }
}