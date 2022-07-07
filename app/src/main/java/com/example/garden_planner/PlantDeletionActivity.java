package com.example.garden_planner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garden_planner.adapters.PlantDeletionAdapter;
import com.example.garden_planner.adapters.ReminderAdapter;
import com.example.garden_planner.databinding.ActivityPlantDeletionBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.Reminder;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class PlantDeletionActivity extends AppCompatActivity {

    ActivityPlantDeletionBinding binding;
    RecyclerView rvPlants;
    private ArrayList<PlantInBed> plants;
    private Garden garden;
    private PlantDeletionAdapter adapter;
    private Button btSetPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plants = new ArrayList<>();

        binding = ActivityPlantDeletionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        rvPlants = binding.rvPlants;
        btSetPlants = binding.btSetPlants;

        garden = getIntent().getParcelableExtra("garden");

        adapter = new PlantDeletionAdapter(this, plants);
        rvPlants.setLayoutManager(new LinearLayoutManager(this));
        rvPlants.setAdapter(adapter);
        GardenMethodHelper.queryPlantInBed(plants, adapter, garden);


        btSetPlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<PlantInBed> plantsToDelete = adapter.getPlantsToDelete();
                for (PlantInBed plant : plantsToDelete){
                    deleteReminders(plant);
                    plant.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            finish();
                        }
                    });
                }

            }
        });
    }

    public void deleteReminders(PlantInBed plant){
        ParseQuery<Reminder> query = ParseQuery.getQuery(Reminder.class);
        query.whereEqualTo(Reminder.KEY_REMIND_WHICH_PLANT, plant);
        query.addAscendingOrder(Reminder.KEY_REMINDER_START);
        query.include(Reminder.KEY_REMIND_WHICH_PLANT);

        // start an asynchronous call for reminders
        query.findInBackground(new FindCallback<Reminder>() {
            @Override
            public void done(List<Reminder> reminders, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("Detail Activity", "Issue with getting gardens", e);
                    return;
                }

                // for debugging purposes let's print every reminder title to LogCat, then delete the reminder
                for (Reminder reminder : reminders) {
                    Log.i("Reminder Query", "Reminder: " + reminder.getReminderTitle());
                    try {
                        reminder.delete();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

    }
}