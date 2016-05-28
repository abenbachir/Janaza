package com.janaza.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.janaza.Adapters.ViewPagerAdapter;
import com.janaza.Factories.FragmentFactory;
import com.janaza.R;

import java.util.ArrayList;

public class MainFragment extends BaseFragment {

    private FragmentFactory fragmentFactory = FragmentFactory.getInstance();
    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        //ajout de l'adaptateur
        ArrayList<Fragment> items = new ArrayList<>();
        items.add(fragmentFactory.getHomeFragment());
        items.add(fragmentFactory.getJanazaHistoryFragment());
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), items));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return view;
    }






}


