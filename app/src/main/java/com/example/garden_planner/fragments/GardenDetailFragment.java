package com.example.garden_planner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.EditGardenActivity;
import com.example.garden_planner.GardenMethodHelper;
import com.example.garden_planner.adapters.PlantInBedAdapter;
import com.example.garden_planner.adapters.ReminderAdapter;
import com.example.garden_planner.databinding.FragmentGardenDetailBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.Reminder;

import java.util.ArrayList;
import java.util.List;

public class GardenDetailFragment extends Fragment {
    private FragmentGardenDetailBinding binding;

    public Garden garden;
    private PlantInBedAdapter plantInBedAdapter;
    private List<PlantInBed> somePlants;
    ReminderAdapter reminderAdapter;
    List<Reminder> userReminders;

    private TextView tvGardenName;
    private ImageView ivGardenImage;
    private RecyclerView rvPlants;
    private TextView tvGardenLocation;
    private TextView tvReminderTitle;
    private RecyclerView rvReminders;
    private Button btEditGarden;

    public GardenDetailFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        binding = FragmentGardenDetailBinding.inflate(getLayoutInflater(), parent, false);

        View view = binding.getRoot();

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvGardenName = binding.tvGardenName;
        ivGardenImage = binding.ivGardenImage;
        rvPlants = binding.rvPlants;
        tvGardenLocation = binding.tvGardenLocation;
        btEditGarden = binding.btEditGarden;

        tvGardenName.setText(garden.getName());
        tvGardenLocation.setText("Location: "+garden.getLocation());
        if(garden.has("lastFrostDate")){
            tvGardenLocation.setText("Location: "+garden.getLocation()+"\nFrost date: "+garden.getLastFrostDate());
        }

        if (garden.has("photo")){
            Glide.with(getContext()).load(garden.getParseFile("photo").getUrl()).into(ivGardenImage);
        }

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        somePlants = new ArrayList<>();
        plantInBedAdapter = new PlantInBedAdapter(getContext(), somePlants);

        rvPlants.setAdapter(plantInBedAdapter);
        rvPlants.setLayoutManager(horizontalLayoutManager);

        GardenMethodHelper.queryPlantInBed(somePlants, plantInBedAdapter, garden);

        tvReminderTitle = binding.tvReminderTitle;

        rvReminders = binding.rvReminders;

        userReminders = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(getContext(), userReminders, rvReminders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvReminders.setAdapter(reminderAdapter);
        rvReminders.setLayoutManager(linearLayoutManager);

        GardenMethodHelper.queryReminders(userReminders, reminderAdapter, Reminder.KEY_REMIND_WHAT, garden);

        btEditGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), EditGardenActivity.class);
                i.putExtra("garden", garden);
                getContext().startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        GardenMethodHelper.queryPlantInBed(somePlants, plantInBedAdapter, garden);
        GardenMethodHelper.queryReminders(userReminders, reminderAdapter, Reminder.KEY_REMIND_WHAT, garden);

    }
}