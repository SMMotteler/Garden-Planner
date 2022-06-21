package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.garden_planner.databinding.ActivityMainBinding;
import com.example.garden_planner.databinding.ActivityPictureHandlerBinding;

public class PictureHandlerActivity extends AppCompatActivity {

    private ActivityPictureHandlerBinding binding;
    TextView tvPhotoText;
    Button btTakePhoto;
    Button btChoosePhoto;
    ImageView ivPhotoToUpload;
    TextView tvYourPhoto;
    Button btUpload;
    TextView tvOr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPictureHandlerBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        tvPhotoText = binding.tvPhotoText;
        btTakePhoto = binding.btTakePhoto;
        btChoosePhoto = binding.btChoosePhoto;
        ivPhotoToUpload = binding.ivPhotoToUpload;
        tvYourPhoto = binding.tvYourPhoto;
        btUpload = binding.btUpload;
        tvOr = binding.tvOr;
    }
}