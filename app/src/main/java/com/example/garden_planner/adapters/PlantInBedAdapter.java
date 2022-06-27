package com.example.garden_planner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.databinding.ItemPlantInBedBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.PlantInBed;

import java.util.List;

public class PlantInBedAdapter extends RecyclerView.Adapter<PlantInBedAdapter.ViewHolder> {
    public static final String TAG = "PlantInBedAdapter";
    private Context context;
    private List<PlantInBed> plants;

    ItemPlantInBedBinding binding;

    public PlantInBedAdapter(Context context, List<PlantInBed> plants){
        Log.i(TAG, "making PlantInBedAdapter");
        this.context = context;
        this.plants = plants;
    }

    @NonNull
    @Override
    public PlantInBedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");

        binding = ItemPlantInBedBinding.inflate( LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {        Log.i(TAG, "onBindViewHolder " + position);
        PlantInBed plant = plants.get(position);
        holder.bind(plant);
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPlantPic;
        private TextView tvPlantName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                PlantInBed plant = plants.get(position);
                // TODO: maybe set up a detail view for a plant in a bed?
            }

        }

        public void bind(PlantInBed plant){
            ivPlantPic = binding.ivPlantPic;
            tvPlantName = binding.tvPlantName;

            tvPlantName.setText(plant.getThisPlantName());

            if (plant.getPlantType().has("photo")){
                Glide.with(context).load(plant.getPlantType().getPhoto().getUrl()).into(ivPlantPic);
            }
        }
    }
}
