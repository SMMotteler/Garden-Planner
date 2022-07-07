package com.example.garden_planner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garden_planner.adapters.PlantDeletionAdapter;
import com.example.garden_planner.databinding.ActivityPlantDeletionBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.Reminder;
import com.parse.DeleteCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

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
                    List<Reminder> plantReminders = new ArrayList<>();
                    // get the list of reminders that pertains to deletedPlant
                    GardenMethodHelper.queryReminders(plantReminders, null, Reminder.KEY_REMIND_WHICH_PLANT, plant, true);
                    Log.i("reminders", plantReminders.toString());
                    for (Reminder reminder : plantReminders){
                        Log.i("deleteReminders", reminder.getReminderTitle()+"is being deleted");
                        try {
                            reminder.delete();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.i("deleteReminders", "deleted");
                    }
                    plant.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {

                        }
                    });
                }

            }
        });
    }

}