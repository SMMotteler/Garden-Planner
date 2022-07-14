package com.example.garden_planner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.garden_planner.R;
import com.example.garden_planner.databinding.ItemPlantInBedBinding;
import com.example.garden_planner.databinding.ItemPlantInBedExpandableBinding;
import com.example.garden_planner.models.PlantInBed;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

public class PlantDeletionAdapter  extends RecyclerView.Adapter<PlantDeletionAdapter.ViewHolder> {
    public static final String TAG = "PlantDeletionAdapter";
    private Context context;
    private List<PlantInBed> plants;
    private List<PlantInBed> plantsToDelete;

    ItemPlantInBedExpandableBinding binding;

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

        binding = ItemPlantInBedExpandableBinding.inflate( LayoutInflater.from(context), parent, false);
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
        private ImageView ivArrow;
        private TextView tvDisplayName;
        private TextView tvPlantType;
        private ExpandableLayout expandableLayoutOptions;
        private ExpandableLayout expandableLayoutRename;
        private ConstraintLayout selectionLayout;
        private ConstraintLayout renameLayout;
        private Button btRename;
        private Button btDeletePlant;
        private Button btSetNewName;
        private EditText etPlantName;
        private LinearLayout llBackground;
        private LinearLayout llPlantInBedInfo;

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

        @SuppressLint("ResourceAsColor")
        public void bind(PlantInBed plant){
            llBackground = binding.llBackground;
            ivPlantPic = binding.ivPlantPic;
            ivArrow = binding.ivArrow;
            tvDisplayName = binding.tvDisplayName;
            tvPlantType = binding.tvPlantType;
            expandableLayoutOptions = binding.expandableLayoutOptions;
            expandableLayoutRename = binding.expandableLayoutRename;;
            selectionLayout = binding.selectionLayout;
            renameLayout = binding.renameLayout;
            btRename = binding.btRename;
            btDeletePlant = binding.btDeletePlant;
            btSetNewName = binding.btSetNewName;
            etPlantName = binding.etPlantName;
            llPlantInBedInfo = binding.llPlantInBedInfo;

            llBackground.setBackgroundColor(R.color.canvas);
            tvDisplayName.setText(plant.getDisplayName());


            Glide.with(context).load(plant.getPlantType().getPhoto().getUrl()).into(ivPlantPic);

            llPlantInBedInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableLayoutOptions.isExpanded()) {
                        expandableLayoutOptions.collapse();
                        if (expandableLayoutRename.isExpanded()){
                            expandableLayoutRename.collapse();
                        }
                        Glide.with(context).load(R.drawable.rightarrow).into(ivArrow);
                    }
                    else{
                        Glide.with(context).load(R.drawable.downarrow).into(ivArrow);
                        expandableLayoutOptions.expand();
                    }
                }
            });


            btDeletePlant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: change background of this plant object to red, add it to the removed list
                    // TODO: if clicked again, maybe "unremove" it? (remove from removed list, and change
                    // TODO: the background back)
                    if (((ColorDrawable)llBackground.getBackground()).getColor() == Color.RED){
                        plantsToDelete.remove(plant);
                        llBackground.setBackgroundColor(Color.WHITE);
                    }
                    else{
                        plantsToDelete.add(plant);
                        llBackground.setBackgroundColor(Color.RED);
                    }
                }
            });
        }
    }
}
