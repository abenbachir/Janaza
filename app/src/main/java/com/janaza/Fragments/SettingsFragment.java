package com.janaza.Fragments;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.janaza.Factories.FragmentFactory;
import com.janaza.MainActivity;
import com.janaza.R;

import java.util.ArrayList;
import java.util.Locale;


public class SettingsFragment extends BaseFragment {

    private Locale myLocale;
    private ListView mySettingsList;
    private ArrayList<String> settingsElement = null;
    private String[] languageList = new String[3];


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsElement = new ArrayList<String>()
        {{add(getString(R.string.Set_LangTitle)); add(getString(R.string.Set_OthersTitles));}};

        languageList[0] = getString(R.string.Set_TxtEnglish);
        languageList[1] = getString(R.string.Set_TxtFrench);
        languageList[2] = getString(R.string.Set_TxtArabic);

        mySettingsList = (ListView) view.findViewById(R.id.mySettingsList);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, settingsElement);
        mySettingsList.setAdapter(myAdapter);
        mySettingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    showLocationDialog();
                }
            }
        });


        return view;
    }

    // Function to change the language within the application
    private void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        res.getConfiguration().locale = myLocale;
        res.updateConfiguration(res.getConfiguration(), res.getDisplayMetrics());

        // recreate the mainActivity so that the changes can be seen
        getActivity().recreate();

        // we need to refresh all the fragment so we close and add them again.
        for (Fragment fragment : FragmentFactory.getInstance().getAllFragments()) {
            getFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .add(R.id.fragment_container, fragment)
                    .hide(fragment)
                    .commit();
        }

        mListener.onSwitchToMainFragmentView();

        SharedPreferences Prefsettings = getActivity().getBaseContext().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor Prefeditor = Prefsettings.edit();
        Prefeditor.putString("Lang", lang);
        Prefeditor.commit();

    }

    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.Set_DialogTitle));

        builder.setItems(languageList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), languageList[which] + "", Toast.LENGTH_SHORT).show();
                switch (which) {
                    case 0:
                        setLocale("en");
                        break;

                    case 1:
                        setLocale("fr");
                        break;

                    case 2:
                        setLocale("ar");
                        break;

                    default:
                        break;

                }
            }
        });


        AlertDialog dialog = builder.create();
        // display dialog

        ListView listView = dialog.getListView();

        listView.setDivider(new ColorDrawable(Color.GRAY));

        listView.setDividerHeight(1);

        dialog.show();
    }

}


