package com.example.garden_planner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.databinding.ItemPlantBinding;
import com.example.garden_planner.databinding.ItemPlantInBedBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Plant;
import com.example.garden_planner.models.PlantInBed;

import java.util.ArrayList;
import java.util.List;

public class PlantDeletionAdapter  extends RecyclerView.Adapter<PlantDeletionAdapter.ViewHolder> {
    public static final String TAG = "PlantDeletionAdapter";
    private Context context;
    private List<PlantInBed> plants;
    private List<PlantInBed> plantsToDelete;

    ItemPlantInBedBinding binding;

    public PlantDeletionAdapter(Context context, List<PlantInBed> plants){
        Log.i(TAG, "making PlantInBedAdapter");
        this.context = context;
        this.plants = plants;
        plantsToDelete = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlantDeletionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");

        binding = ItemPlantInBedBinding.inflate( LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new PlantDeletionAdapter.ViewHolder(view);
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

    public List<PlantInBed> getPlantsToDelete(){
        return plantsToDelete;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPlantPic;
        private TextView tvPlantName;
        private Button btDelete;
        private LinearLayout llBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                PlantInBed plant = plants.get(position);
                // TODO: view the details of that plant
            }

        }

        public void bind(PlantInBed plant){
            llBackground = binding.llBackground;
            ivPlantPic = binding.ivPlantPic;
            tvPlantName = binding.tvPlantName;
            btDelete = binding.btDelete;

            tvPlantName.setText(plant.getDisplayName());

            Glide.with(context).load(plant.getPlantType().getPhoto().getUrl()).into(ivPlantPic);

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: change background of this plant object to red, add it to the removed list
                    // TODO: if clicked again, maybe "unremove" it? (remove from removed list, and change
                    // TODO: the background back
                    if (((ColorDrawable)llBackground.getBackground()).getColor() == Color.parseColor("#00FF00")){
                        plantsToDelete.remove(plant);
                        llBackground.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    else{
                        plantsToDelete.add(plant);
                        llBackground.setBackgroundColor(Color.parseColor("#00FF00"));
                    }
                }
            });
        }
    }
}
