package com.example.funfood.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {

    // Location permissions
    public static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    // Storage permissions
    public static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Camera permission
    public static final String[] CAMERA_PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    /**
     * Check if permission is granted
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check if all permissions are granted
     */
    public static boolean arePermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check location permissions
     */
    public static boolean hasLocationPermission(Context context) {
        return isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION) ||
                isPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * Check storage permissions
     */
    public static boolean hasStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ doesn't need storage permission for most cases
            return true;
        }
        return arePermissionsGranted(context, STORAGE_PERMISSIONS);
    }

    /**
     * Check camera permission
     */
    public static boolean hasCameraPermission(Context context) {
        return isPermissionGranted(context, Manifest.permission.CAMERA);
    }

    /**
     * Request permissions
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * Request location permissions
     */
    public static void requestLocationPermissions(Activity activity, int requestCode) {
        requestPermissions(activity, LOCATION_PERMISSIONS, requestCode);
    }

    /**
     * Request storage permissions
     */
    public static void requestStoragePermissions(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // No need to request on Android 13+
            return;
        }
        requestPermissions(activity, STORAGE_PERMISSIONS, requestCode);
    }

    /**
     * Request camera permission
     */
    public static void requestCameraPermission(Activity activity, int requestCode) {
        requestPermissions(activity, CAMERA_PERMISSIONS, requestCode);
    }

    /**
     * Check if should show rationale
     */
    public static boolean shouldShowRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * Handle permission result
     */
    public static boolean handlePermissionResult(int[] grantResults) {
        if (grantResults.length == 0) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}