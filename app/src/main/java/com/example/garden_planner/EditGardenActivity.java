package com.example.garden_planner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.garden_planner.adapters.PlantAdditionAdapter;
import com.example.garden_planner.adapters.PlantDeletionAdapter;
import com.example.garden_planner.databinding.ActivityEditGardenNewBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.PushNotification;
import com.example.garden_planner.models.Reminder;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditGardenActivity extends AppCompatActivity {
    private ActivityEditGardenNewBinding binding;
    private Garden garden;
    private ArrayList<PlantInBed> plants;
    private PlantDeletionAdapter deletionAdapter;
    private PlantAdditionAdapter adapter;

    private Button btSaveGarden;
    private RecyclerView rvCurrentPlants;
    private TextView tvGardenName;
    private TextView tvAddAPlant;
    private EditText etPlantType;
    private Button btSearch;
    private ExpandableLayout elPlantInfo;
    private ImageView ivPlantPic;
    private EditText etNewPlantName;
    private TextView tvPlantType;
    private Button btSaveThisPlant;

    public static final int ACTIVITY_RESULT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditGardenNewBinding.inflate(getLayoutInflater());

        garden = getIntent().getParcelableExtra("garden");

        View view = binding.getRoot();
        setContentView(view);

        btSaveGarden = binding.btSaveGarden;
        btSearch = binding.btSearch;
        btSaveThisPlant = binding.btSaveThisPlant;
        tvAddAPlant = binding.tvAddAPlant;
        tvPlantType = binding.tvPlantType;
        tvGardenName = binding.tvGardenName;
        rvCurrentPlants = binding.rvCurrentPlants;
        etNewPlantName = binding.etNewPlantName;
        etPlantType = binding.etPlantType;
        elPlantInfo = binding.elPlantInfo;
        ivPlantPic = binding.ivPlantPic;


        plants = new ArrayList<>();
        deletionAdapter = new PlantDeletionAdapter(this, plants);
        GardenMethodHelper.queryPlantInBed(plants, deletionAdapter, garden);
        rvCurrentPlants.setLayoutManager(new LinearLayoutManager(this));
        rvCurrentPlants.setAdapter(deletionAdapter);
        tvGardenName.setText("Current plants in "+garden.getName()+":");
        tvAddAPlant.setText("Add another plant to "+garden.getName()+":");

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plantName = etPlantType.getText().toString();

                if (plantName.isEmpty()){
                    Toast.makeText(EditGardenActivity.this, "Please enter something into the text box!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("Edit Garden", plantName);

                ParseQuery<Plant> query = ParseQuery.getQuery(Plant.class);
                query.whereEqualTo(Plant.KEY_NAME, plantName);
                query.addAscendingOrder("createdAt");

                try {
                    Plant selectedPlant = query.getFirst();
                    if (selectedPlant.equals(null)){
                        Toast.makeText(EditGardenActivity.this, "No results for your query :(", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Glide.with(EditGardenActivity.this).load(selectedPlant.getPhoto().getUrl()).into(ivPlantPic);
                    String plantType = selectedPlant.getName().substring(0,1).toUpperCase()
                            + selectedPlant.getName().substring(1) +" Plant";
                    tvPlantType.setText("New "+plantType);

                    elPlantInfo.expand();

                    PlantInBed newPlant = new PlantInBed();
                    newPlant.setPlantType(selectedPlant);
                    newPlant.setGarden(garden);
                    newPlant.setShouldPlantDate(GardenMethodHelper.convertToDate(
                            GardenMethodHelper.convertToLocalDateViaInstant(garden.getLastFrostDate())
                                    .plusWeeks(selectedPlant.getPlantTime())));

                    btSaveThisPlant.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newPlantName = etNewPlantName.getText().toString();
                            if (newPlantName.isEmpty()) {
                                Toast.makeText(EditGardenActivity.this, "Enter a name for your new plant!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            newPlant.setDisplayName(newPlantName);
                            newPlant.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e != null){
                                        Log.e("yikes", e.getMessage());
                                        Toast.makeText(EditGardenActivity.this, "Error in making the planting reminder!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    elPlantInfo.collapse();
                                    etNewPlantName.setText("");

                                    plants = new ArrayList<>();
                                    deletionAdapter = new PlantDeletionAdapter(EditGardenActivity.this, plants);
                                    GardenMethodHelper.queryPlantInBed(plants, deletionAdapter, garden);
                                    rvCurrentPlants.setLayoutManager(new LinearLayoutManager(EditGardenActivity.this));
                                    rvCurrentPlants.setAdapter(deletionAdapter);

                                }
                            });

                            // create a planting reminder for plantInBed
                            Reminder plantReminder = new Reminder();
                            Date startPlantWeek = newPlant.getShouldPlantDate();
                            Date endPlantWeek = GardenMethodHelper.convertToDate(
                                    GardenMethodHelper.convertToLocalDateViaInstant(
                                            startPlantWeek).plusWeeks(1));
                            String reminderTitle = "Plant "+newPlantName;

                            plantReminder.initializeReminder(startPlantWeek, endPlantWeek, reminderTitle,
                                    selectedPlant.getPlantAdvice(), garden, newPlant, "plant");

                            plantReminder.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e != null){
                                        Log.e("yikes", e.getMessage());
                                        Toast.makeText(EditGardenActivity.this, "Error in making the planting reminder!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                }
                            });
                        }
                    });

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // add search code
            }
        });

        btSaveGarden.setOnClickListener(new View.OnClickListener() {
            // display loading gifs as tasks finish
            @Override
            public void onClick(View v) {
                List<PlantInBed> plantsToDelete = deletionAdapter.getPlantsToDelete();
                for (PlantInBed plant : plantsToDelete){
                    try {
                        deleteReminders(plant);
                        plant.delete();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                garden.saveInBackground(new SaveCallback() {
                    // while loading, display the loading gif

                    @Override
                    public void done(ParseException e) {
                        finish();
                    }
                });
            }
        });

    }

    //@Override
    //public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    //    super.onActivityResult(requestCode, resultCode, data);
   //     finish();
        // TODO: figure out what to do with the save button/edit activity
   // }

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
                    Log.e("Edit Garden", "Issue with getting reminders", e);
                    return;
                }

                // for debugging purposes let's print every reminder title to LogCat, then delete the reminder
                for (Reminder reminder : reminders) {
                    reminder.deletePushes();
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