package com.example.garden_planner.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Reminder")
public class Reminder extends ParseObject {
    public static final String KEY_REMINDER_START = "ReminderStart";
    public static final String KEY_REMINDER_END = "ReminderEnd";
    public static final String KEY_REMINDER_MESSAGE = "ReminderMessage";
    public static final String KEY_REMIND_WHAT = "RemindWhat";
    public static final String KEY_REMIND_WHO = "RemindWho";
    public static final String KEY_REMIND_WHICH_PLANT = "RemindWhichPlant";
    public static final String KEY_REMINDER_TITLE = "ReminderTitle";

    public Date getReminderStart(){return getDate(KEY_REMINDER_START);}

    public void setReminderStart(Date reminderStart){put(KEY_REMINDER_START, reminderStart);}

    public Date getReminderEnd(){return getDate(KEY_REMINDER_END);}

    public void setReminderEnd(Date reminderEnd){put(KEY_REMINDER_END, reminderEnd);}

    public String getReminderMessage(){return getString(KEY_REMINDER_MESSAGE);}

    public void setReminderMessage(String reminderMessage){put(KEY_REMINDER_MESSAGE, reminderMessage);}

    public Garden getRemindWhat(){return (Garden)get(KEY_REMIND_WHAT);}

    public void setRemindWhat(Garden remindWhat){put(KEY_REMIND_WHAT, remindWhat);}

    public ParseUser getRemindWho(){return getParseUser(KEY_REMIND_WHO);}

    public void setRemindWho(ParseUser remindWho){put(KEY_REMIND_WHO, remindWho);}

    public PlantInBed getRemindWhichPlant(){return (PlantInBed) get(KEY_REMIND_WHICH_PLANT);}

    public void setRemindWhichPlant(PlantInBed remindWhichPlant){put(KEY_REMIND_WHICH_PLANT, remindWhichPlant);}

    public String getReminderTitle(){return getString(KEY_REMINDER_TITLE);}

    public void setReminderTitle(String reminderTitle){put(KEY_REMINDER_TITLE, reminderTitle);}

}
