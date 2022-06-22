package com.example.garden_planner.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Plant")
public class Plant  extends ParseObject {
    public static final String KEY_HARDINESS = "hardinessZones";
    public static final String KEY_NAME = "name";
    public static final String KEY_HARVEST_ADVICE = "harvestAdvice";
    public static final String KEY_PLANT_TIME = "weeksBeforeAfterFrostPlant";
    public static final String KEY_HARVEST_TIME = "weeksUntilHarvest";
    public static final String KEY_PLANT_ADVICE = "plantingAdvice";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_SEED_REC = "recommendStartAsSeed";

    public List getHardiness(){return (List)get(KEY_HARDINESS);}

    public void setHardiness(List hardiness){put(KEY_HARDINESS, hardiness);}

    public String getName(){return getString(KEY_NAME);}

    public void setName(String name){put(KEY_NAME, name);}

    public String getHarvestAdvice(){return getString(KEY_HARVEST_ADVICE);}

    public void setHarvestAdvice(String harvestAdvice){put(KEY_HARVEST_ADVICE, harvestAdvice);}

    public int getPlantTime(){return getInt(KEY_PLANT_TIME);}

    public void setPlantTime(int plantTime){put(KEY_PLANT_TIME, plantTime);}

    public int getHarvestTime(){return getInt(KEY_HARVEST_TIME);}

    public void setHarvestTime(int harvestTime){put(KEY_HARVEST_TIME, harvestTime);}

    public String getPlantAdvice(){return getString(KEY_PLANT_ADVICE);}

    public void setPlantAdvice(String plantAdvice){put(KEY_PLANT_ADVICE, plantAdvice);}

    public ParseFile getPhoto(){return getParseFile(KEY_PHOTO);}

    public void setPhoto(ParseFile photo){put(KEY_PHOTO, photo);}

    public Boolean getSeedRec(){return getBoolean(KEY_SEED_REC);}

    public void setSeedRec(Boolean seedRec){put(KEY_SEED_REC, seedRec);}
}
