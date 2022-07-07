package com.example.garden_planner.models;

import com.example.garden_planner.GardenMethodHelper;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.time.LocalDate;
import java.util.Date;

@ParseClassName("PlantInBed")
public class PlantInBed extends ParseObject {
    public static final String KEY_PLANT_DATE = "whenActuallyPlanted";
    public static final String KEY_TYPE = "PlantType";
    public static final String KEY_NAME = "ThisPlantName";
    public static final String KEY_GARDEN= "plantedInGarden";
    public static final String KEY_SHOULD_PLANT_DATE = "whenShouldPlant";
    public static final String KEY_SHOULD_HARVEST_DATE = "whenShouldHarvest";
    public static final String KEY_HARVEST_DATE = "whenActuallyHarvested";

    // TODO: implement when Frost Zone API is set up
    // public Date toPlantByDate;
    // public Date harvestDate;

    public Date getPlantDate(){return getDate(KEY_PLANT_DATE);}

    public void setPlantDate(Date plantDate){
        put(KEY_PLANT_DATE, plantDate);
    }

    public Date getShouldPlantDate(){return getDate(KEY_SHOULD_PLANT_DATE);}

    public void setShouldPlantDate(Date frostDate){
        LocalDate plantDate = GardenMethodHelper.convertToLocalDateViaInstant(frostDate);
        plantDate.plusWeeks(getPlantType().getPlantTime());
        put(KEY_SHOULD_PLANT_DATE, GardenMethodHelper.convertToDate(plantDate));
    }

    public Date getHarvestDate(){return getDate(KEY_HARVEST_DATE);}

    public void setHarvestDate(Date harvestDate){put(KEY_HARVEST_DATE, harvestDate);}

    public Date getShouldHarvestDate(){return getDate(KEY_SHOULD_HARVEST_DATE);}

    public void setShouldHarvestDate(Date plantDate){
        LocalDate harvestDate = GardenMethodHelper.convertToLocalDateViaInstant(plantDate);
        harvestDate.plusWeeks(getPlantType().getHarvestTime());
        put(KEY_SHOULD_HARVEST_DATE, GardenMethodHelper.convertToDate(harvestDate));
    }

    public Plant getPlantType(){return (Plant)get(KEY_TYPE);}

    public void setPlantType(Plant plantType){put(KEY_TYPE, plantType);}

    public String getDisplayName(){return getString(KEY_NAME);}

    public void setDisplayName(String displayName){put(KEY_NAME, displayName);}

    public Garden getGarden(){return (Garden)get(KEY_GARDEN);}

    public void setGarden(Garden garden){put(KEY_GARDEN, garden);}

}
