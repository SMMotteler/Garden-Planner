package com.example.garden_planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.garden_planner.databinding.ActivityEditGardenBinding;
import com.example.garden_planner.models.Garden;

public class EditGardenActivity extends AppCompatActivity {
    private ActivityEditGardenBinding binding;
    private Button btAddPlant;
    private Button btDeletePlant;
    private Garden garden;
    public static final int ACTIVITY_RESULT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditGardenBinding.inflate(getLayoutInflater());

        garden = getIntent().getParcelableExtra("garden");

        View view = binding.getRoot();
        setContentView(view);

        btAddPlant = binding.btAddPlant;
        btDeletePlant = binding.btDeletePlant;

        btAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditGardenActivity.this, PlantAdditionActivity.class);
                i.putExtra("garden", garden);
                startActivityForResult(i, ACTIVITY_RESULT);
            }
        });

        btDeletePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditGardenActivity.this, PlantDeletionActivity.class);
                i.putExtra("garden", garden);
                startActivityForResult(i, ACTIVITY_RESULT);

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}