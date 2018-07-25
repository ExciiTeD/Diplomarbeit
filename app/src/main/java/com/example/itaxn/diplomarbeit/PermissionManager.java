package com.example.itaxn.diplomarbeit;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PermissionManager {
    /**
     * The request code of the PermissionManager.
     */
    public static final int REQUEST_CODE = 1;

    /**
     * Checks the permission of the given activity.
     *
     * @param activity   the activity where the message should pop up
     * @param permission the permission that should be looked up.
     * @return true if the permission is granted otherwise false.
     */
    public static boolean checkPermission(Activity activity, String permission) {
        boolean permitted = true;
        int permissionCode = ContextCompat.checkSelfPermission(activity, permission);

        if (permissionCode == PackageManager.PERMISSION_DENIED) {
            permitted = false;
        }

        return permitted;
    }

    /**
     * Requests all permissions that are in the string array.
     *
     * @param activity    the activity where the message should pop up
     * @param permissions list of permissions
     */
    public static void requestPermission(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }
}
