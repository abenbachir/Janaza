package com.janaza;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.janaza.Factories.FragmentFactory;
import com.janaza.Fragments.OnFragmentInteractionListener;
import com.janaza.Services.AccountManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnFragmentInteractionListener {

    private static final int RC_SIGN_IN = 0;
    private FragmentManager mFragmentManager;
    private android.support.v4.app.FragmentTransaction mFragmentTransaction;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToogle;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private Fragment activeFragment;
    private AccountManager accountManager = AccountManager.getInstance();
    private SignInButton btnSignIn;
    private GoogleApiClient mGoogleApiClient;
    private boolean signInClicked = false;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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
//        for (Fragment fragment : FragmentFactory.getInstance().getAllFragments()) {
//            fragmentManager
//                    .beginTransaction()
//                    .add(R.id.fragment_container, fragment)
//                    .hide(fragment)
//                    .commit();
//        }
        //montre le premier fragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container,activeFragment)
                .commit();

        View headerLayout = mNavigationView.getHeaderView(0);
        btnSignIn = (SignInButton) headerLayout.findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickSignIn(v);
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (!accountManager.isConnected()) {
            mGoogleApiClient = buildGoogleAPIClient();
            accountManager.setGoogleApiClient(mGoogleApiClient);
        }

        updateAccountBox();
        updateNavigationMenuItems();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!accountManager.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    private GoogleApiClient buildGoogleAPIClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    @NonNull
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;
            if (signInClicked) {
                //déjà connecté
                resolveSignInError();
            }
        }
        btnSignIn.setEnabled(true);
        btnSignIn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                signInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    public void onConnected(Bundle arg0) {
        signInClicked = false;
        accountManager.updateAccount();
        updateAccountBox();
        updateNavigationMenuItems();
    }

    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    private void OnClickSignIn(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            signInClicked = true;
            resolveSignInError();
        }
        btnSignIn.setEnabled(false);
    }

    //erreurs dans le signin
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    protected void updateAccountBox() {
         View headerLayout = mNavigationView.getHeaderView(0);

         LinearLayout signinLayout = (LinearLayout) headerLayout.findViewById(R.id.nav_header_signin_options_layout);
         LinearLayout displayLayout = (LinearLayout) headerLayout.findViewById(R.id.nav_header_display_layout);
         signinLayout.setVisibility(View.GONE);
         displayLayout.setVisibility(View.GONE);
         if(accountManager.getAccount() != null) {
             String url = accountManager.getAccount().getPictureUrl();
             TextView profileName = (TextView) headerLayout.findViewById(R.id.nav_profileName);
             TextView profileEmail = (TextView) headerLayout.findViewById(R.id.nav_profileEmail);
             profileName.setText(accountManager.getAccount().getDisplayName());
             profileEmail.setText(accountManager.getAccount().getEmail());
             displayLayout.setVisibility(View.VISIBLE);
         }else{
             btnSignIn.setEnabled(true);
             signinLayout.setVisibility(View.VISIBLE);
         }
     }

    protected void updateNavigationMenuItems() {

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
        Toast.makeText(getBaseContext(),""+id,Toast.LENGTH_SHORT);
        if (id == R.id.nav_signOut) {
            accountManager.disconnect();
            updateAccountBox();
            updateNavigationMenuItems();
        } else {
            switchFragment(FragmentFactory.getInstance().getFragment(id));
            item.setChecked(true);
            setTitle(item.getTitle());
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }


    protected void switchFragment(Fragment toFragment) {
        if (activeFragment != toFragment) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out)
//                    .hide(activeFragment)
//                    .show(toFragment)
                    .replace(R.id.fragment_container, toFragment)
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
