package com.mino.woway.helpers;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.mino.woway.R;

import static com.mino.woway.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class DialogHelper {

    public static void buildAlertMessageNoGps(Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.requires_gps_message))
                .setCancelable(false)
                .setPositiveButton(activity.getResources().getString(R.string.positive_button_dialog), (dialog, id) -> {
                    Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
