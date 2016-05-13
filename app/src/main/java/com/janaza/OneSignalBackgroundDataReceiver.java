package com.janaza;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import org.json.JSONObject;

/*
Example JSON payload.
{
  "app_id" : "YOUR_ONESIGNAL_APP_ID",
  "include_player_ids": ["YOUR_ONESIGNAL_PLAYER_ID"],
  "contents" : {
"en" : "Message 1"
},
  "android_background_data": true,
  "data": {"yourCustomKey": "value123"}
}
*/

public class OneSignalBackgroundDataReceiver extends WakefulBroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle dataBundle = intent.getBundleExtra("data");

        try {
            Log.i("-------------", "----------------------------");
            Log.i("OneSignalExample", "NotificationTable title: " + dataBundle.getString("title"));
            Log.i("OneSignalExample", "Is Your App Active: " + dataBundle.getBoolean("isActive"));
            Log.i("OneSignalExample", "data additionalData: " + dataBundle.getString("custom"));
            JSONObject customJSON = new JSONObject(dataBundle.getString("custom"));
            if (customJSON.has("a")) {
                JSONObject additionalData = customJSON.getJSONObject("a");
                if (additionalData.has("yourCustomKey"))
                    Log.i("OneSignalExample", "additionalData:yourCustomKey: " + additionalData.getString("yourCustomKey"));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}