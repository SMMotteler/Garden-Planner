package com.example.garden_planner.models;

import android.util.Log;

import com.example.garden_planner.GardenMethodHelper;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ParseClassName("Reminder")
public class Reminder extends ParseObject {
    public static final String KEY_REMINDER_START = "ReminderStart";
    public static final String KEY_REMINDER_END = "ReminderEnd";
    public static final String KEY_REMINDER_MESSAGE = "ReminderMessage";
    public static final String KEY_REMIND_WHAT = "remindWhat";
    public static final String KEY_REMIND_WHO = "RemindWho";
    public static final String KEY_REMIND_WHICH_PLANT = "RemindWhichPlant";
    public static final String KEY_REMINDER_TITLE = "ReminderTitle";
    public static final String KEY_TYPE = "reminderType";

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

    public String getReminderType(){return getString(KEY_TYPE);}

    public void setReminderType(String reminderType){put(KEY_TYPE, reminderType);}

    public void initializeReminder(Date reminderStart, Date reminderEnd, String reminderTitle,
                                   String reminderMessage, Garden reminderGarden, PlantInBed reminderWhichPlant,
                                   String reminderType){
        this.setReminderStart(reminderStart);
        this.setReminderEnd(reminderEnd);
        this.setReminderTitle(reminderTitle);
        this.setReminderMessage(reminderMessage);
        this.setRemindWhat(reminderGarden);
        this.setRemindWhichPlant(reminderWhichPlant);
        this.setRemindWho(ParseUser.getCurrentUser());
        this.setReminderType(reminderType);

        long millisecondsBetween = reminderEnd.getTime() - reminderStart.getTime();

        // this should always be equal to 7, but it's better to calculate to make sure
        long dates = TimeUnit.DAYS.convert(millisecondsBetween, TimeUnit.MILLISECONDS);

        for(long day =0; day<=dates; day++){
            PushNotification pushNotification = new PushNotification();
            Date pushDate = GardenMethodHelper.convertToDate(
                    GardenMethodHelper.convertToLocalDateViaInstant(reminderStart).plusDays(day));
            pushNotification.setPushDate(pushDate);

            pushNotification.setReminder(this);

            pushNotification.setPushTitle(this.getReminderTitle());

            pushNotification.setPushToUser(this);

            pushNotification.saveInBackground();
        }


    }

    public void deletePushes() {
        ParseQuery<PushNotification> query = ParseQuery.getQuery(PushNotification.class);
        query.whereEqualTo(PushNotification.KEY_REMINDER, this);
        query.findInBackground(new FindCallback<PushNotification>() {
            @Override
            public void done(List<PushNotification> pushNotifications, ParseException e) {
                if (e != null) {
                    Log.e("Detail Activity", "Issue with getting pushes", e);
                    return;
                }
                for (PushNotification notification : pushNotifications){
                    try {
                        notification.delete();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
    }
}
