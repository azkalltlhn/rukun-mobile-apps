package com.example.ru_kun.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ru_kun.R;
import com.example.ru_kun.model.Notification;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;
    private SharedPreferences sharedPreferences;

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView title;
        public TextView description;
        public ImageView icon;
        public ImageView statusIcon;
        public TextView timestamp; // Add TextView for timestamp

        public NotificationViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.notificationTitle);
            description = view.findViewById(R.id.notificationDescription);
            icon = view.findViewById(R.id.notificationIcon);
            statusIcon = view.findViewById(R.id.notificationStatus);
            timestamp = view.findViewById(R.id.timestamp); // Initialize timestamp TextView
        }
    }

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.description.setText(notification.getDescription());
        holder.icon.setImageResource(notification.getIconResId());
        holder.timestamp.setText(notification.getTimestamp()); // Set timestamp

        // Use a unique key for each notification
        String key = notification.getTitle() + "_" + position;

        // Check the saved state of the status icon
        boolean isStatusVisible = sharedPreferences.getBoolean(key, true);
        holder.statusIcon.setVisibility(isStatusVisible ? View.VISIBLE : View.GONE);

        // Set the click listener for the status icon to make it disappear when clicked
        holder.statusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.statusIcon.setVisibility(View.GONE);
                // Save the state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(key, false);
                editor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}