package com.janaza.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.janaza.Factories.FragmentFactory;
import com.janaza.MainActivity;
import com.janaza.R;

import java.util.Locale;

public class SettingsFragment extends BaseFragment {

    private Button changeLang;
    private Locale myLocale;
    private int Cmpt = 0;

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
        changeLang = (Button) view.findViewById(R.id.changeLanguage);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (Cmpt){
                    case 0 :
                        //Toast.makeText(getContext(),"ar",Toast.LENGTH_LONG).show();
                        setLocale("ar");
                        Cmpt ++;
                        break;

                    case 1 :
                        //Toast.makeText(getContext(),"fr",Toast.LENGTH_LONG).show();
                        setLocale("fr");
                        Cmpt = 0;
                        break;

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

    }


}


