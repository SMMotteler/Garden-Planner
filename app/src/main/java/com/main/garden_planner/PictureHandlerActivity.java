package com.main.garden_planner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.garden_planner.databinding.ActivityPictureHandlerBinding;
import com.main.garden_planner.models.Garden;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureHandlerActivity extends AppCompatActivity {
    public static final String TAG = "PictureHandlerActivity";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final int GET_FROM_GALLERY = 3;
    public String photoFileName = "photo.jpg";
    public File photoFile;
    private Garden garden = null;
    public final static int RESULT_OK = -1;
    public static final int REQUEST_CODE = 1;

    private ActivityPictureHandlerBinding binding;
    TextView tvPhotoText;
    Button btTakePhoto;
    Button btChoosePhoto;
    ImageView ivPhotoToUpload;
    ImageView ivGardenPhoto;
    TextView tvYourPhoto;
    Button btUpload;

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
        ivGardenPhoto = binding.ivGardenPhoto;
        tvYourPhoto = binding.tvYourPhoto;
        btUpload = binding.btUpload;

        if (getIntent().hasExtra("garden")){
            garden = getIntent().getParcelableExtra("garden");
            tvPhotoText.setText("Uploading a photo for "+garden.getName()+".");
        }
        else{
            tvPhotoText.setText("Uploading a photo for your profile picture.");
        }
        // set visibility of submission views (tvYourPhoto, ivPhotoToUpload, ivGardenPhoto,
        // and btUpload) to GONE until a photo is chosen
        tvYourPhoto.setVisibility(View.GONE);
        ivPhotoToUpload.setVisibility(View.GONE);
        ivGardenPhoto.setVisibility(View.GONE);
        btUpload.setVisibility(View.GONE);

        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPictures();
            }
        });
    }
    public Bitmap scaleToFitWidth(Bitmap b, int width)
    {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }
    public void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider.main.GardenPlanner", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            return null;
        }


        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;


    }

    public void getPictures(){
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap rawTakenImage = null;
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
                rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == GET_FROM_GALLERY){
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                try {
                    rawTakenImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't selected correctly!", Toast.LENGTH_SHORT).show();
            }



        }
        if (rawTakenImage != null){
            Bitmap resizedBitmap = rawTakenImage;
            while (resizedBitmap.getByteCount() >= 1000000 * 10) {
                resizedBitmap = scaleToFitWidth(rawTakenImage, resizedBitmap.getWidth()/2);
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            File resizedFile = getPhotoFileUri(photoFileName + "_resized");
            try {
                resizedFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(resizedFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // reveal the submission views
            tvYourPhoto.setVisibility(View.VISIBLE);
            btUpload.setVisibility(View.VISIBLE);

            // Load the resized image into a preview
            Bitmap takenImage = BitmapFactory.decodeFile(resizedFile.getAbsolutePath());

            // depending on which photo is uploaded, we reveal the corresponding image preview
            // and fill it with the picture
            if (garden != null) {
                ivGardenPhoto.setVisibility(View.VISIBLE);
                Glide.with(this).load(takenImage).into(ivGardenPhoto);
            }
            else{
                ivPhotoToUpload.setVisibility(View.VISIBLE);
                Glide.with(this).load(takenImage).circleCrop().into(ivPhotoToUpload);
            }

            btUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent();

                    if (garden != null) {
                        ParseFile gardenPic = new ParseFile(resizedFile);
                        garden.setPhoto(gardenPic);
                        garden.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e != null){
                                    e.printStackTrace();
                                    Toast.makeText(PictureHandlerActivity.this, "Error in changing profile pic!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                setResult(RESULT_OK);
                                i.putExtra("garden",gardenPic);
                                finish();
                            }
                        });
                    }
                    else{
                        ParseFile profilePic = new ParseFile(resizedFile);
                        ParseUser.getCurrentUser().put("profilePic", profilePic);
                        ParseUser.getCurrentUser().saveInBackground((new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e != null){
                                    e.printStackTrace();
                                    Toast.makeText(PictureHandlerActivity.this, "Error in changing profile pic!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                setResult(RESULT_OK);
                                i.putExtra("profilePic",profilePic);
                                finish();
                            }
                        }));
                    }

                }
            });
        }
    }



}