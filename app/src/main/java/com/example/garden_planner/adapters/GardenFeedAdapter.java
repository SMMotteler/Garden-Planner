package com.example.garden_planner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garden_planner.databinding.ItemGardenBinding;
import com.example.garden_planner.databinding.ItemReminderBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Reminder;

import java.util.List;

public class GardenFeedAdapter extends RecyclerView.Adapter<GardenFeedAdapter.ViewHolder> {
    public static final String TAG = "GardenAdapter";
    private Context context;
    private List<Garden> gardens;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }

        public void bind(Garden garden){

        }
    }
}
