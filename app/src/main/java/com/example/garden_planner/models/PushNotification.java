package com.example.garden_planner.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("PushNotification")
public class PushNotification extends ParseObject {
    public static final String KEY_PUSH_DATE = "pushDate";
    public static final String KEY_REMINDER = "reminder";

    public Date getPushDate(){return getDate(KEY_PUSH_DATE);}

    public void setPushDate(Date pushDate){put(KEY_PUSH_DATE, pushDate);}

    public Reminder getReminder(){return (Reminder) getParseObject(KEY_REMINDER);}

    public void setReminder(Reminder reminder){put(KEY_REMINDER, reminder);}
}
