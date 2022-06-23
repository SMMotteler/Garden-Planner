package com.example.garden_planner;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.garden_planner.adapters.GardenFeedAdapter;
import com.example.garden_planner.models.Garden;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

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

    public static String profilePic(ParseUser user){
        ParseFile image = (ParseFile)user.get("profilePic");
        if (image == null){
            return "android.resource://com.example.parsetagram/"+R.drawable.default_pic;
        }
        return image.getUrl();
    }

    public void queryGarden(List<Garden> userGardens, GardenFeedAdapter adapter, ParseUser user){
        ParseQuery<Garden> query = ParseQuery.getQuery(Garden.class);

        query.whereEqualTo(Garden.KEY_USER, user);
        query.addDescendingOrder("createdAt");
        query.include(Garden.KEY_NAME);
        // start an asynchronous call for comments
        query.findInBackground(new FindCallback<Garden>() {
            @Override
            public void done(List<Garden> gardens, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("Detail Activity", "Issue with getting gardens", e);
                    return;
                }

                // for debugging purposes let's print every garden name to LogCat
                for (Garden garden : gardens) {
                    Log.i("Garden Query", "Garden: " + garden.getName());
                }

                // save user garden to list and notify adapter of new data
                userGardens.clear();
                userGardens.addAll(gardens);

                adapter.notifyDataSetChanged();
            }
        });

    }

}
