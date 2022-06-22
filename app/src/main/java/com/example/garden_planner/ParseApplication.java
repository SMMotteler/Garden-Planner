package com.example.garden_planner;

import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.Reminder;
import com.example.garden_planner.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Garden.class);
        ParseObject.registerSubclass(Reminder.class);
        ParseObject.registerSubclass(Plant.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(PlantInBed.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("RTH8Hw1oEOxBaGszQpER4K4kqxewblDq6OTy6Ocr")
                .clientKey("wdwY2fEjRj1NB1IQWU7QCa70L5jweNkkoKeLCn6I")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
