package com.mino.woway.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mino.woway.R;
import com.mino.woway.helpers.DialogHelper;

import static com.mino.woway.utils.Constants.ERROR_DIALOG_REQUEST;

public class GoogleServicesAvailability {

    private static final String TAG = "GoogleServicesAvailable";

    public static boolean isServicesOk(Activity activity) {
        Log.d(TAG, "isServicesOk: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOk: Google Play Services is working");
            enableGpsIfItDisabled(activity);
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOk: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.cant_make_location_request_message), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private static void enableGpsIfItDisabled(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            DialogHelper.buildAlertMessageNoGps(activity);
        }
    }

}
