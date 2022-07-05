package com.example.garden_planner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garden_planner.adapters.PlantAdditionAdapter;
import com.example.garden_planner.databinding.ActivityPlantAdditionBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PlantAdditionActivity extends AppCompatActivity {

    private ActivityPlantAdditionBinding binding;

    private Garden garden;
    private Button btCreatePlant;
    private EditText etPlantName;
    private RecyclerView rvPlants;
    private PlantAdditionAdapter adapter;
    private List<Plant> plants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantAdditionBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        garden = getIntent().getParcelableExtra("garden");

        btCreatePlant = binding.btCreatePlant;
        etPlantName = binding.etPlantName;
        rvPlants = binding.rvPlants;

        plants = new ArrayList<>();

        adapter = new PlantAdditionAdapter(this, plants);

        rvPlants.setLayoutManager(new LinearLayoutManager(this));
        rvPlants.setAdapter(adapter);

        queryPlant(plants);

        btCreatePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Plant plantType = adapter.getPlantType();
                String name = etPlantName.getText().toString();

                if (plantType == null || name.length() < 1){
                    Toast.makeText(PlantAdditionActivity.this, "Missing a field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                PlantInBed plantInBed = new PlantInBed();
                plantInBed.setPlantType(plantType);
                plantInBed.setDisplayName(name);
                plantInBed.setGarden(garden);

                plantInBed.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            Log.e("yikes", e.getMessage());
                            Toast.makeText(PlantAdditionActivity.this, "Error in making the plant!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        finish();
                    }
                });
            }
        });
    }


    public void queryPlant(List<Plant> plantsNow){
        ParseQuery<Plant> query = ParseQuery.getQuery(Plant.class);

        query.addAscendingOrder("createdAt");

        // start an asynchronous call for PlantInBed objects
        query.findInBackground(new FindCallback<Plant>() {
            @Override
            public void done(List<Plant> plants, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("Detail Activity", "Issue with getting plants", e);
                    return;
                }

                // for debugging purposes let's print every PlantInBed name to LogCat
                for (Plant plant : plants) {
                    Log.i("plantinbed Query", "name: " + plant.getName());
                }

                // save garden's plantsInBed to list and notify adapter of new data
                plantsNow.clear();
                plantsNow.addAll(plants);

                adapter.notifyDataSetChanged();
            }
        });

    }
}