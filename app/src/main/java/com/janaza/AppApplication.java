package com.janaza;

import android.app.Application;

import com.janaza.OneSignal.OneSignalNotificationOpenedHandler;
import com.janaza.OneSignal.OneSignalRestClient;
import com.onesignal.OneSignal;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler(this))
//                .setAutoPromptLocation(true)
                .init();
        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);
        OneSignal.enableNotificationsWhenActive(true);
        OneSignal.enableInAppAlertNotification(true);
        OneSignal.sendTag("masjid", "ICQ");

        OneSignalRestClient.REST_API_KEY = getResources().getString(R.string.one_signal_rest_api_key);
        OneSignalRestClient.APP_ID = getResources().getString(R.string.one_signal_app_id);
    }



}
