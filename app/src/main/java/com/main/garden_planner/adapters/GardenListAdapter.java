package com.main.garden_planner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.databinding.ItemGardenBinding;
import com.main.garden_planner.GardenMethodHelper;
import com.main.garden_planner.MainActivity;
import com.main.garden_planner.models.Garden;
import com.main.garden_planner.models.PlantInBed;

import java.util.ArrayList;
import java.util.List;

public class GardenListAdapter extends RecyclerView.Adapter<GardenListAdapter.ViewHolder> {
    public static final String TAG = "GardenAdapter";
    private Context context;
    private List<Garden> gardens;
    private PlantInBedAdapter adapter;
    private List<PlantInBed> somePlants;

    ItemGardenBinding binding;

    public GardenListAdapter(Context context, List<Garden> gardens){
        this.context = context;
        this.gardens = gardens;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemGardenBinding.inflate( LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GardenListAdapter.ViewHolder holder, int position) {
        Garden garden = gardens.get(position);
        holder.bind(garden);
    }

    @Override
    public int getItemCount() {
        return gardens.size();
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

        private TextView tvGardenName;
        private ImageView ivGardenImage;
        private RecyclerView rvPlants;
        private TextView tvGardenLocation;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Garden garden = gardens.get(position);
                MainActivity activity = (MainActivity) context;
                activity.goToDetailGardenView(garden);
            }

        }

        public void bind(Garden garden){

            tvGardenName = binding.tvGardenName;
            ivGardenImage = binding.ivGardenImage;
            rvPlants = binding.rvPlants;
            tvGardenLocation = binding.tvGardenLocation;

            tvGardenName.setText(garden.getName());
            tvGardenLocation.setText("Location: "+garden.getLocation());

            if (garden.has("photo")){
                Glide.with(context).load(garden.getPhoto().getUrl()).into(ivGardenImage);
            }

            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            somePlants = new ArrayList<>();
            adapter = new PlantInBedAdapter(context, somePlants);

            rvPlants.setAdapter(adapter);
            rvPlants.setLayoutManager(horizontalLayoutManager);

            GardenMethodHelper.queryPlantInBed(somePlants, adapter, garden);

        }

    }
}
