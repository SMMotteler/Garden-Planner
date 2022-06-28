package com.example.garden_planner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.garden_planner.adapters.GardenFeedAdapter;
import com.example.garden_planner.adapters.PlantInBedAdapter;
import com.example.garden_planner.adapters.ReminderAdapter;
import com.example.garden_planner.models.FrostDateClient;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.JsonReader;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.Reminder;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;

public class GardenMethodHelper {

    static FrostDateClient client = new FrostDateClient();

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

    public static void queryGarden(List<Garden> userGardens, GardenFeedAdapter adapter, ParseUser user){
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

    public static void queryReminders(List<Reminder> userReminders, ReminderAdapter adapter, String key, ParseObject object, Boolean byTime){
        ParseQuery<Reminder> query = ParseQuery.getQuery(Reminder.class);

        query.whereEqualTo(key, object);
        if(byTime){
            query.addAscendingOrder(Reminder.KEY_REMINDER_START);
        }
        else{
            query.addAscendingOrder(Reminder.KEY_REMIND_WHAT);
        }
        query.include(Reminder.KEY_REMINDER_MESSAGE);
        query.include(Reminder.KEY_REMINDER_TITLE);
        query.include(Reminder.KEY_REMINDER_START);
        query.include(Reminder.KEY_REMINDER_END);
        query.include(Reminder.KEY_REMIND_WHAT);
        query.include(Reminder.KEY_REMIND_WHICH_PLANT);
        query.include("RemindWhichPlant.PlantType");

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

    public static void queryPlantInBed(List<PlantInBed> plantsInTheBed, PlantInBedAdapter adapter, Garden garden){
        ParseQuery<PlantInBed> query = ParseQuery.getQuery(PlantInBed.class);

        query.whereEqualTo(PlantInBed.KEY_GARDEN, garden);

        query.addAscendingOrder("createdAt");
        query.include(PlantInBed.KEY_TYPE);

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

    // information for this method is from https://github.com/waldoj/frostline
    public static void initializeGardenInformation(double latitude, double longitude) throws JSONException, IOException {
        client.getStations(latitude, longitude, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // the API for the stations list them in order of increasing distance, so we can always
                // take the first JSONObject
                try {
                    JSONObject station = json.jsonArray.getJSONObject(0);
                    client.getFrostDate(station.getString("id"), 1, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            // the API for the frost dates has the 32 deg. threshold as the second entry in
                            // the array every time, which we are using as the temperature where no more frost
                            // will occur
                            try {
                                JSONObject lastFrostDateInfo = json.jsonArray.getJSONObject(1);
                                String lastFrostDateDay = lastFrostDateInfo.getString("prob_50");
                                // return the last frost date
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("initializeGardenInformation", "error with getting frost dates", throwable);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("initializeGardenInformation", "error with getting stations", throwable);
            }
        });
    }

}

