package com.janaza.OneSignal;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import com.onesignal.OneSignal;
import org.json.JSONObject;


public class OneSignalNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    private Context context;

    public OneSignalNotificationOpenedHandler(Context context){
        this.context = context;
    }

    /**
     * Callback to implement in your app to handle when a notification is opened from the Android status bar or
     * a new one comes in while the app is running.
     * This method is located in this Application class as an example, you may have any class you wish implement NotificationOpenedHandler and define this method.
     *
     * @param message        The message string the user seen/should see in the Android status bar.
     * @param additionalData The additionalData key value pair section you entered in on onesignal.com.
     * @param isActive       Was the app in the foreground when the notification was received.
     */
    @Override
    public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {

        AlertDialog.Builder dialog = null;
        if (isActive) {
            // If a push notification is received when the app is being used it does not display in the notification bar so display in the app.
            dialog = new AlertDialog.Builder(context)
                    .setTitle("OneSignal Message")
                    .setMessage(message);
        }

        // Add your app logic around this so the user is not interrupted during gameplay.
        if (dialog != null) {
            dialog.setCancelable(true)
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }
    }
}