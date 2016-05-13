package com.janaza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String appId = "";
    private Button sendNotificationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendNotificationButton = (Button) findViewById(R.id.send_notification);

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postNotification(new JSONObject("{'contents': {'en':'Test Message form admin'}, 'included_segments':[\"All\"]}"), null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void postNotification(JSONObject json, final OneSignal.PostNotificationResponseHandler handler) {
        try {

            OneSignalRestClient.post("notifications/", json, new OneSignalRestClient.ResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Log.d("postNotification ", "HTTP create notification success: " + (response != null ? response : "null"));
                    if (handler != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("errors"))
                                handler.onFailure(jsonObject);
                            else
                                handler.onSuccess(new JSONObject(response));
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }

                @Override
                void onFailure(int statusCode, String response, Throwable throwable) {
                    logHttpError("create notification failed", statusCode, throwable, response);

                    if (statusCode == 0)
                        response = "{'error': 'HTTP no response error'}";

                    if (handler != null) {
                        try {
                            handler.onFailure(new JSONObject(response));
                        } catch (Throwable t) {
                            handler.onFailure(null);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.d("postNotification ", "HTTP create notification json exception!", e);
            if (handler != null) {
                try {
                    handler.onFailure(new JSONObject("{'error': 'HTTP create notification json exception!'}"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static void logHttpError(String errorString, int statusCode, Throwable throwable, String errorResponse) {
        String jsonError = "";
        if (errorResponse != null)
            jsonError = "\n" + errorResponse + "\n";
        Log.d("logHttpError ", "HTTP code: " + statusCode + " " + errorString + jsonError, throwable);
    }
}
