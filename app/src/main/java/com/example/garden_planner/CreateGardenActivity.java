package com.example.garden_planner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.garden_planner.databinding.ActivityCreateGardenBinding;
import com.example.garden_planner.models.Garden;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.net.Authenticator;

public class CreateGardenActivity extends AppCompatActivity {
    private ActivityCreateGardenBinding binding;

    private EditText etGardenName;
    private EditText etGardenLocation;
    private EditText etLatitude;
    private EditText etLongitude;
    private Button btCreate;
    public static final int  REQUEST_CHECK_SETTING = 1001;

    LocationRequest locationRequest;

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

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);



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

    public void findLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled(this)){

                    LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);

                            LocationServices.getFusedLocationProviderClient(CreateGardenActivity.this)
                                    .removeLocationUpdates(this);

                            if(locationResult != null && locationResult.getLocations().size()>0){
                                int index = locationResult.getLocations().size() -1;
                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();

                                // TODO: use this latitude and longitude to set the location of the garden
                            }
                        }
                    }, Looper.getMainLooper());

                }else{
                    turnOnGPS();
                }

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    public boolean isGPSEnabled(Context context){
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

    public void turnOnGPS(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(context)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Log.i("gps", "gps is on");
                } catch (ApiException e) {
                    switch (e.getStatusCode()){
                        case LocationSettingsStatusCodes
                                .RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(CreateGardenActivity.this, REQUEST_CHECK_SETTING);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes
                                .SETTINGS_CHANGE_UNAVAILABLE:
                            break;

                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CHECK_SETTING){
            switch (resultCode){
                case Activity.RESULT_OK:
                    Toast.makeText(this, "gps is turned on", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS needs to be turned on", Toast.LENGTH_SHORT).show();
            }
        }
    }
}