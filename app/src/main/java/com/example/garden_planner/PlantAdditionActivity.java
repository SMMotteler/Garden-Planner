package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.garden_planner.databinding.ActivityPlantAdditionBinding;

public class PlantAdditionActivity extends AppCompatActivity {

    private ActivityPlantAdditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_addition);
    }
}