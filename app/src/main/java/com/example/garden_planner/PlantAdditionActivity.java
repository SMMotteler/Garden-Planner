package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.garden_planner.databinding.ActivityMainBinding;
import com.example.garden_planner.databinding.ActivityPlantAdditionBinding;
import com.example.garden_planner.models.Garden;

public class PlantAdditionActivity extends AppCompatActivity {

    private ActivityPlantAdditionBinding binding;

    private Garden garden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantAdditionBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        garden = savedInstanceState.getParcelable("garden");
    }
}