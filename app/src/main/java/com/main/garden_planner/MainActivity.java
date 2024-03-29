package com.main.garden_planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.garden_planner.BuildConfig;
import com.example.garden_planner.R;
import com.example.garden_planner.databinding.ActivityMainBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.main.garden_planner.fragments.GardenDetailFragment;
import com.main.garden_planner.fragments.GardenFeedFragment;
import com.main.garden_planner.fragments.ProfileFragment;
import com.main.garden_planner.fragments.RemindersFragment;
import com.main.garden_planner.models.Garden;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.LogOutCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    public BottomNavigationView bottomNavigationView;
    public FrameLayout flContainer;
    final GardenFeedFragment feedFragment = new GardenFeedFragment();
    final RemindersFragment remindersFragment = new RemindersFragment();
    final ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.put("user", ParseUser.getCurrentUser());
        ParseUser.getCurrentUser().put("installationID", installation.getInstallationId());

        ParseUser.getCurrentUser().saveInBackground();
        installation.saveInBackground();

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true); //objects created are writable
        ParseACL.setDefaultACL(defaultACL, true);

        View view = binding.getRoot();
        setContentView(view);

        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        PlacesClient placesClient = Places.createClient(this);

        bottomNavigationView = binding.bottomNavigation;
        flContainer = binding.flContainer;

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_reminders:
                        fragment = remindersFragment;
                        // do something here
                        break;
                    case R.id.action_profile:
                        // profileFragment.user = (User) ParseUser.getCurrentUser();
                        fragment = profileFragment;
                        break;
                    case R.id.action_gardens:

                    default:
                        fragment = feedFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;

            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_gardens);

    }
    public void performLogout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
                startActivity(i);
                finish();
            }
        });

    }

    public void goToDetailGardenView(Garden garden){
        bottomNavigationView.setSelectedItemId(R.id.action_gardens);

        GardenDetailFragment detailFragment = new GardenDetailFragment();

        detailFragment.garden = garden;

        fragmentManager.beginTransaction().replace(R.id.flContainer, detailFragment).commit();

    }



}