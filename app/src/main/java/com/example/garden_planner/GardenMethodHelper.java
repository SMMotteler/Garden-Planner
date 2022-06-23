package com.example.garden_planner;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.garden_planner.adapters.GardenFeedAdapter;
import com.example.garden_planner.adapters.PlantInBedAdapter;
import com.example.garden_planner.adapters.ReminderAdapter;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.Reminder;
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
        // start an asynchronous call for gardens
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

    public void queryReminders(List<Reminder> userReminders, ReminderAdapter adapter, ParseUser user, Boolean byTime){
        ParseQuery<Reminder> query = ParseQuery.getQuery(Reminder.class);

        query.whereEqualTo(Reminder.KEY_REMIND_WHO, user);
        if(byTime){
            query.addAscendingOrder(Reminder.KEY_REMINDER_START);
        }
        else{
            query.addAscendingOrder(Reminder.KEY_REMIND_WHAT);
        }
        query.include(Reminder.KEY_REMINDER_MESSAGE);
        // start an asynchronous call for reminders
        query.findInBackground(new FindCallback<Reminder>() {
            @Override
            public void done(List<Reminder> reminders, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("Detail Activity", "Issue with getting gardens", e);
                    return;
                }

                // for debugging purposes let's print every reminder title to LogCat
                for (Reminder reminder : reminders) {
                    Log.i("Reminder Query", "Reminder: " + reminder.getReminderTitle());
                }

                // save user's reminders to list and notify adapter of new data
                userReminders.clear();
                userReminders.addAll(reminders);

                adapter.notifyDataSetChanged();
            }
        });

    }

    public void queryPlantInBed(List<PlantInBed> plantsInTheBed, PlantInBedAdapter adapter, Garden garden){
        ParseQuery<PlantInBed> query = ParseQuery.getQuery(PlantInBed.class);

        query.whereEqualTo(PlantInBed.KEY_GARDEN, garden);

        query.addAscendingOrder("createdAt");

        // start an asynchronous call for PlantInBed objects
        query.findInBackground(new FindCallback<PlantInBed>() {
            @Override
            public void done(List<PlantInBed> plantsInBed, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("Detail Activity", "Issue with getting plants", e);
                    return;
                }

                // for debugging purposes let's print every PlantInBed name to LogCat
                for (PlantInBed plantInBed : plantsInBed) {
                    Log.i("plantinbed Query", "name: " + plantInBed.getThisPlantName());
                }

                // save garden's plantsInBed to list and notify adapter of new data
                plantsInTheBed.clear();
                plantsInTheBed.addAll(plantsInBed);

                adapter.notifyDataSetChanged();
            }
        });

    }

}
