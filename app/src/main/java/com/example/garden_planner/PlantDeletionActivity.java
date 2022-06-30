package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.garden_planner.databinding.ActivityPlantAdditionBinding;
import com.example.garden_planner.databinding.ActivityPlantDeletionBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.PlantInBed;

import java.util.ArrayList;

public class PlantDeletionActivity extends AppCompatActivity {

    ActivityPlantDeletionBinding binding;
    RecyclerView rvPlants;
    private ArrayList<PlantInBed> plants;
    private Garden garden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_deletion);

        binding = ActivityPlantDeletionBinding.inflate(getLayoutInflater());

        rvPlants = binding.rvPlants;

        garden = getIntent().getParcelableExtra("garden");

    }
}