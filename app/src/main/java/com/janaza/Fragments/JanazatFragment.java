package com.janaza.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janaza.R;

public class JanazatFragment extends BaseFragment {

    public JanazatFragment() {
        // Required empty public constructor
    }

    @Override
    public Drawable getIcon(){
        return this.getResources().getDrawable(R.drawable.history);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_janazat, container, false);

        this.title = view.getTag().toString();
        return view;
    }
    
}


