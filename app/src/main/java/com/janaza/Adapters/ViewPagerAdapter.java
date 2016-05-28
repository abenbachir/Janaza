package com.janaza.Adapters;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.janaza.Factories.FragmentFactory;
import com.janaza.Fragments.BaseFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private FragmentFactory fragmentFactory = FragmentFactory.getInstance();
    private Resources resources;
    private ArrayList<Fragment> items;
    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> items) {
        super(fm);
        this.items = items;
    }


    //retourne le fragment avec la bonne position
    @Override
    public Fragment getItem(int position) {

        return items.get(position);
    }

    @Override
    public int getCount() {

        return items.size();

    }

    //retourne le nom des onglets en fonction de la position
    @Override
    public CharSequence getPageTitle(int position) {
        BaseFragment fragment = (BaseFragment)items.get(position);

//        return fragment.getTitle();

        Drawable image = fragment.getIcon();
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}