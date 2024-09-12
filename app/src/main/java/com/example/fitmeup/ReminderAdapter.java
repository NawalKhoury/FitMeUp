package com.example.fitmeup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderAdapter extends ListAdapter<Reminder, ReminderAdapter.ReminderHolder> {

    public ReminderAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Reminder> DIFF_CALLBACK = new DiffUtil.ItemCallback<Reminder>() {
        @Override
        public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getChooseTime().equals(newItem.getChooseTime()) &&
                    oldItem.getRemindEvery().equals(newItem.getRemindEvery()) &&
                    oldItem.isNotificationEnabled() == newItem.isNotificationEnabled();  // Added notification enabled state
        }
    };

    @NonNull
    @Override
    public ReminderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);
        return new ReminderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderHolder holder, int position) {
        Reminder currentReminder = getItem(position);
        holder.textViewTitle.setText(currentReminder.getTitle());
        holder.chooseTimeSpinner.setSelection(getSpinnerIndex(holder.chooseTimeSpinner, currentReminder.getChooseTime()));
        holder.remindEverySpinner.setSelection(getSpinnerIndex(holder.remindEverySpinner, currentReminder.getRemindEvery()));
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    class ReminderHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private Spinner chooseTimeSpinner;
        private Spinner remindEverySpinner;

        public ReminderHolder(@NonNull View itemView) {
            super(itemView);
            // Ensure the IDs match the corresponding elements in your reminder_item.xml layout
            textViewTitle = itemView.findViewById(R.id.reminderTitleEdit); // Assuming the ID for title is reminderTitleEdit
            chooseTimeSpinner = itemView.findViewById(R.id.chooseTimeSpinner);
            remindEverySpinner = itemView.findViewById(R.id.remindEverySpinner);
        }
    }
}
