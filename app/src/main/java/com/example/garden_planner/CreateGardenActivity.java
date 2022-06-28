package com.example.garden_planner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.garden_planner.databinding.ActivityCreateGardenBinding;
import com.example.garden_planner.models.FrostDateClient;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.GeocodingClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;

public class CreateGardenActivity extends AppCompatActivity {
    private ActivityCreateGardenBinding binding;

    private EditText etGardenName;
    private EditText etGardenLocation;
    private Button btFindLocation;
    private Button btCreate;
    public static final int  REQUEST_CHECK_SETTING = 1001;
    private double latitude;
    private double longitude;
    private GeocodingClient geocodingClient;
    private FrostDateClient frostDateClient;

    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGardenBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        etGardenName = binding.etGardenName;
        etGardenLocation = binding.etGardenLocation;
        btFindLocation = binding.btFindLocation;
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

                if(gardenName.isEmpty() || gardenLocation.isEmpty()){
                    Toast.makeText(CreateGardenActivity.this, "You're missing a field!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                geocodingClient.forwardGeocoding(gardenLocation, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            latitude = json.jsonObject.getJSONArray("data").getJSONObject(0).getDouble("latitude");
                            longitude = json.jsonObject.getJSONArray("data").getJSONObject(0).getDouble("longitude");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("create garden", response, throwable);
                        // Toast.makeText(CreateGardenActivity.this, "Error with finding location! Please try again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                Garden garden = new Garden();
                garden.setLocation(gardenLocation);
                garden.setName(gardenName);
                garden.setUser(ParseUser.getCurrentUser());
                garden.setLongitude(Double.valueOf(longitude).longValue());
                garden.setLatitude(Double.valueOf(latitude).longValue());

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

        btFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findLocation();
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
                                latitude = locationResult.getLocations().get(index).getLatitude();
                                longitude = locationResult.getLocations().get(index).getLongitude();

                                Log.i("create garden", "latitude "+latitude+" longitude "+longitude);

                                // TODO: use this latitude and longitude to set the location of the garden
                                geocodingClient = new GeocodingClient();
                                geocodingClient.reverseGeocoding(latitude, longitude, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                                        try {
                                            String address = json.jsonObject.getJSONArray("data")
                                                    .getJSONObject(1).getString("label");
                                            etGardenLocation.setText(address);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                        Log.e("create garden", response, throwable);
                                    }
                                });
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

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
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

    // information for this method is from https://github.com/waldoj/frostline
    public  void initializeGardenInformation(double latitude, double longitude) throws JSONException, IOException {
        frostDateClient.getStations(latitude, longitude, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // the API for the stations list them in order of increasing distance, so we can always
                // take the first JSONObject
                try {
                    JSONObject station = json.jsonArray.getJSONObject(0);
                    frostDateClient.getFrostDate(station.getString("id"), 1, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            // the API for the frost dates has the 32 deg. threshold as the second entry in
                            // the array every time, which we are using as the temperature where no more frost
                            // will occur
                            try {
                                JSONObject lastFrostDateInfo = json.jsonArray.getJSONObject(1);
                                String lastFrostDateDay = lastFrostDateInfo.getString("prob_50");
                                // return the last frost date
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("initializeGardenInformation", "error with getting frost dates", throwable);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("initializeGardenInformation", "error with getting stations", throwable);
            }
        });
    }

}