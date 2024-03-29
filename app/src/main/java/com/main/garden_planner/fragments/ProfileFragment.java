package com.main.garden_planner.fragments;

import static com.main.garden_planner.fragments.GardenDetailFragment.ivGardenImage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.main.garden_planner.GardenMethodHelper;
import com.main.garden_planner.MainActivity;
import com.main.garden_planner.PictureHandlerActivity;
import com.example.garden_planner.databinding.FragmentProfileBinding;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Button btLogout;
    private TextView tvUsername;
    private TextView tvUserSince;
    private ImageView ivProfilePic;
    public ParseUser user = ParseUser.getCurrentUser();
    public static final int REQUEST_CODE = 1;
    public final static int RESULT_OK = -1;


    public ProfileFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), parent, false);

        View view = binding.getRoot();

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btLogout = binding.btLogout;
        tvUsername = binding.tvUsername;
        tvUserSince = binding.tvUserSince;
        ivProfilePic = binding.ivProfilePic;

        // prevents buttons from appearing before anything else
        btLogout.setVisibility(View.GONE);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLoginActivity();
            }
        });

        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                user = (ParseUser) object;
                displayUserInfo();
                if (user.hasSameId(ParseUser.getCurrentUser())) {
                    btLogout.setVisibility(View.VISIBLE);
                    ivProfilePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // allow user to change their profile photo
                            MainActivity activity = (MainActivity) getContext();
                            Intent i = new Intent(getContext(), PictureHandlerActivity.class);
                            activity.startActivityForResult(i, REQUEST_CODE);

                        }
                    });

                }
                // if the user isn't the current user, the user can't change their profile picture
                // or log out through their profile

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getContext()).load(GardenMethodHelper.profilePic(user)).transform(new CircleCrop()).into(ivProfilePic);
    }

    private void goLoginActivity() {
        MainActivity activity = (MainActivity) getContext();
        activity.performLogout();
    }

    public void displayUserInfo() {
        tvUsername.setText(user.getUsername());
        Glide.with(getContext()).load(GardenMethodHelper.profilePic(user)).transform(new CircleCrop()).into(ivProfilePic);
        tvUserSince.setText("User since " + (user.getUpdatedAt().getYear() + 1900));

    }

}