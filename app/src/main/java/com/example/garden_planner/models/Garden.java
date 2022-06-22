package com.example.garden_planner.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Garden")
public class Garden extends ParseObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";
    public static final String KEY_PLANTS = "plants";

    public String getName(){return getString(KEY_NAME);}

    public void setName(String name){put(KEY_NAME, name);}

    public Long getLatitude(){return getLong(KEY_LATITUDE);}

    public void setLatitude(Long latitude){put(KEY_LATITUDE, latitude);}

    public Long getLongitude(){return getLong(KEY_LONGITUDE);}

    public void setLongitude(Long longitude){put(KEY_LONGITUDE, longitude);}

    public List<PlantInBed> getPlants(){return getList(KEY_PLANTS);}

    public void setPlants(List<PlantInBed> plants){ put(KEY_PLANTS, plants);}

    public void addPlant(PlantInBed plant){
        List<PlantInBed> currentPlants = getPlants();
        currentPlants.add(plant);
        setPlants(currentPlants);
    }
}
