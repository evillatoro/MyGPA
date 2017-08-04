package com.gpaye.exv0948.mygpa.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gpaye.exv0948.mygpa.R;
import com.gpaye.exv0948.mygpa.model.Course;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";

    public Toolbar toolbar;
    private HomeFragment homeFragment;
    private AdView adView;
    private boolean testing = false;
    private boolean showAd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4851595901170430~6140397369");

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int numberOfDecimals = 1;
        editor.putInt(getString(R.string.number_of_decimals), numberOfDecimals);
        editor.apply();

        // sets up toolbar for application
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // nav drawer functionality
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                hideKeyBoard();
                getCurrentFocus().clearFocus();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // converts hamburger menu to back button
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backStackSize = getSupportFragmentManager().getBackStackEntryCount();
                Log.d(TAG, "**************** back stack size " + backStackSize);
                if (backStackSize > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                } else {
                    //show hamburger
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false); // show hamburger menu
                    drawerToggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    });
                    toolbar.setTitle("MyGPA");
                }
            }
        });

//        if(showAd) {
//            adView = (AdView) findViewById(R.id.adView);
//            if (testing) {
//                AdRequest adRequest = new AdRequest.Builder()
//                        .addTestDevice("513646C6809FB4097504AB5CB3DCA2DB")
//                        .build();
//                adRequest.isTestDevice(this);
//                adView.setVisibility(View.GONE);
//                adView.loadAd(adRequest);
//            } else {
//                AdRequest adRequest = new AdRequest.Builder()
//                        .build();
//                adRequest.isTestDevice(this);
//                adView.setVisibility(View.GONE);
//                adView.loadAd(adRequest);
//            }
//            adView.setAdListener(new AdListener() {
//                @Override
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//
//                    adView.setVisibility(View.VISIBLE);
//                }
//            });
//        }

        setupHomeFragmentInContainer();
    }

    private void setupHomeFragmentInContainer() {
        homeFragment = new HomeFragment();
        FragmentTransaction k = getSupportFragmentManager().beginTransaction();
        k.replace(R.id.frameContainer, homeFragment);
        //k.addToBackStack(null); include if you want to have a blank screen
        k.commit();
    }

    public void goToAddSemesterFragment() {
        AddSemesterFragment addSemesterFragment = new AddSemesterFragment();
        addSemesterFragment.registerListener(homeFragment);

        FragmentTransaction k = getSupportFragmentManager().beginTransaction();
        k.replace(R.id.frameContainer, addSemesterFragment);
        k.addToBackStack(null);
        k.commit();
    }

    public void goToSettingsFragment() {
        EditGradeScaleFragment editGradeScaleFragment = new EditGradeScaleFragment();

        FragmentTransaction k = getSupportFragmentManager().beginTransaction();
        k.replace(R.id.frameContainer, editGradeScaleFragment);
        k.addToBackStack(null);
        k.commit();
    }

    public void goToCourseListFragment(String semesterName) {
        CourseListFragment courseListFragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putString("semesterName", semesterName);
        courseListFragment.setArguments(args);

        FragmentTransaction k = getSupportFragmentManager().beginTransaction();
        k.replace(R.id.frameContainer, courseListFragment);
        k.addToBackStack(null);
        k.commit();
    }

    public void goToAddCourseFragment(CourseListFragment courseListFragment, String semesterName) {
        AddEditCourseFragment addEditCourseFragment = new AddEditCourseFragment();
        addEditCourseFragment.registerListener(courseListFragment);
        Bundle args = new Bundle();
        args.putString("semesterName", semesterName);
        args.putInt("type", 0);
        addEditCourseFragment.setArguments(args);

        FragmentTransaction k = getSupportFragmentManager().beginTransaction();
        k.replace(R.id.frameContainer, addEditCourseFragment);
        k.addToBackStack(null);
        k.commit();
    }

    public void goToEditCourseFragment(CourseListFragment courseListFragment, Course course) {
        AddEditCourseFragment addEditCourseFragment = new AddEditCourseFragment();
        addEditCourseFragment.registerListener(courseListFragment);
        Bundle args = new Bundle();
        args.putInt("type", 1);
        args.putString("semesterName", course.getSemester());
        args.putString("courseName", course.getName());
        args.putDouble("courseGrade", course.getGrade());
        args.putDouble("courseCredits", course.getCredits());
        args.putInt("courseCountTowardsGPA", course.getCountTowardsGPA());
        addEditCourseFragment.setArguments(args);

        FragmentTransaction k = getSupportFragmentManager().beginTransaction();
        k.replace(R.id.frameContainer, addEditCourseFragment);
        k.addToBackStack(null);
        k.commit();
    }

    private void goToNumberOfDecimalsFragment() {
        NumberOfDecimalsFragment numberOfDecimalsFragment = new NumberOfDecimalsFragment();

        FragmentTransaction k = getSupportFragmentManager().beginTransaction();
        k.replace(R.id.frameContainer, numberOfDecimalsFragment);
        k.addToBackStack(null);
        k.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_grade_scale) {
            Log.d(TAG, "**********************GO TO ADJUST GRADE SCALE SCREEN");
            clearBackStackAndGoToHomeFragment();
            goToSettingsFragment();

        } else if (id == R.id.nav_home) {
            Log.d(TAG, "**********************GO TO HOME SCREEN");
            clearBackStackAndGoToHomeFragment();
        } else if (id == R.id.nav_rate_app) {
            Log.d(TAG, "**********************GO TO GOOGLE PLAY STORE PAGE");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.gpaye.exv0948.mygpa"));
            startActivity(intent);
        } else if (id == R.id.nav_decimals) {
            Log.d(TAG, "**********************GO TO NUMBER OF DECIMALS PAGE");
            goToNumberOfDecimalsFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_add_semester) {
//            goToAddSemesterFragment();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void hideKeyBoard() {
        try  {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.d(TAG, "hideKeyBoard: " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
//        if (showAd) {
//            adView.setVisibility(View.GONE);
//            if (testing) {
//                AdRequest adRequest = new AdRequest.Builder()
//                        .addTestDevice("513646C6809FB4097504AB5CB3DCA2DB")
//                        .build();
//                adRequest.isTestDevice(this);
//                adView.setVisibility(View.GONE);
//                adView.loadAd(adRequest);
//            } else {
//                AdRequest adRequest = new AdRequest.Builder()
//                        .build();
//                adRequest.isTestDevice(this);
//                adView.setVisibility(View.GONE);
//                adView.loadAd(adRequest);
//            }
//        }
        hideKeyBoard();
        super.onBackPressed();
    }

    private void clearBackStackAndGoToHomeFragment() {
        hideKeyBoard();
        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
}
