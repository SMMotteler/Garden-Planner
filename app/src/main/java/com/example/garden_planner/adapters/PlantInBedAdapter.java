package com.example.garden_planner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.databinding.ItemPlantInBedBinding;
import com.example.garden_planner.models.PlantInBed;

import java.util.List;

public class PlantInBedAdapter extends RecyclerView.Adapter<PlantInBedAdapter.ViewHolder> {
    public static final String TAG = "PlantInBedAdapter";
    private Context context;
    private List<PlantInBed> plants;

    ItemPlantInBedBinding binding;

    public PlantInBedAdapter(Context context, List<PlantInBed> plants){
        this.context = context;
        this.plants = plants;
    }

    @NonNull
    @Override
    public PlantInBedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemPlantInBedBinding.inflate( LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantInBed plant = plants.get(position);
        holder.bind(plant);
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPlantPic;
        private TextView tvPlantName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }


        public void bind(PlantInBed plant){
            ivPlantPic = binding.ivPlantPic;
            tvPlantName = binding.tvPlantName;

            tvPlantName.setText(plant.getDisplayName());

            // Note - I know this looks like it should be able to be condensed to one if statement, but
            // if a reminder doesn't have one of the first two keys, then it will throw an error when it checks
            // the following key if it was in a single if statement - this setup prevents any crashes
            if (plant.has(PlantInBed.KEY_TYPE)){
                if (plant.getPlantType().has("photo")){
                    Glide.with(context).load(plant.getPlantType().getPhoto().getUrl()).into(ivPlantPic);
                }
            }

        }
    }
}
