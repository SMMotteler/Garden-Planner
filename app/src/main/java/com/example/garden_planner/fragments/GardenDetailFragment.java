package com.example.garden_planner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.garden_planner.R;
import com.example.garden_planner.databinding.FragmentGardenDetailBinding;

public class GardenDetailFragment extends Fragment {
    private FragmentGardenDetailBinding binding;

    public GardenDetailFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_garden_detail, parent, false);
    }
}
