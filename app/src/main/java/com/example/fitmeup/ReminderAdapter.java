package com.example.fitmeup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderAdapter extends ListAdapter<Reminder, ReminderAdapter.ReminderHolder> {

    // Make the constructor public to allow access from other classes
    public ReminderAdapter() {
        super(DIFF_CALLBACK);
    }

    // DiffUtil to efficiently update the RecyclerView when data changes
    private static final DiffUtil.ItemCallback<Reminder> DIFF_CALLBACK = new DiffUtil.ItemCallback<Reminder>() {
        @Override
        public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getStartTime().equals(newItem.getStartTime()) &&
                    oldItem.getEndTime().equals(newItem.getEndTime()) &&
                    oldItem.getRemindEvery().equals(newItem.getRemindEvery());
        }
    };

    @NonNull
    @Override
    public ReminderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each reminder item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);
        return new ReminderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderHolder holder, int position) {
        // Bind the reminder data to the ViewHolder
        Reminder currentReminder = getItem(position);
        holder.textViewTitle.setText(currentReminder.getTitle());
        holder.textViewStartTime.setText(currentReminder.getStartTime());
        holder.textViewEndTime.setText(currentReminder.getEndTime());
        holder.textViewRemindEvery.setText(currentReminder.getRemindEvery());
    }

    // ViewHolder to represent each reminder item
    class ReminderHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewStartTime;
        private TextView textViewEndTime;
        private TextView textViewRemindEvery;

        public ReminderHolder(@NonNull View itemView) {
            super(itemView);
            // Match these IDs with the ones defined in your reminder_item.xml layout
            textViewTitle = itemView.findViewById(R.id.reminderTitle);
            textViewStartTime = itemView.findViewById(R.id.startTimeLabel);
            textViewEndTime = itemView.findViewById(R.id.endTimeLabel);
            textViewRemindEvery = itemView.findViewById(R.id.remindEveryLabel);
        }
    }
}
