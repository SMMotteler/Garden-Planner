package com.example.garden_planner;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        // ParseObject.registerSubclass(User.class);
        // ParseObject.registerSubclass(Post.class);
        // ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("RTH8Hw1oEOxBaGszQpER4K4kqxewblDq6OTy6Ocr")
                .clientKey("wdwY2fEjRj1NB1IQWU7QCa70L5jweNkkoKeLCn6I")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
