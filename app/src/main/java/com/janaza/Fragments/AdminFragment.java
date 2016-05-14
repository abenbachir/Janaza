package com.janaza.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.janaza.OneSignal.OneSignalRestClient;
import com.janaza.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminFragment extends BaseFragment {

    private Button sendNotificationButton;
    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

//        sendNotificationButton = (Button) findViewById(R.id.send_notification);
//
//        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    postNotification(new JSONObject("{'contents': {'en':'Test Message form admin'}, 'included_segments':[\"All\"]}"), null);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        return view;
    }


    public void postNotification(JSONObject json, final com.onesignal.OneSignal.PostNotificationResponseHandler handler) {
        try {

            OneSignalRestClient.post("notifications/", json, new OneSignalRestClient.ResponseHandler(){
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
                public void onFailure(int statusCode, String response, Throwable throwable) {
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


