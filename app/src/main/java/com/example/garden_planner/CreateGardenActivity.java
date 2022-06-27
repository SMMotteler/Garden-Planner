package com.example.garden_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.garden_planner.databinding.ActivityCreateGardenBinding;
import com.example.garden_planner.models.Garden;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateGardenActivity extends AppCompatActivity {
    private ActivityCreateGardenBinding binding;

    private EditText etGardenName;
    private EditText etGardenLocation;
    private EditText etLatitude;
    private EditText etLongitude;
    private Button btCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGardenBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        etGardenName = binding.etGardenName;
        etGardenLocation = binding.etGardenLocation;
        etLatitude = binding.etLatitude;
        etLongitude = binding.etLongitude;
        btCreate = binding.btCreate;

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gardenName = etGardenName.getText().toString();
                String gardenLocation = etGardenLocation.getText().toString();
                String longitudeText = etLongitude.getText().toString();
                String latitudeText = etLatitude.getText().toString();

                if(gardenName.isEmpty() || gardenLocation.isEmpty()
                        || longitudeText.isEmpty() || latitudeText.isEmpty()){
                    Toast.makeText(CreateGardenActivity.this, "You're missing a field!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Garden garden = new Garden();
                garden.setLatitude(Long.parseLong(latitudeText));
                garden.setLongitude(Long.parseLong(longitudeText));
                garden.setLocation(gardenLocation);
                garden.setName(gardenName);
                garden.setUser(ParseUser.getCurrentUser());

                garden.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            Log.e("yikes", e.getMessage());
                            Toast.makeText(CreateGardenActivity.this, "Error in making a garden!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        finish();
                    }
                });
            }
        });
    }


}