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
import com.example.garden_planner.models.Plant;


import java.util.List;

public class PlantAdditionAdapter extends RecyclerView.Adapter<PlantAdditionAdapter.ViewHolder> {
    public static final String TAG = "PlantInBedAdapter";
    private Context context;
    private List<Plant> plants;

    ItemPlantInBedBinding binding;

    public PlantAdditionAdapter(Context context, List<Plant> plants){
        Log.i(TAG, "making PlantInBedAdapter");
        this.context = context;
        this.plants = plants;
    }

    @NonNull
    @Override
    public PlantAdditionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");

        binding = ItemPlantInBedBinding.inflate( LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new PlantAdditionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantAdditionAdapter.ViewHolder holder, int position) {
        Plant plant = plants.get(position);
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
                Plant plant = plants.get(position);
                // TODO: add a plant item of that kind to the garden
            }

        }

        public void bind(Plant plant){
            ivPlantPic = binding.ivPlantPic;
            tvPlantName = binding.tvPlantName;

            // tvPlantName.setText(plant.getDisplayName());


        }
    }
}
