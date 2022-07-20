package com.example.garden_planner.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.garden_planner.GardenMethodHelper;
import com.example.garden_planner.MainActivity;
import com.example.garden_planner.PictureHandlerActivity;
import com.example.garden_planner.databinding.FragmentProfileBinding;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import java.util.HashMap;

public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding binding;
    private Button btLogout;
    private Button btTestPushNotifications;
    private TextView tvUsername;
    private TextView tvUserSince;
    private ImageView ivProfilePic;
    public ParseUser user = ParseUser.getCurrentUser();

    public ProfileFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
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
        btTestPushNotifications = binding.btTestPushNotifications;

        // prevents buttons from appearing before anything else
        btLogout.setVisibility(View.GONE);
        btTestPushNotifications.setVisibility(View.GONE);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLoginActivity();
            }
        });

        btTestPushNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, String> params = new HashMap<>();
// Calling the cloud code function
                ParseCloud.callFunctionInBackground("pushsample", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object response, ParseException exc) {
                        if(exc == null) {
                            // The function was executed, but it's interesting to check its response
                            alertDisplayer("Successful Push","Check on your phone the notifications to confirm!");
                        }
                        else {
                            // Something went wrong
                            Toast.makeText(getContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                user = (ParseUser) object;
                displayUserInfo();
                if(user.hasSameId(ParseUser.getCurrentUser())){
                    btLogout.setVisibility(View.VISIBLE);
                    btTestPushNotifications.setVisibility(View.VISIBLE);
                    ivProfilePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // allow user to change their profile photo
                            MainActivity activity = (MainActivity)getContext();
                            Intent i = new Intent(getContext(), PictureHandlerActivity.class);
                            activity.startActivity(i);
                            // Glide.with(getContext()).load(GardenMethodHelper.profilePic(user)).transform(new CircleCrop()).into(ivProfilePic);

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
        MainActivity activity = (MainActivity)getContext();
        activity.performLogout();
    }

    public void displayUserInfo(){
        tvUsername.setText(user.getUsername());
        Glide.with(getContext()).load(GardenMethodHelper.profilePic(user)).transform(new CircleCrop()).into(ivProfilePic);
        tvUserSince.setText("User since "+ (user.getUpdatedAt().getYear()+1900));

    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
