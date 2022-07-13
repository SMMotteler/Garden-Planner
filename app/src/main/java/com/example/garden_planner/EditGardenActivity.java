package com.example.garden_planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garden_planner.adapters.PlantAdditionAdapter;
import com.example.garden_planner.adapters.PlantDeletionAdapter;
import com.example.garden_planner.databinding.ActivityEditGardenBinding;
import com.example.garden_planner.databinding.ActivityEditGardenNewBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;

import java.util.ArrayList;
import java.util.List;

public class EditGardenActivity extends AppCompatActivity {
    private ActivityEditGardenNewBinding binding;
    private Button btSaveGarden;
    private RecyclerView rvAddPlants;
    private RecyclerView rvCurrentPlants;
    private Garden garden;
    private TextView tvGardenName;
    private PlantAdditionAdapter additionAdapter;
    private List<Plant> plantTypes;
    private ArrayList<PlantInBed> plants;
    private PlantDeletionAdapter deletionAdapter;

    public static final int ACTIVITY_RESULT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditGardenNewBinding.inflate(getLayoutInflater());

        garden = getIntent().getParcelableExtra("garden");

        View view = binding.getRoot();
        setContentView(view);

        btSaveGarden = binding.btSaveGarden;
        rvAddPlants = binding.rvAddPlants;
        tvGardenName = binding.tvGardenName;
        rvCurrentPlants = binding.rvCurrentPlants;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        finish();
        // TODO: figure out what to do with the save button/edit activity
    }
}