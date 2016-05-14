package com.janaza;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.janaza.Factories.FragmentFactory;
import com.janaza.Fragments.OnFragmentInteractionListener;
import com.janaza.OneSignal.OneSignalRestClient;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener
 {

    private FragmentManager mFragmentManager;
    private android.support.v4.app.FragmentTransaction mFragmentTransaction;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToogle;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Home");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Setup DrawerLayout et NavigationView
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToogle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToogle);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        //ajouter le premier fragment
        mFragmentManager = getSupportFragmentManager();
        //mDrawerLayout.openDrawer(GravityCompat.START);

        mNavigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        activeFragment = FragmentFactory.getInstance().getMainFragment();

        //ajoute tous les fragments et les hides
        for (Fragment fragment : FragmentFactory.getInstance().getAllFragments()) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .hide(fragment)
                    .commit();
        }
        //montre le premier fragment
        fragmentManager.beginTransaction()
                .show(activeFragment)
                .commit();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // `onPostCreate` called when activity start-up is complete after `onStart()`
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToogle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToogle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //ORGANISATION DU CHANGEMENT DE VUES/FRAGMENTS DANS LA BARRE DE NAVIGATION
        int id = item.getItemId();
        if (id == R.id.nav_signOut) {
//            Intent intent = new Intent(this, SplashActivity.class);
//            startActivity(intent);
//            finish();
        } else {
            switchFragment(FragmentFactory.getInstance().getFragment(id));
        }
        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void switchFragment(Fragment toFragment) {
        if (activeFragment != toFragment) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out)
                    .hide(activeFragment)
                    .show(toFragment)
                    .commit();
            activeFragment = toFragment;
        }
    }

    public void onSwitchToFragmentView(Fragment toFragment) {
        switchFragment(toFragment);
    }

    public void onSwitchToMainFragmentView() {
        switchFragment(FragmentFactory.getInstance().getMainFragment());
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }


}
