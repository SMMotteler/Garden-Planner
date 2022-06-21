package com.example.garden_planner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.garden_planner.R;
import com.example.garden_planner.databinding.FragmentGardenFeedBinding;
import com.example.garden_planner.databinding.FragmentProfileBinding;

public class GardenFeedFragment extends Fragment {

    private FragmentGardenFeedBinding binding;

    public GardenFeedFragment()
    {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        binding = FragmentGardenFeedBinding.inflate(getLayoutInflater(), parent, false);

        View view = binding.getRoot();

        return view;    }

}
