package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.garden_planner.adapters.PlantDeletionAdapter;
import com.example.garden_planner.databinding.ActivityPlantAdditionBinding;
import com.example.garden_planner.databinding.ActivityPlantDeletionBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.PlantInBed;
import com.parse.DeleteCallback;
import com.parse.ParseException;

import java.lang.reflect.Array;
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
            public void onClick(View v) {
                List<PlantInBed> plantsToDelete = adapter.getPlantsToDelete();
                for (PlantInBed plant : plantsToDelete){
                    plant.deleteInBackground();
                }
                finish();
            }
        });
    }
}