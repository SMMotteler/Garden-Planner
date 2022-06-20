package com.example.garden_planner;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class GardenMethodHelper {

    public static void goMainActivity(Activity activity) {
        // Toast.makeText(activity.getApplicationContext(), "Logged in!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public static void loginUser(String username, String password, Activity activity) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    Toast.makeText(activity, "Issue with login :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity(activity);
            }
        });
    }

}
