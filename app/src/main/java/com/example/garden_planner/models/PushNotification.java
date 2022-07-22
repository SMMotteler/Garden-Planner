package com.example.garden_planner.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("PushNotification")
public class PushNotification extends ParseObject {
    public static final String KEY_PUSH_DATE = "pushDate";
    public static final String KEY_REMINDER = "reminder";
    public static final String KEY_TITLE = "pushTitle";
    public static final String KEY_USER = "pushTo";

    public Date getPushDate(){return getDate(KEY_PUSH_DATE);}

    public void setPushDate(Date pushDate){put(KEY_PUSH_DATE, pushDate);}

    public Reminder getReminder(){return (Reminder) getParseObject(KEY_REMINDER);}

    public void setReminder(Reminder reminder){put(KEY_REMINDER, reminder);}

    public String getPushTitle(){return getString(KEY_TITLE);}

    public void setPushTitle(String pushTitle){put(KEY_TITLE, pushTitle);}

    public ParseUser getPushToUser(){return getParseUser(KEY_USER);}

    public void setPushToUser(Reminder reminder){put(KEY_USER, reminder.getRemindWho());}
}
