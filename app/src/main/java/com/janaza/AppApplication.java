package com.janaza;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import com.janaza.OneSignal.OneSignalNotificationOpenedHandler;
import com.janaza.OneSignal.OneSignalRestClient;
import com.onesignal.OneSignal;

import java.util.Locale;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AssetManager am = this.getAssets();

        Typeface typeface = Typeface.createFromAsset(am,
                String.format(new Locale("", "fr", "CA"), "fonts/%s", "UniversNextArabic-Regular.ttf"));

        //setTypeface(typeface);

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
