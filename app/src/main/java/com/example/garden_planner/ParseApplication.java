package com.example.garden_planner;

import android.app.Application;

import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.PushNotification;
import com.example.garden_planner.models.Reminder;
import com.example.garden_planner.models.User;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;

import java.util.ArrayList;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Garden.class);
        ParseObject.registerSubclass(Reminder.class);
        ParseObject.registerSubclass(Plant.class);
        ParseObject.registerSubclass(PushNotification.class);
        ParseObject.registerSubclass(PlantInBed.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("RTH8Hw1oEOxBaGszQpER4K4kqxewblDq6OTy6Ocr")
                .clientKey("wdwY2fEjRj1NB1IQWU7QCa70L5jweNkkoKeLCn6I")
                .server("https://parseapi.back4app.com")
                .build()
        );

        ArrayList<String> channels = new ArrayList<>();
        channels.add("News");
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.put("GCMSenderId", BuildConfig.GCM_SENDER_ID);
        installation.put("channels", channels);
        installation.saveInBackground();

        ParsePush.subscribeInBackground("News");
    }
}
