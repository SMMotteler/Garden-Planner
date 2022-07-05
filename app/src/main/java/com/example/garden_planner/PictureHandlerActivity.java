package com.example.garden_planner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.garden_planner.models.BitmapScaler;
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
    public final static int RESULT_OK = -1;

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

        tvPhotoText.setText("Uploading a photo for your profile picture.");

        // set visibility of submission views (tvYourPhoto, ivPhotoToUpload, and btUpload) to
        // GONE until a photo is chosen
        tvYourPhoto.setVisibility(View.GONE);
        ivPhotoToUpload.setVisibility(View.GONE);
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

    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider.GardenPlanner", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            Log.i(TAG, " pics :(");
        }

    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
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
                // by this point we have the camera photo on disk
                // Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // resize the photo
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
// by this point we have the camera photo on disk
                rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
// See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb

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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't selected correctly!", Toast.LENGTH_SHORT).show();
            }



        }
        if (rawTakenImage != null){
            Bitmap resizedBitmap = rawTakenImage;
            // while the size of the bitmap is larger than 10 MB, shrink it to 1/4 of its current size
            // - once it is smaller than 10 MB, it is acceptable to be saved to the imageview
            while (resizedBitmap.getByteCount() >= 1000000 * 10) {
                resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, resizedBitmap.getWidth()/2);
            }
// Configure byte output stream
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
// Compress the image further
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
// Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
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
// Write the bytes of the bitmap to file
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
            ivPhotoToUpload.setVisibility(View.VISIBLE);
            btUpload.setVisibility(View.VISIBLE);

            // Load the resized image into a preview
            Bitmap takenImage = BitmapFactory.decodeFile(resizedFile.getAbsolutePath());

            Glide.with(this).load(takenImage).circleCrop().into(ivPhotoToUpload);                Log.i(TAG, "bitmap size: "+BitmapFactory.decodeFile(resizedFile.getAbsolutePath()).getByteCount());
            Log.i(TAG, "original size: "+rawTakenImage.getByteCount());

            btUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser.getCurrentUser().put("profilePic", new ParseFile(resizedFile));
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e("yikes", e.getMessage());
                                Toast.makeText(PictureHandlerActivity.this, "Error in changing profile pic!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            finish();
                        }
                    });
                }
            });
        }
    }



}