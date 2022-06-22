package com.example.garden_planner.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("PlantInBed")
public class PlantInBed extends ParseObject {
    public static final String KEY_DATE = "PlantDate";
    public static final String KEY_TYPE = "PlantType";

    // TODO: implement when Frost Zone API is set up
    // public Date toPlantByDate;
    // public Date harvestDate;

    public Date getPlantDate(){return getDate(KEY_DATE);}

    public void setPlantDate(Date plantDate){put(KEY_DATE, plantDate);}

    public Plant getPlantType(){return (Plant)get(KEY_TYPE);}

    public void setPlantType(Plant plantType){put(KEY_TYPE, plantType);}
}