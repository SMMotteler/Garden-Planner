package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.garden_planner.databinding.ActivityCreateGardenBinding;
import com.example.garden_planner.databinding.ActivityEditGardenBinding;

public class EditGardenActivity extends AppCompatActivity {
    private ActivityEditGardenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditGardenBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
    }
}