package com.example.garden_planner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.GardenMethodHelper;
import com.example.garden_planner.MainActivity;
import com.example.garden_planner.PlantAdditionActivity;
import com.example.garden_planner.databinding.ItemReminderBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.Reminder;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    public static final String TAG = "ReminderAdapter";
    private Context context;
    private List<Reminder> reminders;
    private RecyclerView recyclerView;
    private ParseObject whatQueryBy;
    private String key;

    ItemReminderBinding binding;

    public ReminderAdapter(Context context, List<Reminder> reminders, RecyclerView recyclerView, String key, ParseObject whatQueryBy) {
        Log.i(TAG, "making ReminderAdapter");
        this.context = context;
        this.reminders = reminders;
        this.recyclerView = recyclerView;
        this.key = key;
        this.whatQueryBy = whatQueryBy;
    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");

        binding = ItemReminderBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
        Reminder reminder = reminders.get(position);
        Log.i(TAG, "reminder " + reminder.getReminderTitle());
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvGardenName;
        private TextView tvToDoDate;
        private ImageView ivPlantPic;
        private TextView tvPlantName;
        private TextView tvReminderTitle;
        private TextView tvReminderText;
        private Button completeReminderButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Reminder reminder = reminders.get(position);
                Garden garden = reminder.getRemindWhat();
                MainActivity activity = (MainActivity) context;
                activity.goToDetailGardenView(garden);
            }
        }

        public void bind(Reminder reminder) {
            Log.i(TAG, "binding " + reminder.getReminderTitle());
            tvGardenName = binding.tvGardenName;
            tvToDoDate = binding.tvToDoDate;
            ivPlantPic = binding.ivPlantPic;
            tvPlantName = binding.tvPlantName;
            tvReminderTitle = binding.tvReminderTitle;
            tvReminderText = binding.tvReminderText;
            completeReminderButton = binding.completeReminderButton;

            Log.i(TAG, "binding " + reminder.getReminderTitle());

            tvGardenName.setText(reminder.getRemindWhat().getName());

            if (reminder.getReminderStart().equals(reminder.getReminderEnd())) {
                tvToDoDate.setText(reminder.getReminderStart().toString());
            } else {
                tvToDoDate.setText(reminder.getReminderStart().toString() + " to " + reminder.getReminderEnd().toString());
            }

            // Note - I know this looks like it should be able to be condensed to one if statement, but
            // if a reminder doesn't have one of the first two keys, then it will throw an error when it checks
            // the following key if it was in a single if statement - this setup prevents any crashes
            if (reminder.has(Reminder.KEY_REMIND_WHICH_PLANT)) {
                if (reminder.getRemindWhichPlant().has(PlantInBed.KEY_TYPE)) {
                    if (reminder.getRemindWhichPlant().getPlantType().has(Plant.KEY_PHOTO)) {
                        Glide.with(context).load(reminder.getRemindWhichPlant().getPlantType().getPhoto().getUrl()).into(ivPlantPic);
                    }
                }
            }

            tvPlantName.setText(reminder.getRemindWhichPlant().getDisplayName());

            tvReminderTitle.setText(reminder.getReminderTitle());

            tvReminderText.setText(reminder.getReminderMessage());

            completeReminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // create a harvest reminder if the reminder completed is a planting reminder
                    if (reminder.getReminderType().equals("plant")) {
                        PlantInBed plantInBed = reminder.getRemindWhichPlant();
                        plantInBed.setPlantDate(new Date());
                        plantInBed.setShouldHarvestDate(GardenMethodHelper.convertToDate(
                                GardenMethodHelper.convertToLocalDateViaInstant(
                                                plantInBed.getPlantDate())
                                        .plusWeeks(plantInBed.getPlantType().getHarvestTime())));

                        Reminder harvestReminder = new Reminder();
                        Date startHarvestWeek = plantInBed.getShouldHarvestDate();
                        Date endHarvestWeek = GardenMethodHelper.convertToDate(
                                GardenMethodHelper.convertToLocalDateViaInstant(
                                        startHarvestWeek).plusWeeks(1));
                        String harvestTitle = "Harvest " + plantInBed.getDisplayName();
                        String harvestMessage =plantInBed.getPlantType().getHarvestAdvice();

                        harvestReminder.initializeReminder(startHarvestWeek, endHarvestWeek, harvestTitle,
                                harvestMessage, plantInBed.getGarden(), plantInBed, "harvest");
                        // reminders.add(harvestReminder);

                        try {
                            harvestReminder.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    // delete the completed reminder
                    try {
                        reminder.delete();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // reminders.remove(reminder);
                    reminders = new ArrayList<>();
                    ReminderAdapter newAdapter = new ReminderAdapter(context, reminders, recyclerView, key, whatQueryBy);
                    recyclerView.setAdapter(newAdapter);
                    GardenMethodHelper.queryReminders(reminders, newAdapter, key, whatQueryBy);
                    // notifyDataSetChanged();
                    Toast.makeText(context, "reminder completed!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
