package hatchure.designs.Helpers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class Utils {
    public static final String MyPREFERENCES = "TownyPreferences";
    public static final String PhoneNumber = "PhoneNumber";

    public static boolean IsNetworkAvailable(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null && !activeNetwork.isConnectedOrConnecting()) {
            Toast.makeText(context, "Something went wrong. Please check your internet connection.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static void ResetAppPreferences(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Utils.PhoneNumber, "");
        editor.commit();
    }

    public static SharedPreferences.Editor GetSharedPreferencesEditor(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.edit();
    }

    public static ProgressDialog GetProcessDialog(Context context) {
        final ProgressDialog p = new ProgressDialog(context);
        p.setMessage("Please wait...");
        p.setIndeterminate(false);
        p.setCancelable(false);
        return p;
    }
}
