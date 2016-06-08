package com.janaza.OneSignal;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.janaza.Models.Janaza;
import com.janaza.Services.JanazaManager;
import com.janaza.Services.TerminalManager;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationPayload;
import com.onesignal.NotificationExtenderService;

import org.json.JSONObject;

import java.math.BigInteger;

public class OneSignalNotificationExtenderService extends NotificationExtenderService {
    private JanazaManager janazaManager = JanazaManager.getInstance();
    private TerminalManager terminalManager = TerminalManager.getInstance();
    @Override
    protected boolean onNotificationProcessing(OSNotificationPayload notification) {

        Gson gson = new Gson();
        String json = notification.additionalData.toString();
        Janaza janaza = gson.fromJson(json, Janaza.class);
        janazaManager.addJanaza(janaza);
        terminalManager.updateJanazat(janazaManager.getCollection());

//        OverrideSettings overrideSettings = new OverrideSettings();
//        overrideSettings.extender = new NotificationCompat.Extender() {
//            @Override
//            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
//                // Sets the background notification color to Green on Android 5.0+ devices.
//                return builder.setColor(new BigInteger("FF00FF00", 16).intValue());
//            }
//        };
//
//        OSNotificationDisplayedResult result = displayNotification(overrideSettings);
//        Log.d("OneSignalExample", "Notification displayed with id: " + result.notificationId);

        return false;
    }
}