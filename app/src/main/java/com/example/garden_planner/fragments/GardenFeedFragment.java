package com.example.garden_planner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garden_planner.CreateGardenActivity;
import com.example.garden_planner.GardenMethodHelper;
import com.example.garden_planner.MainActivity;
import com.example.garden_planner.R;
import com.example.garden_planner.adapters.GardenFeedAdapter;
import com.example.garden_planner.adapters.ReminderAdapter;
import com.example.garden_planner.databinding.FragmentGardenFeedBinding;
import com.example.garden_planner.databinding.FragmentProfileBinding;
import com.example.garden_planner.models.Garden;
import com.parse.ParseUser;

import java.util.ArrayList;

public class GardenFeedFragment extends Fragment {

    private FragmentGardenFeedBinding binding;

    RecyclerView rvGardens;
    ArrayList<Garden> userGardens;
    GardenFeedAdapter adapter;
    Button btNewGarden;

    public GardenFeedFragment()
    {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        binding = FragmentGardenFeedBinding.inflate(getLayoutInflater(), parent, false);

        View view = binding.getRoot();

        return view;    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rvGardens = binding.rvGardens;
        btNewGarden = binding.btNewGarden;

        userGardens = new ArrayList<>();
        adapter = new GardenFeedAdapter(getContext(), userGardens);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvGardens.setAdapter(adapter);
        rvGardens.setLayoutManager(linearLayoutManager);

        GardenMethodHelper.queryGarden(userGardens, adapter, ParseUser.getCurrentUser());

        btNewGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateGardenActivity.class);
                getContext().startActivity(i);
            }
        });
    }

}
