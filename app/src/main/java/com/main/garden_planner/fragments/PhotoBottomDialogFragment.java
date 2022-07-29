package com.main.garden_planner.fragments;

import static com.main.garden_planner.fragments.GardenDetailFragment.ivGardenImage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.main.garden_planner.GardenMethodHelper;
import com.main.garden_planner.ImageActivity;
import com.main.garden_planner.MainActivity;
import com.main.garden_planner.PictureHandlerActivity;
import com.example.garden_planner.databinding.FragmentPhotoDialogBinding;
import com.main.garden_planner.models.Garden;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PhotoBottomDialogFragment extends BottomSheetDialogFragment {

    private TextView viewModalText;
    private TextView editImageText;
    private FragmentPhotoDialogBinding binding;
    public static final int REQUEST_CODE = 1;
    public final static int RESULT_OK = -1;


    public PhotoBottomDialogFragment() {

    }

    public static PhotoBottomDialogFragment newInstance(Garden garden) {
        PhotoBottomDialogFragment fragment = new PhotoBottomDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("garden", garden);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhotoDialogBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GardenDetailFragment fragment = (GardenDetailFragment) this.getParentFragment();

        viewModalText = binding.viewModalText;
        editImageText = binding.editImageText;


        viewModalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showGrey();
                Garden garden = getArguments().getParcelable("garden");
                MainActivity activity = (MainActivity) getContext();
                Intent i = new Intent(getContext(), ImageActivity.class);
                i.putExtra("photo", garden.getPhoto().getUrl());
                activity.startActivity(i);
                dismiss();
            }
        });

        editImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Garden garden = getArguments().getParcelable("garden");
                MainActivity activity = (MainActivity) getContext();
                Intent i = new Intent(getContext(), PictureHandlerActivity.class);
                i.putExtra("garden", garden);
                activity.startActivityForResult(i, 1);
                dismiss();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Bundle bundle = data.getExtras();
            GardenMethodHelper.changePic((MainActivity)getContext(), "garden", ivGardenImage, bundle);

    }
}
