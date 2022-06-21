package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.garden_planner.databinding.ActivityEditGardenBinding;

public class EditGardenActivity extends AppCompatActivity {
    private ActivityEditGardenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_garden);
    }
}