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
import com.example.garden_planner.databinding.ItemReminderBinding;
import com.example.garden_planner.models.Garden;
import com.example.garden_planner.models.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    public static final String TAG = "ReminderAdapter";
    private Context context;
    private List<Reminder> reminders;

    ItemReminderBinding binding;

    public ReminderAdapter(Context context, List<Reminder> reminders){
        Log.i(TAG, "making ReminderAdapter");
        this.context = context;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder ");

        binding = ItemReminderBinding.inflate( LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder " + position);
        Reminder reminder = reminders.get(position);
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvGardenName;
        private TextView tvToDoDate;
        private ImageView ivPlantPic;
        private TextView tvPlantName;
        private TextView tvReminderTitle;
        private TextView tvReminderText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Reminder reminder = reminders.get(position);
                Garden garden = reminder.getRemindWhat();
                // TODO: go to the detail view of garden
            }
        }

        public void bind(Reminder reminder){
            tvGardenName = binding.tvGardenName;
            tvToDoDate = binding.tvToDoDate;
            ivPlantPic = binding.ivPlantPic;
            tvPlantName = binding.tvPlantName;
            tvReminderTitle = binding.tvReminderTitle;
            tvReminderText = binding.tvReminderText;

            tvGardenName.setText(reminder.getRemindWhat().getName());

            if (reminder.getReminderStart().equals(reminder.getReminderEnd())){
                tvToDoDate.setText(reminder.getReminderStart().toString());
            }
            else {
                tvToDoDate.setText(reminder.getReminderStart().toString() + " to " + reminder.getReminderEnd().toString());
            }

            Glide.with(context).load(reminder.getRemindWhichPlant().getPlantType().getPhoto().getUrl()).into(ivPlantPic);

            tvPlantName.setText(reminder.getRemindWhichPlant().getDisplayName());

            tvReminderTitle.setText(reminder.getReminderTitle());

            tvReminderText.setText(reminder.getReminderMessage());

        }

    }
    }
