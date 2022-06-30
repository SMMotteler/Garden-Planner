package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.garden_planner.databinding.ActivityCreateGardenBinding;
import com.example.garden_planner.databinding.ActivityEditGardenBinding;
import com.example.garden_planner.models.Garden;

public class EditGardenActivity extends AppCompatActivity {
    private ActivityEditGardenBinding binding;
    private Button btAddPlant;
    private Button btDeletePlant;
    private Garden garden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditGardenBinding.inflate(getLayoutInflater());

        garden = savedInstanceState.getParcelable("garden");

        View view = binding.getRoot();
        setContentView(view);

        btAddPlant = binding.btAddPlant;
        btDeletePlant = binding.btDeletePlant;

        btAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditGardenActivity.this, PlantAdditionActivity.class);
                i.putExtra("garden", garden);
                startActivity(i);
            }
        });

        btDeletePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditGardenActivity.this, PlantDeletionActivity.class);
                i.putExtra("garden", garden);
                startActivity(i);
            }
        });
    }
}