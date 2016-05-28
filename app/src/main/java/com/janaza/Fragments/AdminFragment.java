package com.janaza.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.janaza.OneSignal.OneSignalRestClient;
import com.janaza.R;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class AdminFragment extends BaseFragment {

    private Button sendNotificationButton;
    private Spinner numberPersonSpinner;
    private Spinner janazaPlacesSpinner;
    private CheckBox manCheckBox;
    private CheckBox womenCheckBox;
    private CheckBox childCheckBox;
    private EditText janazaPlaceOther;

    private TimePicker timePicker;
    private DatePicker datePicker;

    private final String[] janazaPlaces = {"Islamic Center Of Quebec (ICQ)", "Qoubaa", "Other"};
    private final Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

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

//        this.title = getResources().getString(R.string.menu_);

        numberPersonSpinner = (Spinner) view.findViewById(R.id.numberPersonSpinner);
        numberPersonSpinner.setAdapter(new ArrayAdapter<Integer>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, numbers));
        janazaPlacesSpinner = (Spinner) view.findViewById(R.id.janazaPlacesSpinner);
        janazaPlacesSpinner.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, janazaPlaces));

        janazaPlacesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = janazaPlaces[position];
                janazaPlaceOther.setVisibility(item.equals("Other") ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        manCheckBox = (CheckBox) view.findViewById(R.id.manCheckBox);
        womenCheckBox = (CheckBox) view.findViewById(R.id.womenCheckBox);
        childCheckBox = (CheckBox) view.findViewById(R.id.childCheckBox);
        janazaPlaceOther = (EditText) view.findViewById(R.id.janazaPlaceOther);
        datePicker = (DatePicker) view.findViewById(R.id.janaza_datePicker);
        if(Build.VERSION.SDK_INT >= 23)
            datePicker.setMinDate(System.currentTimeMillis());

        timePicker = (TimePicker) view.findViewById(R.id.janaza_timePicker);
        if(Build.VERSION.SDK_INT >= 23) {
            timePicker.setHour(13);
            timePicker.setMinute(0);
        }
        sendNotificationButton = (Button) view.findViewById(R.id.send_janaza_notification);
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

        reset();

        return view;
    }

    @Override
    public void onVisible() {
        super.onVisible();

        reset();
    }

    protected void reset()
    {
        manCheckBox.setChecked(false);
        womenCheckBox.setChecked(false);
        childCheckBox.setChecked(false);
        janazaPlaceOther.setText("");
        janazaPlaceOther.setVisibility(View.GONE);
        if(janazaPlacesSpinner.getSelectedItem().toString().equals("Other")){
            janazaPlaceOther.setVisibility(View.VISIBLE);
        }
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


