package com.janaza.Factories;


import android.support.v4.app.Fragment;

import com.janaza.Fragments.MainFragment;
import com.janaza.R;
import com.janaza.Fragments.AdminFragment;
import com.janaza.Fragments.HomeFragment;
import com.janaza.Fragments.JanazatFragment;
import com.janaza.Fragments.SalatFragment;
import com.janaza.Fragments.SettingsFragment;
import com.janaza.Fragments.SunanFragment;

import java.util.Collection;
import java.util.HashMap;

public class FragmentFactory {

    protected static FragmentFactory INSTANCE = null;
    protected HashMap<String, Fragment> cache = new HashMap<>();

    protected FragmentFactory() {
        try {
            //navigationbar
//            cache.put(HomeFragment.class.toString(), HomeFragment.class.newInstance());
//            cache.put(JanazatFragment.class.toString(), JanazatFragment.class.newInstance());
//            cache.put(SunanFragment.class.toString(),  SunanFragment.class.newInstance());
//            cache.put(SalatFragment.class.toString(),  SalatFragment.class.newInstance());
//            cache.put(SettingsFragment.class.toString(),  SettingsFragment.class.newInstance());
//            cache.put(AdminFragment.class.toString(),  AdminFragment.class.newInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FragmentFactory getInstance() {

        if (INSTANCE == null) {
            synchronized (FragmentFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FragmentFactory();
                }
            }
        }
        return INSTANCE;
    }


    public Fragment getMainFragment() {
        return getFragment(-1);
    }
    public Fragment getAdminFragment() {
        return getFragment(R.id.nav_send_notification);
    }
    public Fragment getHomeFragment() {
        return getFragment(HomeFragment.class);
    }
    public Fragment getJanazaHistoryFragment() {
        return getFragment(JanazatFragment.class);
    }
    public Collection<Fragment> getAllFragments() {
        return cache.values();
    }

    protected Fragment getFragment(Class fragmentClass){
        String fragmentName = fragmentClass.toString();
        Fragment fragment;
        if (!cache.containsKey(fragmentName)) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                cache.put(fragmentName, fragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fragment = cache.get(fragmentName);
        return fragment;
    }
    public Fragment getFragment(int itemId) {

        Class fragmentClass;

        switch (itemId) {
            case R.id.nav_main:
                fragmentClass = MainFragment.class;
                break;
//            case R.id.nav_home:
//                fragmentClass = HomeFragment.class;
//                break;
//            case R.id.nav_janazat:
//                fragmentClass = JanazatFragment.class;
//                break;
//            case R.id.nav_sunan:
//                fragmentClass = SunanFragment.class;
//                break;
//            case R.id.nav_salat:
//                fragmentClass = SalatFragment.class;
//                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_send_notification:
                fragmentClass = AdminFragment.class;
                break;
            default:
                fragmentClass = MainFragment.class;
        }

        return getFragment(fragmentClass);
    }
}
