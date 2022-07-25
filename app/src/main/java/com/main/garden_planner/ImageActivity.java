package com.main.garden_planner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.databinding.ActivityImageBinding;

public class ImageActivity extends Activity {

    private ActivityImageBinding binding;

    private ImageView mDialog;
    private String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mDialog = binding.yourImage;
        imageUrl = getIntent().getStringExtra("photo");

        Glide.with(this).load(imageUrl).into(mDialog);
        mDialog.setClickable(true);


        //finish the activity (dismiss the image dialog) if the user clicks
        //anywhere on the image
        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}