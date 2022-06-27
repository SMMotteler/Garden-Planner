package com.example.garden_planner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.GardenMethodHelper;
import com.example.garden_planner.MainActivity;
import com.example.garden_planner.databinding.ItemGardenBinding;
import com.example.garden_planner.databinding.ItemReminderBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.PlantInBed;
import com.example.garden_planner.models.Reminder;

import java.util.ArrayList;
import java.util.List;

public class GardenFeedAdapter extends RecyclerView.Adapter<GardenFeedAdapter.ViewHolder> {
    public static final String TAG = "GardenAdapter";
    private Context context;
    private List<Garden> gardens;
    private PlantInBedAdapter adapter;
    private List<PlantInBed> somePlants;

    ItemGardenBinding binding;

    public GardenFeedAdapter(Context context, List<Garden> gardens){
        Log.i(TAG, "making GardenFeedAdapter");
        this.context = context;
        this.gardens = gardens;
    }

    @NonNull
    @Override
    public GardenFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");

        binding = ItemGardenBinding.inflate( LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GardenFeedAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
        Garden garden = gardens.get(position);
        holder.bind(garden);
    }

    @Override
    public int getItemCount() {
        return gardens.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivGardenIcon;
        private TextView tvGardenName;
        private ImageView ivGardenImage;
        private RecyclerView rvPlants;
        private TextView tvGardenLocation;
        private Button btDetailView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            Log.i("click", "click");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Garden garden = gardens.get(position);
                Log.i("clicked", "clicked "+garden.getName());
                MainActivity activity = new MainActivity();
                activity.goToDetailGardenView(garden);
            }

        }

        public void bind(Garden garden){
            ivGardenIcon = binding.ivGardenIcon;
            tvGardenName = binding.tvGardenName;
            ivGardenImage = binding.ivGardenImage;
            rvPlants = binding.rvPlants;
            tvGardenLocation = binding.tvGardenLocation;
            btDetailView = binding.btDetailView;

            tvGardenName.setText(garden.getName());
            tvGardenLocation.setText("Location: "+garden.getLocation());

            if (garden.has("photo")){
                Glide.with(context).load(garden.getParseFile("photo").getUrl()).into(ivGardenImage);
            }

            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            somePlants = new ArrayList<>();
            adapter = new PlantInBedAdapter(context, somePlants);

            rvPlants.setAdapter(adapter);
            rvPlants.setLayoutManager(horizontalLayoutManager);

            GardenMethodHelper.queryPlantInBed(somePlants, adapter, garden);

            btDetailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("click", "click");
                    MainActivity activity = (MainActivity) context;
                    activity.goToDetailGardenView(garden);

                }
            });
        }
    }
}
