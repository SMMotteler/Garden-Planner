package com.example.garden_planner.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseGeoPoint;

import java.util.List;

@ParseClassName("Garden")
public class Garden extends ParseObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PLANTS = "plants";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_USER = "whoseGarden";
    public static final String KEY_LATLONG = "latLong";

    public String getName(){return getString(KEY_NAME);}

    public void setName(String name){put(KEY_NAME, name);}

    public double getLatitude(){return Double.valueOf(getString(KEY_LATITUDE));}

    public void setLatitude(double latitude){put(KEY_LATITUDE, ""+latitude);}

    public double[] getLatLong(){
        double[] location = {getParseGeoPoint(KEY_LATLONG).getLatitude(), getParseGeoPoint(KEY_LATLONG).getLongitude()};
        return location;
    }

    public void setLatLong(double latitude, double longitude){put(KEY_LATLONG, new ParseGeoPoint(latitude, longitude));}


    public double getLongitude(){return Double.valueOf(getString(KEY_LONGITUDE));}

    public void setLongitude(double longitude){put(KEY_LONGITUDE, ""+longitude);}

    public List<PlantInBed> getPlants(){return getList(KEY_PLANTS);}

    public void setPlants(List<PlantInBed> plants){ put(KEY_PLANTS, plants);}

    public void addPlant(PlantInBed plant){
        List<PlantInBed> currentPlants = getPlants();
        currentPlants.add(plant);
        setPlants(currentPlants);
    }

    public String getLocation(){return getString(KEY_LOCATION);}

    public void setLocation(String location){put(KEY_LOCATION, location);}

    public ParseFile getPhoto(){return getParseFile(KEY_PHOTO);}

    public void setPhoto(ParseFile photo){put(KEY_PHOTO, photo);}

    public ParseUser getUser(){return getParseUser(KEY_USER);}

    public void setUser(ParseUser user){put(KEY_USER, user);}


}
