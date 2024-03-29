package com.main.garden_planner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.main.garden_planner.EditGardenActivity;
import com.main.garden_planner.GardenMethodHelper;
import com.main.garden_planner.ImageActivity;
import com.main.garden_planner.MainActivity;
import com.main.garden_planner.PictureHandlerActivity;
import com.main.garden_planner.adapters.PlantInBedAdapter;
import com.main.garden_planner.adapters.ReminderAdapter;
import com.example.garden_planner.databinding.FragmentGardenDetailBinding;
import com.main.garden_planner.models.Garden;
import com.main.garden_planner.models.PlantInBed;
import com.main.garden_planner.models.Reminder;

import java.util.ArrayList;
import java.util.List;

public class GardenDetailFragment extends Fragment {
    private FragmentGardenDetailBinding binding;
    public static final int MODAL_DISAPPEAR = 5;

    public Garden garden;
    private PlantInBedAdapter plantInBedAdapter;
    private List<PlantInBed> somePlants;
    ReminderAdapter reminderAdapter;
    List<Reminder> userReminders;

    private TextView tvGardenName;
    public static ImageView ivGardenImage;
    private RecyclerView rvPlants;
    private TextView tvGardenLocation;
    private RecyclerView rvReminders;
    private Button btEditGarden;
    private Button changeGardenPhotoButton;
    public static View modalGreyLayer;
    public static final int REQUEST_CODE = 1;
    public final static int RESULT_OK = -1;

    public GardenDetailFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
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
        changeGardenPhotoButton = binding.changeGardenPhotoButton;
        modalGreyLayer = binding.modalGreyLayer;

        hideGrey();
        tvGardenName.setText(garden.getName());
        tvGardenLocation.setText("Location: " + garden.getLocation());
        if (garden.has("lastFrostDate")) {
            tvGardenLocation.setText("Location: " + garden.getLocation() + "\nFrost date: " + garden.getLastFrostDate());
        }

        if (garden.has("photo")) {
            Glide.with(getContext()).load(garden.getPhoto().getUrl()).into(ivGardenImage);
        }

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        somePlants = new ArrayList<>();
        plantInBedAdapter = new PlantInBedAdapter(getContext(), somePlants);

        rvPlants.setAdapter(plantInBedAdapter);
        rvPlants.setLayoutManager(horizontalLayoutManager);

        GardenMethodHelper.queryPlantInBed(somePlants, plantInBedAdapter, garden);

        rvReminders = binding.rvReminders;

        userReminders = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(getContext(), userReminders, rvReminders, Reminder.KEY_REMIND_WHAT, garden);

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

        changeGardenPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getContext();
                Intent i = new Intent(getContext(), PictureHandlerActivity.class);
                i.putExtra("garden", garden);
                activity.startActivity(i);
            }
        });

        // got this code from https://stackoverflow.com/questions/7693633/android-image-dialog-popup
        ivGardenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGrey();
                MainActivity activity = (MainActivity) getContext();
                Intent i = new Intent(getContext(), ImageActivity.class);
                i.putExtra("photo", garden.getPhoto().getUrl());
                activity.startActivity(i);


            }
        });

        ivGardenImage.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                PhotoBottomDialogFragment photoBottomDialogFragment = PhotoBottomDialogFragment.newInstance(garden);
                photoBottomDialogFragment.show(fm, "fragment_photo_bottom_dialog");
                return true;
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        somePlants = new ArrayList<>();
        plantInBedAdapter = new PlantInBedAdapter(getContext(), somePlants);

        rvPlants.setAdapter(plantInBedAdapter);
        rvPlants.setLayoutManager(horizontalLayoutManager);

        GardenMethodHelper.queryPlantInBed(somePlants, plantInBedAdapter, garden);

        Glide.with(getContext()).load(garden.getPhoto().getUrl()).into(ivGardenImage);

        userReminders = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(getContext(), userReminders, rvReminders, Reminder.KEY_REMIND_WHAT, garden);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvReminders.setAdapter(reminderAdapter);
        rvReminders.setLayoutManager(linearLayoutManager);

        GardenMethodHelper.queryReminders(userReminders, reminderAdapter, Reminder.KEY_REMIND_WHAT, garden);

        hideGrey();
    }

    public static void showGrey() {
        modalGreyLayer.setVisibility(View.VISIBLE);
    }

    public static void hideGrey() {
        modalGreyLayer.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Bundle bundle = data.getExtras();
            GardenMethodHelper.changePic((MainActivity) getContext(), "garden", ivGardenImage, bundle);
    }

}