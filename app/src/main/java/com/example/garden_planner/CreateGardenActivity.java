package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.garden_planner.databinding.ActivityCreateGardenBinding;

public class CreateGardenActivity extends AppCompatActivity {
    private ActivityCreateGardenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGardenBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
    }
}