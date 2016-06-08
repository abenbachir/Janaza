package com.janaza;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import com.janaza.OneSignal.OneSignalNotificationOpenedHandler;
import com.janaza.OneSignal.OneSignalRestClient;
import com.janaza.Services.JanazaManager;
import com.janaza.Services.TerminalManager;
import com.onesignal.OneSignal;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // add new Fonts
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/UniversNextArabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // set janazat
        JanazaManager.getInstance().setCollection(TerminalManager.getInstance().getJanazat());

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
