package com.janaza.Fragments;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.janaza.R;

public class HomeFragment extends BaseFragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public Drawable getIcon(){
        return this.getResources().getDrawable(R.drawable.home);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.title = view.getTag().toString();
//        this.title = getResources().getString(R.string.menu_home);
        return view;
    }






}


