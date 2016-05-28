package com.janaza.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.janaza.R;
import com.janaza.Utils.Connectivity;

public class BaseFragment extends Fragment {

    protected String title;
    protected Activity mActivity;
    protected OnFragmentInteractionListener mListener;


    public BaseFragment() {
        // Required empty public constructor
    }

    public String getTitle(){
        return title;
    }

    public Drawable getIcon(){
        return mActivity.getResources().getDrawable(R.drawable.common_full_open_on_phone);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!this.isHidden()) {
            onVisible();
        } else {
            onHidden();
        }
    }

    public boolean isNetworkAvailable() {
        return Connectivity.isNetworkOnline(getContext());
    }

    public void onVisible() {
    }

    public void onHidden() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        mListener = null;
    }
}
