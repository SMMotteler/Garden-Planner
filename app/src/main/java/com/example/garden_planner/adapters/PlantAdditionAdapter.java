package com.example.garden_planner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.R;
import com.example.garden_planner.databinding.ItemPlantBinding;
import com.example.garden_planner.models.Plant;

import java.util.List;

public class PlantAdditionAdapter extends RecyclerView.Adapter<PlantAdditionAdapter.ViewHolder> {
    public static final String TAG = "PlantAdditionAdapter";
    private Context context;
    private List<Plant> plants;
    private Plant plantType = null;

    ItemPlantBinding binding;

    public PlantAdditionAdapter(Context context, List<Plant> plants){
        Log.i(TAG, "making PlantInBedAdapter");
        this.context = context;
        this.plants = plants;
    }

    @NonNull
    @Override
    public PlantAdditionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");

        binding = ItemPlantBinding.inflate( LayoutInflater.from(context), parent, false);
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

    public Plant getPlantType(){
        return plantType;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPlantPic;
        private TextView tvPlantName;
        private TextView tvStartAs;
        private TextView tvWhenPlant;
        private TextView tvWhenHarvest;
        private LinearLayout llBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Plant plant = plants.get(position);
                plantType = plant;
                // TODO: set the type of the PlantInBed object to be of type plant
                // TODO: if there is another plant that has already been clicked,
                // TODO: change its background back to white
                llBackground.setBackgroundResource(R.drawable.login_register_gradient);
            }

        }

        public void bind(Plant plant){
            ivPlantPic = binding.ivPlantPic;
            tvPlantName = binding.tvPlantName;
            tvStartAs = binding.tvStartAs;
            tvWhenHarvest = binding.tvWhenHarvest;
            tvWhenPlant = binding.tvWhenPlant;
            llBackground = binding.llBackground;

            // tvPlantName.setText(plant.getDisplayName());


            Glide.with(context).load(plant.getPhoto().getUrl()).into(ivPlantPic);
            tvPlantName.setText(plant.getName());
            tvWhenHarvest.setText("Harvest "+plant.getHarvestTime()+" weeks after planting outdoors.");
            String startAs = "Plant as seedlings outdoors";
            if (plant.getSeedRec()){
                startAs = "Plant as seeds outdoors";
            }
            tvStartAs.setText(startAs);

            if(plant.getPlantTime() < 0){
                tvWhenPlant.setText("Plant "+(-1* plant.getPlantTime())+" weeks before the last frost.");
            }
            else{
                tvWhenPlant.setText("Plant "+plant.getPlantTime()+" weeks after the last frost.");
            }

        }
    }
}
