package com.example.garden_planner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garden_planner.GardenMethodHelper;
import com.example.garden_planner.R;
import com.example.garden_planner.adapters.ReminderAdapter;
import com.example.garden_planner.databinding.FragmentProfileBinding;
import com.example.garden_planner.databinding.FragmentRemindersBinding;
import com.example.garden_planner.models.Reminder;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RemindersFragment extends Fragment {

    FragmentRemindersBinding binding;

    RecyclerView rvReminders;
    RadioGroup rdReminderOrder;
    RadioButton rbByTime;
    RadioButton rbByGarden;
    ReminderAdapter adapter;
    List<Reminder> userReminders;

    public RemindersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        binding = FragmentRemindersBinding.inflate(getLayoutInflater(), parent, false);

        View view = binding.getRoot();

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rvReminders = binding.rvReminders;
        rdReminderOrder = binding.rdReminderOrder;
        rbByGarden = binding.rbByGarden;
        rbByTime = binding.rbByTime;

        userReminders = new ArrayList<>();
        adapter = new ReminderAdapter(getContext(), userReminders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvReminders.setAdapter(adapter);
        rvReminders.setLayoutManager(linearLayoutManager);

        GardenMethodHelper.queryReminders(userReminders, adapter, Reminder.KEY_REMIND_WHO, ParseUser.getCurrentUser(), true);

        rbByTime.setChecked(true);

        rdReminderOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Is the button now checked?
                boolean checked = rbByTime.isChecked();

                // Check which radio button was clicked
                        if (checked) {
                            GardenMethodHelper.queryReminders(userReminders, adapter, Reminder.KEY_REMIND_WHO, ParseUser.getCurrentUser(), true);
                        }
                        else {
                            GardenMethodHelper.queryReminders(userReminders, adapter, Reminder.KEY_REMIND_WHO, ParseUser.getCurrentUser(), false);
                        }

                }
        });

    }
}